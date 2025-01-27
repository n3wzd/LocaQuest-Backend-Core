package com.example.locaquest.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_achievements")
public class UserAchievement {

    @EmbeddedId
    private UserAchievementKey id;

    @Column(name = "achieved_at", nullable = false, updatable = false)
    private LocalDateTime achievedAt;

    @PrePersist
    protected void onCreate() {
        this.achievedAt = LocalDateTime.now();
    }

    public void setId(UserAchievementKey id) {
        this.id = id;
    }

    public UserAchievementKey getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return achievedAt;
    }
}
