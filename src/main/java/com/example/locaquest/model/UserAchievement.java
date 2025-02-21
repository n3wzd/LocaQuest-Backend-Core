package com.example.locaquest.model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_achievements")
public class UserAchievement {

    @EmbeddedId
    private UserAchievementKey id;

    @Column(name = "achieved_at", nullable = false, updatable = false)
    private ZonedDateTime achievedAt;

    public void setId(UserAchievementKey id) {
        this.id = id;
    }

    public UserAchievementKey getId() {
        return id;
    }
    
    public void setAchievedAt(ZonedDateTime achievedAt) {
        this.achievedAt = achievedAt;
    }
    
    public void setAchievedAt(String achievedAt) {
        this.achievedAt = ZonedDateTime.parse(achievedAt, DateTimeFormatter.ISO_DATE_TIME);
    }

    public ZonedDateTime getAchievedAt() {
        return achievedAt;
    }
}
