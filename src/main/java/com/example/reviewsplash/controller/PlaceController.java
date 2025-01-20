package com.example.reviewsplash.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reviewsplash.exception.ServiceException;
import com.example.reviewsplash.dto.PlaceRequest;
import com.example.reviewsplash.service.UserService;
import com.example.reviewsplash.service.PlaceService;

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
            placeService.search(placeRequest.getLocation(), placeRequest.getQuery());
            logger.info("searchPlaces successful: userId={}", userService.getCurrentUserId());
            return ResponseEntity.ok("User Profile updated successfully.");
        } catch (ServiceException e) {
            logger.warn("searchPlaces failed: userId={}, {}", userService.getCurrentUserId(), e.toString());
            return ResponseEntity.badRequest().body("searchPlaces failed");
        }
    }
}
