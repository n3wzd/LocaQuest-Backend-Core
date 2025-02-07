package com.example.locaquest.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatusResponse {
    private int level;
    private int exp;
    private int steps;
    private int distance;  
    private List<AchievementData> achievementList;

    public void setAchievementList(List<AchievementData> achievementList) {
        this.achievementList = achievementList;
    }
}
