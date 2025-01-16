package com.example.reviewsplash.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.reviewsplash.dto.LoginRequest;
import com.example.reviewsplash.dto.LoginResponse;
import com.example.reviewsplash.exception.ServiceException;
import com.example.reviewsplash.model.User;
import com.example.reviewsplash.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUserId(user.getUserId());
            loginRequest.setPassword(user.getPassword());

            String token = userService.authenticate(loginRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token));
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/find-id")
    public ResponseEntity<?> findUserId(@RequestParam String email) {
        boolean isAvailable = !userService.isEmailExists(email);
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyByEmail(@RequestParam String token) {
        boolean isAvailable = !userService.isEmailExists(token);
        return ResponseEntity.ok(isAvailable);
    }
}
