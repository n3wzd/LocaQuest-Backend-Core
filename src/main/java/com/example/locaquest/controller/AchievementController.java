package com.example.locaquest.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.model.Achievement;
import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.service.AchievementService;
import com.example.locaquest.service.UserService;

@RestController
@RequestMapping("/achievements")
public class AchievementController {

    private final UserService userService;
    private final AchievementService achievementService;
    static final private Logger logger = LoggerFactory.getLogger(UserController.class);

    public AchievementController(UserService userService, AchievementService achievementService) {
        this.userService = userService;
        this.achievementService = achievementService;
    }

    @PostMapping("/find")
    public ResponseEntity<?> searchPlaces() {
        int userId = userService.getCurrentUserId();
        List<Achievement> result = achievementService.getUserAchievementList(userId);
        logger.info("searchPlaces successful: userId={}", userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/scan-statistic")
    public ResponseEntity<?> searchPlaces(@RequestBody UserStatistic userStatistic) {
        achievementService.updateUserAchievementByUserStatistic(userStatistic);
        logger.info("searchPlaces successful: userId={}", userService.getCurrentUserId());
        return ResponseEntity.ok("");
    }
}
