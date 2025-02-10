package com.example.locaquest.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import lombok.RequiredArgsConstructor;

import com.example.locaquest.component.JwtTokenManager;
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
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;
    private final RedisService redisService;

    public void preregisterUser(User user) {
        if (isEmailExists(user.getEmail())) {
            throw new EmailExistsException(user.getEmail());
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        redisService.savePreregisterUser(user);
        sendAuthMail(user.getEmail(), "/template/register/accept");
    }

    public User registerUser(String token) {
        String email = getEmailByAuthMailToken(token);
        if (isEmailExists(email)) {
            throw new EmailExistsException(email);
        }
        User user = redisService.getPreregisterUser(email);
        if (user == null) {
            throw new ServiceException("data not exists in Redis: " + email);
        }
        redisService.deletePreregisterUser(email);
        redisService.saveAuthToken(token);
        User registerdUser = userRepository.save(user);
        userStatisticRepository.save(new UserStatistic(registerdUser.getUserId()));
        return registerdUser;
    }

    public String registerCheckVerified(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new EmailNotExistsException(email);
        }
        return jwtTokenManager.generateLoginToken(String.valueOf(user.getUserId()), user.getName());
    }

    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if(user == null) {
            throw new EmailNotExistsException(loginRequest.getEmail());
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new WrongPasswordException(loginRequest.getEmail());
        }
        return jwtTokenManager.generateLoginToken(String.valueOf(user.getUserId()), user.getName());
    }

    public void updatePasswordSendAuthEmail(String email) {
        if (!isEmailExists(email)) {
            throw new EmailNotExistsException(email);
        }
        sendAuthMail(email, "/template/update-password/accept");
    }

    public String updatePasswordVerifyAuthEmail(String token) {
        String email = getEmailByAuthMailToken(token);
        redisService.saveChangePasswordEmail(email);
        redisService.saveAuthToken(token);
        return email;
    }

    public boolean updatePasswordCheckVerified(String email) {
        String value = redisService.getChangePasswordEmail(email);
        return value != null;
    }

    @Transactional
    public void updatePasswordByEmail(String password, String email) {
        String encodedPassword = passwordEncoder.encode(password);
        if(userRepository.updatePassword(encodedPassword, email) == 0) {
            throw new ServiceException("Failed to update password: " + email);
        }
        redisService.deleteChangePasswordEmail(email);
    }

    public String updateUser(int userId, User newUser) {
        User user = userRepository.findByUserId(userId);

        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        user.setPassword(encodedPassword);
        user.setName(newUser.getName());
        userRepository.save(user);
        return jwtTokenManager.generateLoginToken(String.valueOf(user.getUserId()), user.getName());
    }

    @Transactional
    public void deleteUser(int userId, String password) {
        User user = userRepository.findByUserId(userId);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new WrongPasswordException(user.getEmail());
        }
        if (userRepository.deleteByUserId(userId) == 0) {
            throw new ServiceException("Failed to delete user: " + user.getEmail());
        }
    }

    public void checkAuthTokenUsedWithException(String token) {
        String data = redisService.getAuthToken(token);
        if(data != null) {
            throw new AlreadyVerifiedException(token);
        }
    }
    
    private boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private void sendAuthMail(String email, String serverUrl) {
        String token = jwtTokenManager.generateAuthToken(email);
        String tokenUrl = createServerUrlForAuthMail(serverUrl, token);
        emailSender.sendAuthMail(email, tokenUrl);
    }

    private String createServerUrlForAuthMail(String url, String token) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
            .replacePath(url)
            .queryParam("token", token)
            .toUriString();
    }

    private String getEmailByAuthMailToken(String token) {
        jwtTokenManager.validateAuthTokenWithException(token);
        return jwtTokenManager.getEmailByAuthToken(token);
    }
}
