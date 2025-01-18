package com.example.reviewsplash.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.reviewsplash.dto.AuthEmailRequest;
import com.example.reviewsplash.dto.LoginRequest;
import com.example.reviewsplash.dto.LoginResponse;
import com.example.reviewsplash.exception.ServiceException;
import com.example.reviewsplash.exception.TokenException;
import com.example.reviewsplash.model.User;
import com.example.reviewsplash.service.UserService;
import com.example.reviewsplash.util.TokenParser;

import jakarta.servlet.http.HttpServletRequest;

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
            User registedUser = userService.registerUser(user);

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUserId(registedUser.getUserId());
            loginRequest.setPassword(prevPassword);

            String token = userService.login(loginRequest);
            logger.info("registerUser successfully: userId={}, email={}", registedUser.getUserId(), registedUser.getEmail());
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
            String token = userService.login(loginRequest);
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

    @PostMapping("/send-auth-email")
    public ResponseEntity<?> sendAuthEmail(@RequestBody AuthEmailRequest authEmailRequest) {
        try {
            userService.sendAuthMail(authEmailRequest.getEmail(), authEmailRequest.getRedirectUrl());
            logger.info("sendAuthEmailFromRegister successful: email={}", authEmailRequest.getEmail());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Authentication email sent successfully.");
        } catch (ServiceException e) {
            logger.warn("sendAuthEmailFromRegister failed: {}", e.toString());
            return ResponseEntity.badRequest().body("sendAuthEmailFromRegister failed");
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyAuthEmail(@RequestParam String token) {
        try {
            String redirectUrl = userService.getRedicrectUrlByAuthMailToken(token);
            logger.info("verifyAuthEmail successful: redirectUrl={}", redirectUrl);
            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
        } catch (ServiceException | TokenException e) {
            logger.warn("verifyAuthEmail failed: {}", e.toString());
            return ResponseEntity.badRequest().body("verifyAuthEmail failed");
        }
    }

    @PostMapping("/send-auth-email-userid")
    public ResponseEntity<?> sendAuthEmailByUserId(@RequestBody AuthEmailRequest authEmailRequest) {
        try {
            userService.sendAuthMailByUserId(authEmailRequest.getUserId(), authEmailRequest.getRedirectUrl());
            logger.info("findPassword successful: userId={}", authEmailRequest.getUserId());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Authentication email sent successfully.");
        } catch (ServiceException e) {
            logger.warn("findPassword failed: {}", e.toString());
            return ResponseEntity.badRequest().body("findPassword failed");
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody LoginRequest loginRequest) {
        try {
            userService.updatePasswordByUserId(loginRequest.getPassword(), loginRequest.getUserId());
            logger.info("updatePassword successful: userId={}", loginRequest.getUserId());
            return ResponseEntity.ok("Password updated successfully.");
        } catch (ServiceException e) {
            logger.warn("updatePassword failed: {}", e.toString());
            return ResponseEntity.badRequest().body("updatePassword failed");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateUser(HttpServletRequest request, @RequestBody User user) {
        String token = TokenParser.extractToken(request);
        try {
            User updatedUser = userService.updateUser(user, token);
            logger.info("updateUser successful: userId={}", updatedUser.getUserId());
            return ResponseEntity.ok("User Profile updated successfully.");
        } catch (ServiceException | TokenException e) {
            logger.warn("updateUser failed: {}", e.toString());
            return ResponseEntity.badRequest().body("updateUser failed");
        }
    }

    @PostMapping("/check-password")
    public ResponseEntity<?> checkPassword(HttpServletRequest request, @RequestBody String password) {
        String token = TokenParser.extractToken(request);
        try {
            User user = userService.checkPassword(password, token);
            logger.info("checkPassword successful: userId={}", user.getUserId());
            return ResponseEntity.ok("Password Correct!");
        } catch (ServiceException | TokenException e) {
            logger.warn("checkPassword failed: {}", e.toString());
            return ResponseEntity.badRequest().body("checkPassword failed");
        }
    }
}
