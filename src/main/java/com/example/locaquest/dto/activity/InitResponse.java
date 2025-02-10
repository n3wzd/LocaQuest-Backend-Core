package com.example.locaquest.dto.activity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitResponse {
    private String loginTokenKey;
    private String kafkaTopicUserParamGain;
}
