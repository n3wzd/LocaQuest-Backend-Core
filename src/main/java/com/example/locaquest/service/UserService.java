package com.example.locaquest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.locaquest.component.JwtTokenManager;
import com.example.locaquest.dto.LoginRequest;
import com.example.locaquest.exception.ServiceException;
import com.example.locaquest.model.User;
import com.example.locaquest.repogitory.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;
    private final RedisService redisService;
    static final private Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, EmailSender emailSender, PasswordEncoder passwordEncoder, JwtTokenManager jwtTokenManager, RedisService redisService) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenManager = jwtTokenManager;
        this.redisService = redisService;
    }

    public int getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Integer.parseInt(userDetails.getUsername());
    }

    public void preregisterUser(User user) {
        if (isEmailExists(user.getEmail())) {
            throw new ServiceException("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        redisService.savePreregisterUser(user);
        sendAuthMail(user.getEmail(), "/api/users/register/accept");
    }

    public User registerUser(String token) {
        String email = getEmailByAuthMailToken(token);
        if (isEmailExists(email)) {
            throw new ServiceException("Email already exists");
        }
        User user = redisService.getPreregisterUser(email);
        if (user == null) {
            throw new ServiceException("can't find User in Redis");
        }
        redisService.deletePreregisterUser(email);
        User registerdUser = userRepository.save(user);
        return registerdUser;
    }

    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if(user == null) {
            throw new ServiceException("User not found");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ServiceException("Invalid password");
        }
        return jwtTokenManager.generateLoginToken(String.valueOf(user.getUserId()));
    }

    public void updatePasswordSendAuthEmail(String email) {
        if (!isEmailExists(email)) {
            throw new ServiceException("Email not exists");
        }
        sendAuthMail(email, "/api/users/update-password/accept");
    }

    public String updatePasswordVerifyAuthEmail(String token) {
        String email = getEmailByAuthMailToken(token);
        redisService.saveChangePasswordEmail(email);
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
            throw new ServiceException("Failed to update password");
        }
        redisService.deleteChangePasswordEmail(email);
    }

    public User updateUser(User newUser) {
        int userId = getCurrentUserId();
        User user = userRepository.findByUserId(userId);

        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        user.setPassword(encodedPassword);
        user.setName(newUser.getName());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String password) {
        int userId = getCurrentUserId();
        User user = userRepository.findByUserId(userId);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServiceException("Invalid password");
        }
        if (userRepository.deleteByUserId(userId) == 0) {
            throw new ServiceException("Failed to delete user");
        }
    }
    
    private boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private void sendAuthMail(String email, String serverUrl) {
        String token = jwtTokenManager.generateAuthToken(email);
        String tokenUrl = createServerUrlForAuthMail(serverUrl, token);
        try {
            emailSender.sendAuthMail(email, tokenUrl);
        } catch(ServiceException e) {
            throw new ServiceException(String.format("Email Error: {}", e.toString()));
        }
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
