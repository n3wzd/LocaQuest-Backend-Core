package com.example.locaquest.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;

import com.example.locaquest.dto.status.UserParamGain;
import com.example.locaquest.service.UserStatusService;
import com.example.locaquest.util.LogUtil;

@Service
@RequiredArgsConstructor
public class GainUserParam {
    private final UserStatusService userStatusService;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${kafka.topic.user-param-gain}", groupId = "${kafka.topic.user-param-gain}")
    @Transactional
    public void consumeGainUserParam(String message, Acknowledgment acknowledgment) {
        try {
            UserParamGain paramGain = objectMapper.readValue(message, UserParamGain.class);
            if(userStatusService.gainParam(paramGain)) {
            	LogUtil.info(String.format("successfully: userId=%d, date=%s, exp=%d, steps=%d, distance=%d", 
            			paramGain.getUserId(), paramGain.getDate(), paramGain.getExp(), paramGain.getSteps(), paramGain.getDistance()),
            			"service.kafka.GainUserParam", "consumeGainUserParam");
            } else {
            	LogUtil.warn(String.format("DB failed. unknown userId: %s", paramGain.getUserId()), "service.kafka.GainUserParam", "consumeGainUserParam");
            }
        } catch(JsonProcessingException e) {
        	LogUtil.warn(String.format("GainUserParam json parsing failed: %s", e.toString()), "service.kafka.GainUserParam", "consumeGainUserParam");
        } finally {
            acknowledgment.acknowledge();
        }
    }
}
