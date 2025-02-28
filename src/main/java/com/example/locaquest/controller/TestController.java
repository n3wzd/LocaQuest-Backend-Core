package com.example.locaquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.constant.Route;
import com.example.locaquest.dto.status.UserParamGain;
import com.example.locaquest.service.UserService;
import com.example.locaquest.service.UserStatusService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Route.TEST)
@RequiredArgsConstructor
public class TestController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @GetMapping(Route.TEST_LOGIN + "/{userId}")
    public ResponseEntity<?> uploadFile(@PathVariable("userId") int userId) {
        return ResponseEntity.ok(userService.getLoginToken(userId, ""));
    }
    
    @PostMapping(Route.TEST_GAIN_USE_PARAM)
    public ResponseEntity<?> gainUserParam(@RequestBody UserParamGain paramGain) {
        return ResponseEntity.ok(userStatusService.gainParam(paramGain));
    }
}
