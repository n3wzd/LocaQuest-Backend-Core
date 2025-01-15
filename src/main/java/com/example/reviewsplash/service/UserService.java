package com.example.reviewsplash.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.reviewsplash.component.JwtTokenProvider;
import com.example.reviewsplash.dto.LoginRequest;
import com.example.reviewsplash.model.User;
import com.example.reviewsplash.repogitory.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User registerUser(User user) {
        if (isEmailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public String authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByUserId(loginRequest.getUserId());
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return jwtTokenProvider.generateToken(user.getUserId());
    }
}
