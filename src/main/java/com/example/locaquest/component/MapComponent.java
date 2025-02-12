package com.example.locaquest.component;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.locaquest.dto.place.Place;
import com.example.locaquest.dto.constant.MapRouteProfile;
import com.example.locaquest.external.GooglePlaceAPI;
import com.example.locaquest.external.OpenStreetMapAPI;

@Component
public class MapComponent {

    private final boolean useGoogleAPI = false;
    private final GooglePlaceAPI googlePlaceApi;
    private final OpenStreetMapAPI openStreetMapAPI;

    public MapComponent(GooglePlaceAPI googlePlaceApi, OpenStreetMapAPI openStreetMapAPI) {
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
