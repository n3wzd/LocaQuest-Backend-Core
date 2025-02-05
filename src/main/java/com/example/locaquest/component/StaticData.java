package com.example.locaquest.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StaticData {

    @Value("${static.level.maxLevel}")
    private int maxLevel;

    @Value("${static.level.expParamA}")
    private int expParamA;

    @Value("${static.level.expParamB}")
    private int expParamB;

    @Value("${static.level.expParamC}")
    private int expParamC;

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getExpParamA() {
        return expParamA;
    }

    public int getExpParamB() {
        return expParamB;
    }

    public int getExpParamC() {
        return expParamC;
    }
}
