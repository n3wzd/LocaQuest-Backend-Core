package com.example.locaquest.service.kafka;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;

import com.example.locaquest.dto.status.UserParamGain;
import com.example.locaquest.repogitory.UserStatisticRepository;
import com.example.locaquest.service.UserStatusService;
import com.example.locaquest.util.LogUtil;

@Service
@RequiredArgsConstructor
public class GainUserParam {
    private final UserStatisticRepository userStatisticRepository;
    private final UserStatusService userStatusService;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${kafka.topic.user-param-gain}", groupId = "${kafka.topic.user-param-gain}")
    @Transactional
    public void consumeGainUserParam(String message, Acknowledgment acknowledgment) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            UserParamGain paramGain = objectMapper.readValue(message, UserParamGain.class);
            int userId = paramGain.getUserId();
            String dateStr = paramGain.getDate();
            int exp = paramGain.getExp();
            int steps = paramGain.getSteps();
            int distance = paramGain.getDistance();
            LocalDate date = LocalDate.parse(dateStr, formatter);
            userStatusService.updateAttend(userId, dateStr);
            if(userStatisticRepository.gainParam(userId, date, exp, steps, distance) == 1) {
            	LogUtil.info(String.format("successfully: userId=%s, date=%s, exp=%d, steps=%d, distance=%d", userId, dateStr, exp, steps, distance), "service.kafka.GainUserParam", "consumeGainUserParam");
            } else {
            	LogUtil.warn(String.format("DB failed. unknown userId: %s", userId), "service.kafka.GainUserParam", "consumeGainUserParam");
            }
        } catch(JsonProcessingException e) {
        	LogUtil.warn(String.format("GainUserParam json parsing failed: %s", e.toString()), "service.kafka.GainUserParam", "consumeGainUserParam");
        } finally {
            acknowledgment.acknowledge();
        }
    }
}
