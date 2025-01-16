package com.example.reviewsplash.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.reviewsplash.component.JwtTokenProvider;
import com.example.reviewsplash.dto.LoginRequest;
import com.example.reviewsplash.exception.ServiceException;
import com.example.reviewsplash.model.User;
import com.example.reviewsplash.repogitory.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    static final private Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, EmailSender emailSender, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User registerUser(User user) {
        if (isEmailExists(user.getEmail())) {
            String reason = "Email already exists";
            logger.warn("User registration failed: email={}, reason={}", user.getEmail(), reason);
            throw new ServiceException(reason);
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        try {
            User registerdUser = userRepository.save(user);
            logger.info("User registered successfully: userId={}, email={}", user.getUserId(), user.getEmail());
            return registerdUser;
        } catch(IllegalArgumentException | OptimisticLockingFailureException e) {
            String reason = "Database Error";
            logger.warn("User registration failed: email={}, reason={}", user.getEmail(), reason);
            throw new ServiceException(reason);
        }
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public String authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByUserId(loginRequest.getUserId());
        if(user == null) {
            String reason = "User not found";
            logger.warn("User login failed: reason={}", reason);
            throw new ServiceException(reason);
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String reason = "Invalid password";
            logger.warn("User login failed: userId={}, reason={}", user.getUserId(), reason);
            throw new ServiceException(reason);
        }

        logger.info("User login successful: userId={}", user.getUserId());
        return jwtTokenProvider.generateToken(user.getUserId());
    }

    public void findUserId(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            String reason = "User not found";
            logger.warn("ID retrieval failed: reason={}", reason);
            throw new ServiceException(reason);
        }
        try {
            String title = "Your User ID Has Been Successfully Sent";
            String contents = String.format("User ID: %s", user.getUserId());
            emailSender.sendEmail(user.getEmail(), title, contents);
            logger.info("ID retrieval successful: email={}", user.getEmail());
        } catch(ServiceException e) {
            String reason = "Email Error";
            logger.warn("ID retrieval failed: email={}, reason={}", user.getEmail(), reason);
            throw new ServiceException(reason);
        }
    }
}
