package com.example.locaquest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.constant.Route;
import com.example.locaquest.model.Achievement;
import com.example.locaquest.service.UserStatusService;
import com.example.locaquest.util.LogUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Route.ACHIEVEMENT)
@RequiredArgsConstructor
public class AchievementController {
    private final UserStatusService userStatusService;
    private final String filePath = "controller.AchievementController";
    
    @GetMapping(Route.ACHIEVEMENT_ALL)
    public ResponseEntity<?> getAchievements(HttpServletRequest request) {
        List<Achievement> result = userStatusService.getAchievementList();
        LogUtil.info(String.format("successfully"), filePath, Route.ACHIEVEMENT_ALL, request);
        return ResponseEntity.ok(result);
    }
}
