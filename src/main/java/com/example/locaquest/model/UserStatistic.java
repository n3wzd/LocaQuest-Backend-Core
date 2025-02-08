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

    @Column(name = "exp", nullable = false)
    private int exp = 0;

    @Column(name = "steps", nullable = false)
    private int steps = 0;

    @Column(name = "distance", nullable = false)
    private int distance = 0;

    public UserStatistic() {
        
    }

    public UserStatistic(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
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

    public void setUserId(int userId) {
        this.userId = userId;
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
}
