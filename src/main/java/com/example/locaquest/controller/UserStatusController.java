package com.example.locaquest.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.dto.UserStatusResponse;
import com.example.locaquest.dto.AchievementData;
import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.service.TokenService;
import com.example.locaquest.service.UserStatusService;

@RestController
@RequestMapping("/user-status")
public class UserStatusController {

    private final TokenService tokenService;
    private final UserStatusService userStatusService;
    static final private Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserStatusController(TokenService tokenService, UserStatusService userStatusService) {
        this.tokenService = tokenService;
        this.userStatusService = userStatusService;
    }

    @PostMapping("/")
    public ResponseEntity<?> getAll() {
        int userId = tokenService.getUserId();
        UserStatistic userStatstic = userStatusService.getUserStatistics(userId);
        List<AchievementData> achievementList = userStatusService.getAllUserAchievements(userId);

        UserStatusResponse result = new UserStatusResponse();
        result.setUserStatistic(userStatstic);
        result.setAchievementList(achievementList);
        logger.info("getAll successful: userId={}", userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/statistics")
    public ResponseEntity<?> getStatistics() {
        int userId = tokenService.getUserId();
        UserStatistic result = userStatusService.getUserStatistics(userId);
        logger.info("getStatistics successful: userId={}", userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/achievements")
    public ResponseEntity<?> getAchievements() {
        int userId = tokenService.getUserId();
        List<AchievementData> result = userStatusService.getAllUserAchievements(userId);
        logger.info("getAchievements successful: userId={}", userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/achievements/scan-statistic")
    public ResponseEntity<?> scanStatistic(@RequestBody UserStatistic userStatistic) {
        int userId = tokenService.getUserId();
        userStatusService.updateUserAchievementByUserStatistic(userId, userStatistic);
        logger.info("scanStatistic successful: userId={}", userId);
        return ResponseEntity.ok("");
    }
}
