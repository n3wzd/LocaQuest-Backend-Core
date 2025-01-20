package com.example.reviewsplash.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.reviewsplash.component.remote.MapApiRemoteControl;
import com.example.reviewsplash.dto.Location;
import com.example.reviewsplash.exception.ServiceException;
import com.example.reviewsplash.model.User;
import com.example.reviewsplash.model.Place;
import com.example.reviewsplash.repogitory.UserRepository;
import com.example.reviewsplash.repogitory.PlaceRepository;

@Service
public class PlaceService {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final MapApiRemoteControl mapApiRemoteControl;

    private final int thresholdDistance = 5000;  // 5km

    public PlaceService(UserRepository userRepository, PlaceRepository placeRepository, MapApiRemoteControl mapApiRemoteControl) {
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.mapApiRemoteControl = mapApiRemoteControl;
    }

    public List<Place> search(Location location, String query) {
        String result = mapApiRemoteControl.searchNearbyPlaces(location.getLatitude(), location.getLongitude(), thresholdDistance);
    }
}
