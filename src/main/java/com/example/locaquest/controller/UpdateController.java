package com.example.locaquest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.dto.status.UpdateData;
import com.example.locaquest.service.UserStatusService;

@RestController
@RequestMapping("/update")
public class UpdateController {

    private final UserStatusService userStatusService;
    static final private Logger logger = LoggerFactory.getLogger(UpdateController.class);

    public UpdateController(UserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }

    @PostMapping("/")
    public ResponseEntity<?> update() {
        UpdateData data = new UpdateData();
        // data.setMaxLevel(0);
        // data.setExpLimitList(new ArrayList<>());
        logger.info("update successful");
        return ResponseEntity.ok(data);
    }
}
