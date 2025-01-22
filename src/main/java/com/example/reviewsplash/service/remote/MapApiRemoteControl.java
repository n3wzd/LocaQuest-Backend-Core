package com.example.reviewsplash.service.remote;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.reviewsplash.dto.Place;
import com.example.reviewsplash.dto.constant.MapRouteProfile;
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
        List<Place> places = useGoogleAPI ? 
            googlePlaceApi.searchNearbyPlaces(latitude, longitude, radius, type) :
            openStreetMapAPI.searchNearbyPlaces(latitude, longitude, radius, type);
        for(Place place : places) {
            place.setDistanceFromOrigin(latitude, longitude);
        }
        return places;
    }

    public String route(double depLat, double depLng, double destLat, double destLng, MapRouteProfile profile) {
        return openStreetMapAPI.route(depLat, depLng, destLat, destLng, profile);
    }
}
