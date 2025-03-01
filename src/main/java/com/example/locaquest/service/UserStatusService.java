package com.example.locaquest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.locaquest.dto.status.UserAchievementData;
import com.example.locaquest.dto.status.UserParamGain;
import com.example.locaquest.dto.status.UserStatisticData;
import com.example.locaquest.model.Achievement;
import com.example.locaquest.model.UserAchievement;
import com.example.locaquest.model.UserAchievementKey;
import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.model.UserStatisticKey;
import com.example.locaquest.repogitory.AchievementRepository;
import com.example.locaquest.repogitory.UserAchievementRepository;
import com.example.locaquest.repogitory.UserStatisticRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
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
    
    public List<UserStatisticData> getUserStatistics(int userId) {
    	List<UserStatistic> datas = userStatisticRepository.findByIdUserId(userId);
    	List<UserStatisticData> res = new ArrayList<>();
    	for(UserStatistic data : datas) {
    		res.add(new UserStatisticData(data));
    	}
        return res;
    }

    public List<UserAchievementData> getUserAchievements(int userId) {
    	List<UserAchievement> datas = userAchievementRepository.findByIdUserId(userId);
    	List<UserAchievementData> res = new ArrayList<>();
    	for(UserAchievement data : datas) {
    		res.add(new UserAchievementData(data.getId().getAchvId(), data.getAchievedAt().toString()));
    	}
        return res;
    }
    
    public boolean updateAttend(int userId, String statDate) {
    	if(!userStatisticRepository.existsById(new UserStatisticKey(userId, statDate))) {
    		userStatisticRepository.save(new UserStatistic(userId, statDate));
    		return true;
    	}
    	return false;
    }

    public void achieveAchievement(int userId, int achvId, String achievedAt) {
    	UserAchievement userAchv = new UserAchievement();
        userAchv.setId(new UserAchievementKey(userId, achvId));
        userAchv.setAchievedAt(achievedAt);
        userAchievementRepository.save(userAchv);
    }
    
    @Transactional
    public boolean gainParam(UserParamGain paramGain) {
        int userId = paramGain.getUserId();
        String date = paramGain.getDate();
        int exp = paramGain.getExp();
        int steps = paramGain.getSteps();
        int distance = paramGain.getDistance();
        updateAttend(userId, date);
        return userStatisticRepository.gainParam(userId, date, exp, steps, distance) == 1;
    }
}
