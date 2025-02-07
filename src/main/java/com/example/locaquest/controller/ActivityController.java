package com.example.locaquest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.dto.StringData;
import com.example.locaquest.service.ActivityService;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityService activityService;
    static final private Logger logger = LoggerFactory.getLogger(ActivityController.class);

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/get-login-token-key")
    public ResponseEntity<?> getLoginTokenKey(@RequestBody StringData publicKeyRequest) {
        StringData data = new StringData(activityService.getLoginTokenKey(publicKeyRequest.getData()));
        logger.info("getLoginTokenKey successful");
        return ResponseEntity.ok(data);
    }
}
