package com.example.locaquest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.locaquest.model.User;
import com.example.locaquest.service.UserService;

@RestController
@RequestMapping("/template")
public class TemplateController {

    private final UserService userService;
    private final SpringTemplateEngine templateEngine;
    static final private Logger logger = LoggerFactory.getLogger(UserController.class);

    public TemplateController(UserService userService, SpringTemplateEngine templateEngine) {
        this.userService = userService;
        this.templateEngine = templateEngine;
    }

    @GetMapping("/register/accept")
    public String registerUser(@RequestParam String token) {
        userService.checkAuthTokenUsedWithException(token);
        User registedUser = userService.registerUser(token);
        logger.info("registerUser successfully: email={}", registedUser.getEmail());
        return templateEngine.process("VerifyAccept", new Context());
    }

    @GetMapping("/update-password/accept")
    public String updatePasswordVerifyAuthEmail(@RequestParam String token) {
        userService.checkAuthTokenUsedWithException(token);
        String email = userService.updatePasswordVerifyAuthEmail(token);
        logger.info("updatePasswordVerifyAuthEmail successfully: email={}", email);
        return templateEngine.process("VerifyAccept", new Context());
    }
}
