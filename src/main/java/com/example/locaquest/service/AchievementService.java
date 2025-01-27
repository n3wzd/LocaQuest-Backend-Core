package com.example.locaquest.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.locaquest.model.Achievement;
import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.model.UserAchievement;
import com.example.locaquest.model.UserAchievementKey;
import com.example.locaquest.repogitory.AchievementRepository;
import com.example.locaquest.repogitory.UserAchievementRepository;

@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final RedisService redisService;
    private final List<Achievement> achievementList;

    public AchievementService(AchievementRepository achievementRepository, UserAchievementRepository userAchievementRepository, RedisService redisService) {
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.redisService = redisService;
        achievementList = this.achievementRepository.findAll();
    }

    public List<Achievement> getUserAchievementList(int userId) {
        Set<Integer> userAchievementSet = getUserAchievement(userId);
        List<Achievement> userAchiList = new ArrayList<>();
        for(Achievement achv : achievementList) {
            if(userAchievementSet.contains(achv.getAchvId())) {
                userAchiList.add(achv);
            }
        }
        return userAchiList;
    }

    public void updateUserAchievementByUserStatistic(UserStatistic userData) {
        int userId = userData.getUserId();
        int exp = userData.getTotalExperience();
        int steps = userData.getTotalSteps();
        int dist = userData.getTotalDistance();

        int[][] thresholdsExp = {
            {500, 0},
            {1000, 1},
            {10000, 2}
        };
        int[][] thresholdsSteps = {
            {10000, 3},
            {50000, 4}
        };
        int[][] thresholdsDist = {
            {100000, 5},
            {500000, 6}
        };

        for (int[] threshold : thresholdsExp) {
            if (exp >= threshold[0]) {
                achieveAchievement(userId, threshold[1]);
            }
        }
        for (int[] threshold : thresholdsSteps) {
            if (steps >= threshold[0]) {
                achieveAchievement(userId, threshold[1]);
            }
        }
        for (int[] threshold : thresholdsDist) {
            if (dist >= threshold[0]) {
                achieveAchievement(userId, threshold[1]);
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
        Set<Integer> userAchvSet = getUserAchievement(userId);
        return userAchvSet.contains(achvId);
    }

    private Set<Integer> getUserAchievement(int userId) {
        Set<Integer> userAchievementSet = redisService.getUserAchievement(userId);
        if(userAchievementSet == null) {
            userAchievementSet = updateUserAchievementCache(userId);
        }
        return userAchievementSet;
    }

    private Set<Integer> updateUserAchievementCache(int userId) {
        List<UserAchievement> userAchievementDatas = userAchievementRepository.findByIdUserId(userId);
        Set<Integer> userAchievementSet = new HashSet<>();
        for (UserAchievement userAchievement : userAchievementDatas) {
            userAchievementSet.add(userAchievement.getId().getAchvId());
        }
        redisService.saveUserAchievement(userId, userAchievementSet);
        return userAchievementSet;
    }
}
