package com.example.reviewsplash.service;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public String authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByUserId(loginRequest.getUserId());
        if(user == null) {
            throw new ServiceException("User not found");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ServiceException(String.format("Invalid password: userId=%s", user.getUserId()));
        }
        return jwtTokenProvider.generateLoginToken(user.getUserId());
    }

    public void sendAuthLinkMail(String email) {

    }

    public void findUserId(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new ServiceException("User not found");
        }
        try {
            String title = "Your User ID Has Been Successfully Sent";
            String contents = String.format("User ID: %s", user.getUserId());
            emailSender.sendEmail(user.getEmail(), title, contents);
        } catch(ServiceException e) {
            throw new ServiceException(String.format("Email Error: email=%s, reason=%s", user.getEmail(), e.toString()));
        }
    }
}
