package com.example.reviewsplash.service.remote;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.reviewsplash.dto.Place;
import com.example.reviewsplash.service.api.GooglePlaceAPI;
import com.example.reviewsplash.service.api.OpenStreetMapAPI;

@Service
public class MapApiRemoteControl {

    private final boolean useGoogleAPI = false;
    private final GooglePlaceAPI googlePlaceApi;
    private final OpenStreetMapAPI openStreetMapAPI;

    public MapApiRemoteControl(GooglePlaceAPI googlePlaceApi, OpenStreetMapAPI openStreetMapAPI) {
        this.googlePlaceApi = googlePlaceApi;
        this.openStreetMapAPI = openStreetMapAPI;
    }

    public List<Place> searchNearbyPlaces(double latitude, double longitude, int radius, String type) {
        if(useGoogleAPI) {
            return googlePlaceApi.searchNearbyPlaces(latitude, longitude, radius, type);
        } else {
            return openStreetMapAPI.searchNearbyPlaces(latitude, longitude, radius, type);
        }
    }
}
