package com.example.locaquest.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.dto.MapRouteRequest;
import com.example.locaquest.dto.PlaceRequest;
import com.example.locaquest.dto.Place;
import com.example.locaquest.service.PlaceService;
import com.example.locaquest.service.UserService;

@RestController
@RequestMapping("/places")
public class PlaceController {

    private final UserService userService;
    private final PlaceService placeService;
    static final private Logger logger = LoggerFactory.getLogger(UserController.class);

    public PlaceController(UserService userService, PlaceService placeService) {
        this.userService = userService;
        this.placeService = placeService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchPlaces(@RequestBody PlaceRequest placeRequest) {
        List<Place> result = placeService.search(placeRequest);
        logger.info("searchPlaces successful: userId={}", userService.getCurrentUserId());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/route")
    public ResponseEntity<?> routeDestination(@RequestBody MapRouteRequest mapRouteRequest) {
        String result = placeService.route(mapRouteRequest);
        logger.info("routeDestination successful: userId={}", userService.getCurrentUserId());
        return ResponseEntity.ok(result);
    }
}
