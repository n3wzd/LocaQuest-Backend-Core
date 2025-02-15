package com.example.locaquest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.constant.Route;
import com.example.locaquest.dto.user.EmailRequest;
import com.example.locaquest.dto.user.LoginRequest;
import com.example.locaquest.dto.user.PasswordRequest;
import com.example.locaquest.dto.group.CreateGroup;
import com.example.locaquest.dto.group.PasswordGroup;
import com.example.locaquest.dto.group.UpdateGroup;
import com.example.locaquest.model.User;
import com.example.locaquest.service.UserService;
import com.example.locaquest.util.LogUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import com.example.locaquest.service.TokenService;

@RestController
@RequestMapping(Route.USER)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final String filePath = "controller.UserController";

    @PostMapping(Route.USER_REGISTER_MAIL)
    public ResponseEntity<?> registerSendAuthMail(@Validated(CreateGroup.class) @RequestBody User user, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid dto");
        }
        userService.preregisterUser(user);
        LogUtil.info(String.format("successfully: email=%s", user.getEmail()), filePath, Route.USER_REGISTER_MAIL, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
    }

    @PostMapping(Route.USER_REGISTER_VERIFIED)
    public ResponseEntity<?> registerCheckVerified(@RequestBody EmailRequest emailRequest, HttpServletRequest request) {
        String email = emailRequest.getEmail();
        String token = userService.registerCheckVerified(email);
        LogUtil.info(String.format("successfully: email=%s", email), filePath, Route.USER_REGISTER_VERIFIED, request);
        return ResponseEntity.ok(token);
    }
    
    @PostMapping(Route.USER_LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String token = userService.login(loginRequest);
        LogUtil.info(String.format("successfully: email=%s", loginRequest.getEmail()), filePath, Route.USER_LOGIN, request);
        return ResponseEntity.ok(token);
    }

    @PostMapping(Route.USER_UPDATE_PASSWORD_MAIL)
    public ResponseEntity<?> updatePasswordSendAuthEmail(@RequestBody EmailRequest emailRequest, HttpServletRequest request) {
        String email = emailRequest.getEmail();
        userService.updatePasswordSendAuthEmail(email);
        LogUtil.info(String.format("successfully: email=%s", email), filePath, Route.USER_UPDATE_PASSWORD_MAIL, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
    }

    @PostMapping(Route.USER_UPDATE_PASSWORD_VERIFIED)
    public ResponseEntity<?> updatePasswordCheckVerified(@RequestBody EmailRequest emailRequest, HttpServletRequest request) {
        String email = emailRequest.getEmail();
        boolean res = userService.updatePasswordCheckVerified(email);
        LogUtil.info(String.format("successfully: email=%s", email), filePath, Route.USER_UPDATE_PASSWORD_VERIFIED, request);
        return ResponseEntity.ok(res);
    }

    @PostMapping(Route.USER_UPDATE_PASSWORD)
    public ResponseEntity<?> updatePassword(@Validated(PasswordGroup.class) @RequestBody User user, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid dto");
        }
        userService.updatePasswordByEmail(user.getPassword(), user.getEmail());
        LogUtil.info(String.format("successfully: email=%s", user.getEmail()), filePath, Route.USER_UPDATE_PASSWORD, request);
        return ResponseEntity.ok("");
    }

    @PostMapping(Route.USER_UPDATE)
    public ResponseEntity<?> updateUser(@Validated(UpdateGroup.class) @RequestBody User user, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid dto");
        }
        int userId = tokenService.getUserId();
        String token = userService.updateUser(userId, user);
        LogUtil.info(String.format("successfully: userId=%s", userId), filePath, Route.USER_UPDATE, request);
        return ResponseEntity.ok(token);
    }

    @PostMapping(Route.USER_DELETE)
    public ResponseEntity<?> deleteUser(@RequestBody PasswordRequest passwordRequest, HttpServletRequest request) {
        int userId = tokenService.getUserId();
        userService.deleteUser(userId, passwordRequest.getPassword());
        LogUtil.info(String.format("successfully: userId=%s", userId), filePath, Route.USER_DELETE, request);
        return ResponseEntity.ok("");
    }
}
