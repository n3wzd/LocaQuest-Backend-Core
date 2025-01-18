package com.example.reviewsplash.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.reviewsplash.component.JwtTokenManager;
import com.example.reviewsplash.dto.LoginRequest;
import com.example.reviewsplash.exception.ServiceException;
import com.example.reviewsplash.model.User;
import com.example.reviewsplash.repogitory.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;
    static final private Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, EmailSender emailSender, PasswordEncoder passwordEncoder, JwtTokenManager jwtTokenManager) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenManager = jwtTokenManager;
    }

    public User registerUser(User user) {
        if (isEmailExists(user.getEmail())) {
            throw new ServiceException("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        try {
            User registerdUser = userRepository.save(user);
            return registerdUser;
        } catch(IllegalArgumentException | OptimisticLockingFailureException e) {
            throw new ServiceException(String.format("Database Error: %s", e.toString()));
        }
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByUserId(loginRequest.getUserId());
        if(user == null) {
            throw new ServiceException("User not found");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ServiceException(String.format("Invalid password: userId=%s", user.getUserId()));
        }
        return jwtTokenManager.generateLoginToken(user.getUserId());
    }

    public void findUserId(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new ServiceException("User not found");
        }
        try {
            emailSender.sendUserIDMail(email, user.getUserId());
        } catch(ServiceException e) {
            throw new ServiceException(String.format("Email Error: email=%s, reason=%s", user.getEmail(), e.toString()));
        }
    }

    public void sendAuthMail(String email, String redirectUrl) {
        String token = jwtTokenManager.generateAuthToken(email, redirectUrl);
        String linkUrl = ServletUriComponentsBuilder.fromCurrentRequestUri()
            .replacePath("/api/users/verify-email")
            .queryParam("token", token)
            .toUriString();
        try {
            emailSender.sendAuthMail(email, linkUrl);
        } catch(ServiceException e) {
            throw new ServiceException(String.format("Email Error: email=%s, reason=%s", email, e.toString()));
        }
    }

    public String getRedicrectUrlByAuthMailToken(String token) {
        jwtTokenManager.validateAuthTokenWithException(token);
        return jwtTokenManager.getRedirectUrlByAuthToken(token);
    }

    public void sendAuthMailByUserId(String userId, String redirectUrl) {
        User user = userRepository.findByUserId(userId);
        if(user == null) {
            throw new ServiceException("User not found");
        }
        sendAuthMail(user.getEmail(), redirectUrl);
    }

    public void updatePasswordByUserId(String password, String userId) {
        String encodedPassword = passwordEncoder.encode(password);
        if(userRepository.updatePassword(encodedPassword, userId) == 0) {
            throw new ServiceException("Failed to update password");
        }
    }

    public User updateUser(User newUser, String token) {
        jwtTokenManager.validateAuthTokenWithException(token);
        String userId = jwtTokenManager.getUserIdByLoginToken(token);
        User currentUser = userRepository.findByUserId(userId);

        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        currentUser.setPassword(encodedPassword);
        currentUser.setEmail(newUser.getEmail());
        currentUser.setName(newUser.getName());
        try {
            User updatedUser = userRepository.save(currentUser);
            return updatedUser;
        } catch(IllegalArgumentException | OptimisticLockingFailureException e) {
            throw new ServiceException(String.format("Database Error: %s", e.toString()));
        }
    }

    public User checkPassword(String password, String token) {
        jwtTokenManager.validateAuthTokenWithException(token);
        String userId = jwtTokenManager.getUserIdByLoginToken(token);
        User user = userRepository.findByUserId(userId);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServiceException(String.format("Invalid password: userId=%s", user.getUserId()));
        }
        return user;
    }
}
