package com.example.locaquest.dto.status;

import com.example.locaquest.model.UserStatistic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatisticData {
    private int userId;
    private String statDate;
    private int exp = 0;
    private int steps = 0;
    private int distance = 0;
    
    public UserStatisticData(UserStatistic stat) {
    	this.userId = stat.getId().getUserId();
    	this.statDate = stat.getId().getStatDate();
    	this.exp = stat.getExp();
    	this.distance = stat.getDistance();
    	this.steps = stat.getSteps();
    }
}
