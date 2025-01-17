package com.example.reviewsplash.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.reviewsplash.dto.LoginRequest;
import com.example.reviewsplash.dto.LoginResponse;
import com.example.reviewsplash.exception.ServiceException;
import com.example.reviewsplash.model.User;
import com.example.reviewsplash.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    static final private Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            String prevPassword = user.getPassword();
            userService.registerUser(user);

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUserId(user.getUserId());
            loginRequest.setPassword(prevPassword);

            String token = userService.authenticate(loginRequest);
            logger.info("registerUser successfully: userId={}, email={}", user.getUserId(), user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token));
        } catch (ServiceException e) {
            logger.warn("registerUser failed: {}", e.toString());
            return ResponseEntity.badRequest().body("registerUser failed");
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean isAvailable = !userService.isEmailExists(email);
        return ResponseEntity.ok(isAvailable);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.authenticate(loginRequest);
            logger.info("login successful: userId={}", loginRequest.getUserId());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (ServiceException e) {
            logger.warn("login failed: {}", e.toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login failed");
        }
    }

    @GetMapping("/find-id")
    public ResponseEntity<?> findUserId(@RequestParam String email) {
        try {
            userService.findUserId(email);
            logger.info("findUserId successful: email={}", email);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Authentication email sent successfully.");
        } catch (ServiceException e) {
            logger.warn("findUserId failed: {}", e.toString());
            return ResponseEntity.badRequest().body("findUserId failed");
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyByEmail(@RequestParam String token) {
        boolean isAvailable = !userService.isEmailExists(token);
        return ResponseEntity.ok(isAvailable);
    }
}
