package com.example.locaquest.dto;

public class AchievementData {
    private int achvId;
    private String name;
    private String desc;
    private int progress;

    public AchievementData(int achvId, String name, String desc, int progress) {
        this.achvId = achvId;
        this.name = name;
        this.desc = desc;
        this.progress = progress;
    }

    public int getAchvId() {
        return achvId;
    }

    public void setAchvId(int achvId) {
        this.achvId = achvId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
