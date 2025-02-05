package com.example.locaquest.dto;

import java.util.List;

public class UserStatusResponse {
    private int level;
    private int exp;
    private int steps;
    private int distance;  
    private List<AchievementData> achievementList;

    public void setAchievementList(List<AchievementData> achievementList) {
        this.achievementList = achievementList;
    }

    public List<AchievementData> getAchievementList() {
        return achievementList;
    }

    public int getExp() {
        return exp;
    }

    public int getSteps() {
        return steps;
    }

    public int getDistance() {
        return distance;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
