package com.example.locaquest.controller;

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
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.locaquest.dto.LoginRequest;
import com.example.locaquest.dto.group.CreateGroup;
import com.example.locaquest.dto.group.PasswordGroup;
import com.example.locaquest.dto.group.UpdateGroup;
import com.example.locaquest.exception.ServiceException;
import com.example.locaquest.model.User;
import com.example.locaquest.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SpringTemplateEngine templateEngine;
    static final private Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, SpringTemplateEngine templateEngine) {
        this.userService = userService;
        this.templateEngine = templateEngine;
    }

    @PostMapping("/register/send-auth-mail")
    public ResponseEntity<?> registerSendAuthMail(@Validated(CreateGroup.class) @RequestBody User user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid dto");
            }
            userService.preregisterUser(user);
            logger.info("registerSendAuthMail successfully: email={}", user.getEmail());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("registerSendAuthMail successfully");
        } catch (ServiceException | IllegalArgumentException e) {
            logger.warn("registerSendAuthMail failed: email={}, {}", user.getEmail(), e.toString());
            return ResponseEntity.badRequest().body("registerSendAuthMail failed");
        }
    }

    @GetMapping("/register/accept")
    public String registerUser(@RequestParam String token) {
        try {
            User registedUser = userService.registerUser(token);
            logger.info("registerUser successfully: email={}", registedUser.getEmail());
            return templateEngine.process("RegisterAccept", new Context());
        } catch (ServiceException | IllegalArgumentException e) {
            logger.warn("registerUser failed: token={}, {}", token, e.toString());
            return templateEngine.process("RegisterFailed", new Context());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.login(loginRequest);
            logger.info("login successful: email={}", loginRequest.getEmail());
            return ResponseEntity.ok(token);
        } catch (ServiceException e) {
            logger.warn("login failed: email={}, {}", loginRequest.getEmail(), e.toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login failed");
        }
    }

    @PostMapping("/update-password/send-auth-email")
    public ResponseEntity<?> updatePasswordSendAuthEmail(@RequestBody String email) {
        try {
            userService.updatePasswordSendAuthEmail(email);
            logger.info("updatePasswordSendAuthEmail successful: email={}", email);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Authentication email sent successfully.");
        } catch (ServiceException e) {
            logger.warn("updatePasswordSendAuthEmail failed: email={}, {}", email, e.toString());
            return ResponseEntity.badRequest().body("updatePasswordSendAuthEmail failed");
        }
    }

    @GetMapping("/update-password/accept")
    public String updatePasswordVerifyAuthEmail(@RequestParam String token) {
        try {
            String email = userService.updatePasswordVerifyAuthEmail(token);
            logger.info("updatePasswordVerifyAuthEmail successfully: email={}", email);
            return templateEngine.process("UpdatePasswordAccept", new Context());
        } catch (ServiceException | IllegalArgumentException e) {
            logger.warn("updatePasswordVerifyAuthEmail failed: token={}, {}", token, e.toString());
            return templateEngine.process("UpdatePasswordFailed", new Context());
        }
    }

    @PostMapping("/update-password/check-verified")
    public ResponseEntity<?> updatePasswordCheckVerified(@RequestBody String email) {
        try {
            boolean res = userService.updatePasswordCheckVerified(email);
            logger.info("updatePasswordCheckVerified successful: email={}", email);
            return ResponseEntity.ok().body(res);
        } catch (ServiceException e) {
            logger.warn("updatePasswordCheckVerified failed: email={}, {}", email, e.toString());
            return ResponseEntity.badRequest().body("updatePasswordCheckVerified failed");
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@Validated(PasswordGroup.class) @RequestBody User user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid dto");
            }
            userService.updatePasswordByEmail(user.getPassword(), user.getEmail());
            logger.info("updatePassword successful: email={}", user.getEmail());
            return ResponseEntity.ok("Password updated successfully.");
        } catch (ServiceException | IllegalArgumentException e) {
            logger.warn("updatePassword failed: email={}, {}", user.getEmail(), e.toString());
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
