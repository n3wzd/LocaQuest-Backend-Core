package com.example.locaquest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.example.locaquest.dto.status.UserParamGain;
import com.example.locaquest.repogitory.UserStatisticRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final UserStatisticRepository userStatisticRepository;
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @KafkaListener(topics = "${kafka.topic.user-param-gain}", groupId = "${kafka.topic.user-param-gain}")
    @Transactional
    public void consumeGainUserParam(String message, Acknowledgment acknowledgment) {
        try {
            UserParamGain expGain = objectMapper.readValue(message, UserParamGain.class);
            int userId = expGain.getUserId();
            int exp = expGain.getExp();
            int steps = expGain.getSteps();
            int distance = expGain.getDistance();
            if(userStatisticRepository.gainParam(userId, exp, steps, distance) == 1) {
                logger.info("GainUserParam successful: userId={}, exp={}, steps={}, distance={}", userId, exp, steps, distance);
            } else {
                logger.warn("GainUserParam DB failed. unknown userId: {}", userId);
            }
        } catch(JsonProcessingException e) {
            logger.warn("GainUserParam json parsing failed: {}", e.toString());
        } finally {
            acknowledgment.acknowledge();
        }
    }
}
