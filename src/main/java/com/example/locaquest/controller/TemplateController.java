package com.example.locaquest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.locaquest.constant.Route;
import com.example.locaquest.model.User;
import com.example.locaquest.service.UserService;
import com.example.locaquest.util.LogUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Route.TEMPLATE)
@RequiredArgsConstructor
public class TemplateController {

    private final UserService userService;
    private final SpringTemplateEngine templateEngine;
    private final String filePath = "controller.TemplateController";

    @GetMapping(Route.TEMPLATE_REGISTER_ACCREPT)
    public String registerUser(@RequestParam String token, HttpServletRequest request) {
        userService.checkAuthTokenUsedWithException(token);
        User registedUser = userService.registerUser(token);
        LogUtil.info(String.format("successfully: email=%s", registedUser.getEmail()), filePath, Route.TEMPLATE_REGISTER_ACCREPT, request);
        return templateEngine.process("VerifyAccept", new Context());
    }

    @GetMapping(Route.TEMPLATE_UPDATE_PASSWORD_ACCREPT)
    public String updatePasswordVerifyAuthEmail(@RequestParam String token, HttpServletRequest request) {
        userService.checkAuthTokenUsedWithException(token);
        String email = userService.updatePasswordVerifyAuthEmail(token);
        LogUtil.info(String.format("successfully: email=%s", email), filePath, Route.TEMPLATE_UPDATE_PASSWORD_ACCREPT, request);
        return templateEngine.process("VerifyAccept", new Context());
    }
}
