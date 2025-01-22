package com.example.reviewsplash.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reviewsplash.dto.MapRouteRequest;
import com.example.reviewsplash.dto.PlaceRequest;
import com.example.reviewsplash.dto.Place;
import com.example.reviewsplash.exception.ServiceException;
import com.example.reviewsplash.service.PlaceService;
import com.example.reviewsplash.service.UserService;

@RestController
@RequestMapping("/api/places")
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
        try {
            List<Place> result = placeService.search(placeRequest);
            logger.info("searchPlaces successful: userId={}", userService.getCurrentUserId());
            return ResponseEntity.ok(result);
        } catch (ServiceException e) {
            logger.warn("searchPlaces failed: userId={}, {}", userService.getCurrentUserId(), e.toString());
            return ResponseEntity.badRequest().body("searchPlaces failed");
        }
    }

    @PostMapping("/route")
    public ResponseEntity<?> routeDestination(@RequestBody MapRouteRequest mapRouteRequest) {
        try {
            String result = placeService.route(mapRouteRequest);
            logger.info("routeDestination successful: userId={}", userService.getCurrentUserId());
            return ResponseEntity.ok(result);
        } catch (ServiceException e) {
            logger.warn("routeDestination failed: userId={}, {}", userService.getCurrentUserId(), e.toString());
            return ResponseEntity.badRequest().body("routeDestination failed");
        }
    }
}
