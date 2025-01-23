package com.example.locaquest.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.locaquest.dto.Place;
import com.example.locaquest.dto.PlaceRequest;
import com.example.locaquest.dto.MapRouteRequest;
import com.example.locaquest.dto.constant.PlaceSortCriteria;
import com.example.locaquest.dto.constant.MapRouteProfile;
import com.example.locaquest.service.remote.MapApiRemoteControl;

@Service
public class PlaceService {
    private final MapApiRemoteControl mapApiRemoteControl;

    public PlaceService(MapApiRemoteControl mapApiRemoteControl) {
        this.mapApiRemoteControl = mapApiRemoteControl;
    }

    public List<Place> search(PlaceRequest placeRequest) {
        double latitude = placeRequest.getLatitude();
        double longitude = placeRequest.getLongitude();
        String query = placeRequest.getQuery();
        int radius = placeRequest.getRadius();
        List<Place> places = mapApiRemoteControl.searchNearbyPlaces(latitude, longitude, radius, query);
        places = sortPlaces(places, placeRequest.getSortCriteria());
        return places;
    }

    private List<Place> sortPlaces(List<Place> places, PlaceSortCriteria criteria) {
        switch (criteria) {
            case DISTANCE -> places.sort(Comparator.comparingDouble(Place::getDistanceFromOrigin));
            case REVIEW_COUNT -> places.sort(Comparator.comparingInt(Place::getReviewCount).reversed());
            case RATING -> places.sort(Comparator.comparingDouble(Place::getRating));
            case CUSTOM -> {}
        }
        return places;
    }

    public String route(MapRouteRequest mapRouteRequest) {
        double depLat = mapRouteRequest.getDepLatitude();
        double depLng = mapRouteRequest.getDepLongitude();
        double destLat = mapRouteRequest.getDestLatitude();
        double destLng = mapRouteRequest.getDestLongitude();
        MapRouteProfile profile = mapRouteRequest.getMapRouteProfile();
        return mapApiRemoteControl.route(depLat, depLng, destLat, destLng, profile);
    }
}
