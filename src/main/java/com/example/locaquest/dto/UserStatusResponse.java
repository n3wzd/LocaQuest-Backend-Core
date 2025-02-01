package com.example.locaquest.dto;

import java.util.List;

import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.model.Achievement;

public class UserStatusResponse {
    private UserStatistic userStatistic;
    private List<Achievement> achievementList;

    public void setUserStatistic(UserStatistic userStatistic) {
        this.userStatistic = userStatistic;
    }

    public void setAchievementList(List<Achievement> achievementList) {
        this.achievementList = achievementList;
    }

    public UserStatistic getUserStatistic() {
        return userStatistic;
    }

    public List<Achievement> getAchievementList() {
        return achievementList;
    }
}
