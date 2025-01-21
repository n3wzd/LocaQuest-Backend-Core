package com.example.reviewsplash.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.reviewsplash.dto.Location;
import com.example.reviewsplash.dto.Place;
import com.example.reviewsplash.service.remote.MapApiRemoteControl;

@Service
public class PlaceService {

    private final MapApiRemoteControl mapApiRemoteControl;

    private final int thresholdDistance = 2000;  // 2km
    static final private Logger logger = LoggerFactory.getLogger(PlaceService.class);

    public PlaceService(MapApiRemoteControl mapApiRemoteControl) {
        this.mapApiRemoteControl = mapApiRemoteControl;
    }

    public List<Place> search(Location location, String query) {
        return mapApiRemoteControl.searchNearbyPlaces(location.getLatitude(), location.getLongitude(), thresholdDistance, query);
    }
}
