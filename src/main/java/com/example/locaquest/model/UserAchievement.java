package com.example.locaquest.model;

import java.time.LocalDateTime;
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
    private LocalDateTime achievedAt;

    public void setId(UserAchievementKey id) {
        this.id = id;
    }

    public UserAchievementKey getId() {
        return id;
    }
    
    public void setAchievedAt(String achievedAt) {
    	DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        this.achievedAt = LocalDateTime.parse(achievedAt, formatter);
    }

    public LocalDateTime getAchievedAt() {
        return achievedAt;
    }
}
