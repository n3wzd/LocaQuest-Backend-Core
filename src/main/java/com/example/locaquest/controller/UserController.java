package com.example.locaquest.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.dto.AuthEmailRequest;
import com.example.locaquest.dto.LoginRequest;
import com.example.locaquest.dto.group.CreateGroup;
import com.example.locaquest.dto.group.PasswordGroup;
import com.example.locaquest.dto.group.UpdateGroup;
import com.example.locaquest.exception.ServiceException;
import com.example.locaquest.exception.TokenException;
import com.example.locaquest.model.User;
import com.example.locaquest.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    static final private Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated(CreateGroup.class) @RequestBody User user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid dto");
            }

            String prevPassword = user.getPassword();
            User registedUser = userService.registerUser(user);

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUserId(registedUser.getUserId());
            loginRequest.setPassword(prevPassword);

            String token = userService.login(loginRequest);
            logger.info("registerUser successfully: userId={}", registedUser.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(token);
        } catch (ServiceException | IllegalArgumentException e) {
            logger.warn("registerUser failed: userId={}, {}", user.getUserId(), e.toString());
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
            return ResponseEntity.ok(token);
        } catch (ServiceException e) {
            logger.warn("login failed: userId={}, {}", loginRequest.getUserId(), e.toString());
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
            logger.warn("findUserId failed: email={}, {}", email, e.toString());
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
            logger.warn("sendAuthEmailFromRegister failed: email={}, {}", authEmailRequest.getEmail(), e.toString());
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
            logger.warn("verifyAuthEmail failed: token={}, {}", token, e.toString());
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
            logger.warn("findPassword failed: userId={}, {}", authEmailRequest.getUserId(), e.toString());
            return ResponseEntity.badRequest().body("findPassword failed");
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@Validated(PasswordGroup.class) @RequestBody User user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid dto");
            }
            userService.updatePasswordByUserId(user.getPassword(), user.getUserId());
            logger.info("updatePassword successful: userId={}", user.getUserId());
            return ResponseEntity.ok("Password updated successfully.");
        } catch (ServiceException | IllegalArgumentException e) {
            logger.warn("updatePassword failed: userId={}, {}", user.getUserId(), e.toString());
            return ResponseEntity.badRequest().body("updatePassword failed");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@Validated(UpdateGroup.class) @RequestBody User user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid dto");
            }
            userService.updateUser(user);
            logger.info("updateUser successful: userId={}", userService.getCurrentUserId());
            return ResponseEntity.ok("User Profile updated successfully.");
        } catch (ServiceException | IllegalArgumentException e) {
            logger.warn("updateUser failed: userId={}, {}", userService.getCurrentUserId(), e.toString());
            return ResponseEntity.badRequest().body("updateUser failed");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody String password) {
        try {
            userService.deleteUser(password);
            logger.info("deleteUser successful: userId={}", userService.getCurrentUserId());
            return ResponseEntity.ok("Goodbye!");
        } catch (ServiceException e) {
            logger.warn("deleteUser failed: userId={}, {}", userService.getCurrentUserId(), e.toString());
            return ResponseEntity.badRequest().body("deleteUser failed");
        }
    }
}
