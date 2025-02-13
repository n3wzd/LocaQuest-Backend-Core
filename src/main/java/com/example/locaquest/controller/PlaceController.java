package com.example.locaquest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.constant.Route;
import com.example.locaquest.dto.place.MapRouteRequest;
import com.example.locaquest.dto.place.PlaceRequest;
import com.example.locaquest.dto.place.Place;
import com.example.locaquest.service.PlaceService;
import com.example.locaquest.service.TokenService;
import com.example.locaquest.util.LogUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Route.PLACE)
@RequiredArgsConstructor
public class PlaceController {

    private final TokenService tokenService;
    private final PlaceService placeService;
    private final String filePath = "controller.PlaceController";

    @PostMapping(Route.PLACE_SEARCH)
    public ResponseEntity<?> searchPlaces(@RequestBody PlaceRequest placeRequest, HttpServletRequest request) {
        List<Place> result = placeService.search(placeRequest);
        LogUtil.info(String.format("successfully: userId=%s", tokenService.getUserId()), filePath, Route.PLACE_SEARCH, request);
        return ResponseEntity.ok(result);
    }

    @PostMapping(Route.PLACE_ROUTE)
    public ResponseEntity<?> routeDestination(@RequestBody MapRouteRequest mapRouteRequest, HttpServletRequest request) {
        String result = placeService.route(mapRouteRequest);
        LogUtil.info(String.format("successfully: userId=%s", tokenService.getUserId()), filePath, Route.PLACE_ROUTE, request);
        return ResponseEntity.ok(result);
    }
}
