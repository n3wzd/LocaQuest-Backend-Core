package com.example.locaquest.dto.activity;

import java.util.List;

import com.example.locaquest.model.Achievement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitResponse {
    private String loginTokenKey;
    private String kafkaTopicUserParamGain;
    private List<Achievement> achievementList;
}
