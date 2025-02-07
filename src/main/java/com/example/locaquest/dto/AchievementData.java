package com.example.locaquest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchievementData {
    private int achvId;
    private String name;
    private String desc;
    private int progress;
    private String achievedAt;

    public AchievementData(int achvId, String name, String desc, int progress, String achievedAt) {
        this.achvId = achvId;
        this.name = name;
        this.desc = desc;
        this.progress = progress;
        this.achievedAt = achievedAt;
    }
}
