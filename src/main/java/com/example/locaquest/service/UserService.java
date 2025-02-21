package com.example.locaquest.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import lombok.RequiredArgsConstructor;

import com.example.locaquest.component.EmailComponent;
import com.example.locaquest.component.TokenComponent;
import com.example.locaquest.component.RedisComponent;
import com.example.locaquest.dto.user.LoginRequest;
import com.example.locaquest.exception.AlreadyVerifiedException;
import com.example.locaquest.exception.EmailExistsException;
import com.example.locaquest.exception.EmailNotExistsException;
import com.example.locaquest.exception.ServiceException;
import com.example.locaquest.exception.WrongPasswordException;
import com.example.locaquest.model.User;
import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.repogitory.UserRepository;
import com.example.locaquest.repogitory.UserStatisticRepository;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserStatisticRepository userStatisticRepository;
    private final EmailComponent emailComponent;
    private final PasswordEncoder passwordEncoder;
    private final TokenComponent tokenComponent;
    private final RedisComponent redisComponent;

    public void preregisterUser(User user) {
        if (isEmailExists(user.getEmail())) {
            throw new EmailExistsException(user.getEmail());
        }
        String encodedPassword = passwordEncoder.encode(decryptRSAPassword(user.getPassword()));
        user.setPassword(encodedPassword);
        redisComponent.savePreregisterUser(user);
        sendAuthMail(user.getEmail(), "/template/register/accept");
    }

    public User registerUser(String token) {
        String email = getEmailByAuthMailToken(token);
        if (isEmailExists(email)) {
            throw new EmailExistsException(email);
        }
        User user = redisComponent.getPreregisterUser(email);
        if (user == null) {
            throw new ServiceException("data not exists in Redis: " + email);
        }
        redisComponent.deletePreregisterUser(email);
        redisComponent.saveAuthToken(token);
        User registerdUser = userRepository.save(user);
        userStatisticRepository.save(new UserStatistic(registerdUser.getUserId(), registerdUser.getCreatedAt().toLocalDate().toString()));
        return registerdUser;
    }

    public String registerCheckVerified(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new EmailNotExistsException(email);
        }
        return tokenComponent.generateLoginToken(String.valueOf(user.getUserId()), user.getName());
    }

    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if(user == null) {
            throw new EmailNotExistsException(loginRequest.getEmail());
        }
        if (!passwordEncoder.matches(decryptRSAPassword(loginRequest.getPassword()), user.getPassword())) {
            throw new WrongPasswordException(loginRequest.getEmail());
        }
        return tokenComponent.generateLoginToken(String.valueOf(user.getUserId()), user.getName());
    }

    public void updatePasswordSendAuthEmail(String email) {
        if (!isEmailExists(email)) {
            throw new EmailNotExistsException(email);
        }
        sendAuthMail(email, "/template/update-password/accept");
    }

    public String updatePasswordVerifyAuthEmail(String token) {
        String email = getEmailByAuthMailToken(token);
        redisComponent.saveChangePasswordEmail(email);
        redisComponent.saveAuthToken(token);
        return email;
    }

    public boolean updatePasswordCheckVerified(String email) {
        String value = redisComponent.getChangePasswordEmail(email);
        return value != null;
    }

    @Transactional
    public void updatePasswordByEmail(String password, String email) {
        String encodedPassword = passwordEncoder.encode(decryptRSAPassword(password));
        if(userRepository.updatePassword(encodedPassword, email) == 0) {
            throw new ServiceException("Failed to update password: " + email);
        }
        redisComponent.deleteChangePasswordEmail(email);
    }

    public String updateUser(int userId, User newUser) {
        User user = userRepository.findByUserId(userId);
        String encodedPassword = passwordEncoder.encode(decryptRSAPassword(newUser.getPassword()));
        user.setPassword(encodedPassword);
        user.setName(newUser.getName());
        userRepository.save(user);
        return tokenComponent.generateLoginToken(String.valueOf(user.getUserId()), user.getName());
    }

    @Transactional
    public void deleteUser(int userId, String password) {
        User user = userRepository.findByUserId(userId);
        if (!passwordEncoder.matches(decryptRSAPassword(password), user.getPassword())) {
            throw new WrongPasswordException(user.getEmail());
        }
        if (userRepository.deleteByUserId(userId) == 0) {
            throw new ServiceException("Failed to delete user: " + user.getEmail());
        }
    }

    public void checkAuthTokenUsedWithException(String token) {
        String data = redisComponent.getAuthToken(token);
        if(data != null) {
            throw new AlreadyVerifiedException(token);
        }
    }
    
    private boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private void sendAuthMail(String email, String serverUrl) {
        String token = tokenComponent.generateAuthToken(email);
        String tokenUrl = createServerUrlForAuthMail(serverUrl, token);
        emailComponent.sendAuthMail(email, tokenUrl);
    }

    private String createServerUrlForAuthMail(String url, String token) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
            .replacePath(url)
            .queryParam("token", token)
            .toUriString();
    }

    private String getEmailByAuthMailToken(String token) {
    	tokenComponent.validateAuthTokenWithException(token);
        return tokenComponent.getEmailByAuthToken(token);
    }
    
    private String decryptRSAPassword(String encryptedPassword) {
    	return encryptedPassword;
    	/*try {
			return Crypto.decryptRSA(encryptedPassword);
		} catch (Exception e) {
			throw new ServiceException("RSM Error:" + e.toString());
		}*/
    }
}
