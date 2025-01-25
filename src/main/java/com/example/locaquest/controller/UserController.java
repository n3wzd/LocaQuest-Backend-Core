package com.example.locaquest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.locaquest.dto.LoginRequest;
import com.example.locaquest.dto.group.CreateGroup;
import com.example.locaquest.dto.group.PasswordGroup;
import com.example.locaquest.dto.group.UpdateGroup;

import com.example.locaquest.model.User;
import com.example.locaquest.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    static final private Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, SpringTemplateEngine templateEngine) {
        this.userService = userService;
    }

    @PostMapping("/register/send-auth-mail")
    public ResponseEntity<?> registerSendAuthMail(@Validated(CreateGroup.class) @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid dto");
        }
        userService.preregisterUser(user);
        logger.info("registerSendAuthMail successfully: email={}", user.getEmail());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
    }

    @PostMapping("/register/check-verified")
    public ResponseEntity<?> registerCheckVerified(@RequestBody String email) {
        String token = userService.registerCheckVerified(email);
        logger.info("registerCheckVerified successful: email={}", email);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = userService.login(loginRequest);
        logger.info("login successful: email={}", loginRequest.getEmail());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/update-password/send-auth-email")
    public ResponseEntity<?> updatePasswordSendAuthEmail(@RequestBody String email) {
        userService.updatePasswordSendAuthEmail(email);
        logger.info("updatePasswordSendAuthEmail successful: email={}", email);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
    }

    @PostMapping("/update-password/check-verified")
    public ResponseEntity<?> updatePasswordCheckVerified(@RequestBody String email) {
        boolean res = userService.updatePasswordCheckVerified(email);
        logger.info("updatePasswordCheckVerified successful: email={}", email);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@Validated(PasswordGroup.class) @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid dto");
        }
        userService.updatePasswordByEmail(user.getPassword(), user.getEmail());
        logger.info("updatePassword successful: email={}", user.getEmail());
        return ResponseEntity.ok("Password updated successfully.");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@Validated(UpdateGroup.class) @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid dto");
        }
        userService.updateUser(user);
        logger.info("updateUser successful: userId={}", userService.getCurrentUserId());
        return ResponseEntity.ok("User Profile updated successfully.");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody String password) {
        userService.deleteUser(password);
        logger.info("deleteUser successful: userId={}", userService.getCurrentUserId());
        return ResponseEntity.ok("");
    }
}
