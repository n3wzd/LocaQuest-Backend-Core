package com.example.locaquest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.constant.Route;
import com.example.locaquest.dto.client.InitResponse;
import com.example.locaquest.service.ClientService;
import com.example.locaquest.util.LogUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Route.CLIENT)
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final String filePath = "controller.ClientController";

    @Value("${kafka.topic.user-param-gain}")
    private String KAFKA_TOPIC_USER_PARAM_GAIN;

    @GetMapping(Route.CLIENT_INIT)
    public ResponseEntity<?> init(HttpServletRequest request) {
        InitResponse response = new InitResponse();
        System.out.println("1");
        response.setRsaPublicKey(clientService.getRSAPublicKey());
        System.out.println(clientService.getRSAPublicKey());
        LogUtil.info(String.format("successfully"), filePath, Route.CLIENT_INIT, request);
        return ResponseEntity.ok(response);
    }
}
