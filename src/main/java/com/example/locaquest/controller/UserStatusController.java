package com.example.locaquest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.constant.Route;
import com.example.locaquest.dto.status.AchieveRequest;
import com.example.locaquest.dto.status.UserAchievementData;
import com.example.locaquest.dto.status.UserStatusStartRequest;
import com.example.locaquest.dto.status.UserStatusStartResponse;
import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.service.UserStatusService;
import com.example.locaquest.util.LogUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Route.USER_STATUS)
@RequiredArgsConstructor
public class UserStatusController {

    private final UserStatusService userStatusService;
    private final String filePath = "controller.UserStatusController";

    @PostMapping(Route.USER_STATUS_START)
    public ResponseEntity<?> getAll(@RequestBody UserStatusStartRequest attRequest, HttpServletRequest request) {
    	int userId = attRequest.getUserId();
    	String date = attRequest.getDate();
    	
    	boolean isAttend = userStatusService.updateAttend(userId, date);
    	List<UserStatistic> userStatstic = userStatusService.getUserStatistics(userId);
        List<UserAchievementData> userAchievementList = userStatusService.getUserAchievements(userId);
        
        UserStatusStartResponse result = new UserStatusStartResponse();
        result.setUserStatisticList(userStatstic);
        result.setUserAchievementList(userAchievementList);
        result.setAttend(isAttend);
        LogUtil.info(String.format("successfully: userId=%s, date=%s", userId, date), filePath, Route.USER_STATUS_START, request);
        return ResponseEntity.ok(result);
    } 
    
    @PostMapping(Route.USER_STATUS_ACHIEVE)
    public ResponseEntity<?> achieve(@RequestBody AchieveRequest achieveRequest, HttpServletRequest request) {
        int userId = achieveRequest.getUserId();
        int achvId = achieveRequest.getAchvId();
        String achievedAt = achieveRequest.getAchievedAt();
        userStatusService.achieveAchievement(userId, achvId, achievedAt);
        LogUtil.info(String.format("successfully: userId=%s, achvId=%s", userId, achvId), filePath, Route.USER_STATUS_ACHIEVE, request);
        return ResponseEntity.ok("");
    }
}
