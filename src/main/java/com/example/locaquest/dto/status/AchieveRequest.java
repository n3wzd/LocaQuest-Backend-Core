package com.example.locaquest.dto.status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchieveRequest {
    private int achvId;
    private int userId;
    private String achievedAt;
}
