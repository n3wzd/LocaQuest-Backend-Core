package com.example.locaquest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_statistics")
public class UserStatistic {

    @Id
    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    private int userId;

    @Column(name = "total_experience", nullable = false)
    private int totalExperience;

    @Column(name = "total_steps", nullable = false)
    private int totalSteps;

    @Column(name = "total_distance", nullable = false)
    private int totalDistance;

    public int getUserId() {
        return userId;
    }

    public int getTotalExperience() {
        return totalExperience;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTotalExperience(int totalExperience) {
        this.totalExperience = totalExperience;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }
}
