package com.example.locaquest.dto.status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatusStartRequest {
    private int userId;
    private String date;
}
