package com.example.locaquest.service;

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

    public UserService(UserRepository userRepository, EmailSender emailSender, PasswordEncoder passwordEncoder, JwtTokenManager jwtTokenManager) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenManager = jwtTokenManager;
    }

    public String getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    public User registerUser(User user) {
        if (isEmailExists(user.getEmail())) {
            throw new ServiceException("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User registerdUser = userRepository.save(user);
        return registerdUser;
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
            throw new ServiceException("Invalid password");
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
            throw new ServiceException(String.format("Email Error: %s", e.toString()));
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
            throw new ServiceException(String.format("Email Error: {}", e.toString()));
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

    @Transactional
    public void updatePasswordByUserId(String password, String userId) {
        String encodedPassword = passwordEncoder.encode(password);
        if(userRepository.updatePassword(encodedPassword, userId) == 0) {
            throw new ServiceException("Failed to update password");
        }
    }

    public User updateUser(User newUser) {
        String userId = getCurrentUserId();
        User user = userRepository.findByUserId(userId);

        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        user.setPassword(encodedPassword);
        user.setEmail(newUser.getEmail());
        user.setName(newUser.getName());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String password) {
        String userId = getCurrentUserId();
        User user = userRepository.findByUserId(userId);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServiceException("Invalid password");
        }
        if (userRepository.deleteByUserId(userId) == 0) {
            throw new ServiceException("Failed to delete user");
        }
    }
}
