package com.example.locaquest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.locaquest.dto.status.AchievementData;
import com.example.locaquest.model.Achievement;
import com.example.locaquest.model.UserAchievement;
import com.example.locaquest.model.UserAchievementKey;
import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.repogitory.AchievementRepository;
import com.example.locaquest.repogitory.UserAchievementRepository;
import com.example.locaquest.repogitory.UserStatisticRepository;
import com.example.locaquest.component.RedisComponent;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final UserStatisticRepository userStatisticRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final RedisComponent redisComponent;
    private List<Achievement> achievementList;

    @PostConstruct
    public void init() {
        achievementList = this.achievementRepository.findAll();
    }

    public UserStatistic getUserStatistics(int userId) {
        return userStatisticRepository.findByUserId(userId);
    }

    public List<AchievementData> getAllUserAchievements(int userId) {
        Map<String, String> userAchievementMap = getUserAchievementMap(userId);
        List<AchievementData> userAchvList = new ArrayList<>();
        UserStatistic userData = getUserStatistics(userId);
        for(Achievement achv : achievementList) {
            int progress = getAchievementProgress(achv.getAchvId(), userData);
            String achieveAt = userAchievementMap.get(String.valueOf(achv.getAchvId()));
            userAchvList.add(new AchievementData(achv.getAchvId(), achv.getName(), achv.getDesc(), progress, achieveAt));
        }
        return userAchvList;
    }

    public List<AchievementData> getAchievedUserAchievements(int userId) {
        Map<String, String> userAchievementMap = getUserAchievementMap(userId);
        List<AchievementData> userAchvList = new ArrayList<>();
        for(Achievement achv : achievementList) {
            String achieveAt = userAchievementMap.get(String.valueOf(achv.getAchvId()));
            if(achieveAt != null) {
                userAchvList.add(new AchievementData(achv.getAchvId(), achv.getName(), achv.getDesc(), 100, achieveAt));
            }
        }
        return userAchvList;
    }

    public void updateUserAchievementByUserStatistic(int userId, UserStatistic userData) {
        for(Achievement achv: achievementList) {
            int achvId = achv.getAchvId();
            if(checkAchievementCondition(achvId, userData)) {
                achieveAchievement(userId, achvId);
            }
        }
    }

    public void achieveAchievement(int userId, int achvId) {
        if(!hasAchievement(userId, achvId)) {
            UserAchievement userAchv = new UserAchievement();
            userAchv.setId(new UserAchievementKey(userId, achvId));
            userAchievementRepository.save(userAchv);
            updateUserAchievementCache(userId);
        }
    }

    private boolean hasAchievement(int userId, int achvId) {
        Map<String, String> userAchvMap = getUserAchievementMap(userId);
        return userAchvMap.get(String.valueOf(achvId)) != null;
    }

    private Map<String, String> getUserAchievementMap(int userId) {
        Map<String, String> userAchievementMap = redisComponent.getUserAchievement(userId);
        if(userAchievementMap == null) {
            userAchievementMap = updateUserAchievementCache(userId);
        }
        return userAchievementMap;
    }

    private Map<String, String> updateUserAchievementCache(int userId) {
        List<UserAchievement> userAchievementDatas = userAchievementRepository.findByIdUserId(userId);
        Map<String, String> userAchievementMap = new HashMap<>();
        for (UserAchievement userAchievement : userAchievementDatas) {
            int achvId = userAchievement.getId().getAchvId();
            String date = userAchievement.getAchievedAt().toString();
            userAchievementMap.put(String.valueOf(achvId), date);
        }
        redisComponent.saveUserAchievement(userId, userAchievementMap);
        return userAchievementMap;
    }

    private boolean checkAchievementCondition(int achvId, UserStatistic userData) {
        return getAchievementProgress(achvId, userData) == 100;
    }

    private int getAchievementProgress(int achvId, UserStatistic userData) {
        double a = 0, b = 1;
        switch (achvId) {
            case 0 -> {
                a = userData.getExp();
                b = 500;
            }
            case 1 -> {
                a = userData.getExp();
                b = 1000;
            }
            case 2 -> {
                a = userData.getExp();
                b = 10000;
            }
            case 3 -> {
                a = userData.getSteps();
                b = 10000;
            }
            case 4 -> {
                a = userData.getSteps();
                b = 50000;
            }
            case 5 -> {
                a = userData.getDistance();
                b = 100000;
            }
            case 6 -> {
                a = userData.getDistance();
                b = 500000;
            }
            default -> {
            }
        }
        return (int)(a >= b ? 100 : a / b * 100);
    }
}
