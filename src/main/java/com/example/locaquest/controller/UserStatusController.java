package com.example.locaquest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.constant.Route;
import com.example.locaquest.dto.status.AchieveRequest;
import com.example.locaquest.dto.status.AchievementData;
import com.example.locaquest.dto.status.UserStatusResponse;
import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.service.TokenService;
import com.example.locaquest.service.UserStatusService;
import com.example.locaquest.util.LogUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(Route.USER_STATUS)
public class UserStatusController {

    private final TokenService tokenService;
    private final UserStatusService userStatusService;
    private final String filePath = "controller.UserStatusController";

    public UserStatusController(TokenService tokenService, UserStatusService userStatusService) {
        this.tokenService = tokenService;
        this.userStatusService = userStatusService;
    }

    @PostMapping(Route.USER_STATUS_ALL)
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        int userId = tokenService.getUserId();
        UserStatistic userStatstic = userStatusService.getUserStatistics(userId);
        List<AchievementData> achievementList = userStatusService.getAllUserAchievements(userId);
        int exp = userStatstic.getExp();

        UserStatusResponse result = new UserStatusResponse();
        result.setExp(exp);
        result.setLevel(1);
        result.setSteps(userStatstic.getSteps());
        result.setDistance(userStatstic.getDistance());
        result.setAchievementList(achievementList);
        LogUtil.info(String.format("successfully: userId=%s", userId), filePath, Route.USER_STATUS_ALL, request);
        return ResponseEntity.ok(result);
    }

    @GetMapping(Route.USER_STATUS_STATISTIC)
    public ResponseEntity<?> getStatistics(@PathVariable int userId, HttpServletRequest request) {
        UserStatistic result = userStatusService.getUserStatistics(userId);
        LogUtil.info(String.format("successfully: userId=%s", userId), filePath, Route.USER_STATUS_STATISTIC, request);
        return ResponseEntity.ok(result);
    }

    @GetMapping(Route.USER_STATUS_ACHIEVEMENT)
    public ResponseEntity<?> getAchievements(@PathVariable int userId, HttpServletRequest request) {
        List<AchievementData> result = userStatusService.getAllUserAchievements(userId);
        LogUtil.info(String.format("successfully: userId=%s", userId), filePath, Route.USER_STATUS_ACHIEVEMENT, request);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping(Route.USER_STATUS_ACHIEVE)
    public ResponseEntity<?> achieve(@RequestBody AchieveRequest achieveRequest, HttpServletRequest request) {
        int userId = achieveRequest.getUserId();
        int achvId = achieveRequest.getAchvId();
        userStatusService.achieveAchievement(userId, achvId);
        LogUtil.info(String.format("successfully: userId=%s, achvId=%s", userId, achvId), filePath, Route.USER_STATUS_ACHIEVE, request);
        return ResponseEntity.ok("");
    }
}
