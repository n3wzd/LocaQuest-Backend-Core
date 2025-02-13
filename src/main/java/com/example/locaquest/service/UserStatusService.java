package com.example.locaquest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.locaquest.dto.status.UserAchievementData;
import com.example.locaquest.model.Achievement;
import com.example.locaquest.model.UserAchievement;
import com.example.locaquest.model.UserAchievementKey;
import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.repogitory.AchievementRepository;
import com.example.locaquest.repogitory.UserAchievementRepository;
import com.example.locaquest.repogitory.UserStatisticRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final UserStatisticRepository userStatisticRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private List<Achievement> achievementList;

    @PostConstruct
    public void init() {
        achievementList = this.achievementRepository.findAll();
    }

    public List<Achievement> getAchievementList() {
        return achievementList;
    }
    
    public UserStatistic getUserStatistics(int userId) {
        return userStatisticRepository.findByUserId(userId);
    }

    public List<UserAchievementData> getUserAchievements(int userId) {
    	List<UserAchievement> datas = userAchievementRepository.findByIdUserId(userId);
    	List<UserAchievementData> res = new ArrayList<>();
    	for(UserAchievement data : datas) {
    		res.add(new UserAchievementData(data.getId().getAchvId(), data.getAchievedAt().toString()));
    	}
        return res;
    }

    public void achieveAchievement(int userId, int achvId) {
    	UserAchievement userAchv = new UserAchievement();
        userAchv.setId(new UserAchievementKey(userId, achvId));
        userAchievementRepository.save(userAchv);
    }
}
