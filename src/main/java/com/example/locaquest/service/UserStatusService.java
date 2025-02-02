package com.example.locaquest.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.locaquest.dto.AchievementData;
import com.example.locaquest.model.Achievement;
import com.example.locaquest.model.UserAchievement;
import com.example.locaquest.model.UserAchievementKey;
import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.repogitory.AchievementRepository;
import com.example.locaquest.repogitory.UserAchievementRepository;
import com.example.locaquest.repogitory.UserStatisticRepository;

@Service
public class UserStatusService {
    private final UserStatisticRepository userStatisticRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final RedisService redisService;
    private final List<Achievement> achievementList;

    public UserStatusService(UserStatisticRepository userStatisticRepository, AchievementRepository achievementRepository, UserAchievementRepository userAchievementRepository, RedisService redisService) {
        this.userStatisticRepository = userStatisticRepository;
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.redisService = redisService;
        achievementList = this.achievementRepository.findAll();
    }

    public UserStatistic getUserStatistics(int userId) {
        return userStatisticRepository.findByUserId(userId);
    }

    public List<AchievementData> getAllUserAchievements(int userId) {
        List<AchievementData> userAchiList = new ArrayList<>();
        UserStatistic userData = getUserStatistics(userId);
        for(Achievement achv : achievementList) {
            int progress = getAchievementProgress(achv.getAchvId(), userData);
            userAchiList.add(new AchievementData(achv.getAchvId(), achv.getName(), achv.getDesc(), progress));
        }
        return userAchiList;
    }

    public List<AchievementData> getAchievedUserAchievements(int userId) {
        Set<Integer> userAchievementSet = getUserAchievementSet(userId);
        List<AchievementData> userAchiList = new ArrayList<>();
        for(Achievement achv : achievementList) {
            if(userAchievementSet.contains(achv.getAchvId())) {
                userAchiList.add(new AchievementData(achv.getAchvId(), achv.getName(), achv.getDesc(), 100));
            }
        }
        return userAchiList;
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
        Set<Integer> userAchvSet = getUserAchievementSet(userId);
        return userAchvSet.contains(achvId);
    }

    private Set<Integer> getUserAchievementSet(int userId) {
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

    private boolean checkAchievementCondition(int achvId, UserStatistic userData) {
        return getAchievementProgress(achvId, userData) == 100;
    }

    private int getAchievementProgress(int achvId, UserStatistic userData) {
        double a = 0, b = 1;
        switch (achvId) {
            case 0 -> {
                a = userData.getTotalExperience();
                b = 500;
            }
            case 1 -> {
                a = userData.getTotalExperience();
                b = 1000;
            }
            case 2 -> {
                a = userData.getTotalExperience();
                b = 10000;
            }
            case 3 -> {
                a = userData.getTotalSteps();
                b = 10000;
            }
            case 4 -> {
                a = userData.getTotalSteps();
                b = 50000;
            }
            case 5 -> {
                a = userData.getTotalDistance();
                b = 100000;
            }
            case 6 -> {
                a = userData.getTotalDistance();
                b = 500000;
            }
            default -> {
            }
        }
        return (int)(a >= b ? 100 : a / b * 100);
    }
}
