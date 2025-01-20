package com.example.reviewsplash.component.remote;

import org.springframework.stereotype.Component;

import com.example.reviewsplash.component.api.GooglePlaceAPI;
import com.example.reviewsplash.component.api.OpenStreetMapAPI;

@Component
public class MapApiRemoteControl {

    private final GooglePlaceAPI googlePlaceApi;
    private final OpenStreetMapAPI openStreetMapAPI;

    public MapApiRemoteControl(GooglePlaceAPI googlePlaceApi, OpenStreetMapAPI openStreetMapAPI) {
        this.googlePlaceApi = googlePlaceApi;
        this.openStreetMapAPI = openStreetMapAPI;
    }

    public String searchNearbyPlaces(double latitude, double longitude, int radius) {
        return openStreetMapAPI.searchNearbyPlaces(latitude, longitude, radius);
    }
}
