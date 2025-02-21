package com.example.locaquest.dto.status;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatusStartResponse {
    private List<UserStatisticData> userStatisticList;
    private List<UserAchievementData> userAchievementList;
    private boolean isAttend;
}
