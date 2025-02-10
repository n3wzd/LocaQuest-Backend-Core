package com.example.locaquest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.dto.activity.InitRequest;
import com.example.locaquest.dto.activity.InitResponse;
import com.example.locaquest.service.ActivityService;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityService activityService;
    static final private Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Value("${kafka.topic.user-param-gain}")
    private String KAFKA_TOPIC_USER_PARAM_GAIN;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/init")
    public ResponseEntity<?> init(@RequestBody InitRequest initRequest) {
        InitResponse response = new InitResponse();
        response.setLoginTokenKey(activityService.getLoginTokenKey(initRequest.getRsaPublicKey()));
        response.setKafkaTopicUserParamGain(KAFKA_TOPIC_USER_PARAM_GAIN);
        logger.info("init successful");
        return ResponseEntity.ok(response);
    }
}
