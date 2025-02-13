package com.example.locaquest.dto.status;

import java.util.List;

import com.example.locaquest.model.UserStatistic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatusResponse {
    private UserStatistic userStatistic;
    private List<UserAchievementData> achievementList;
}
