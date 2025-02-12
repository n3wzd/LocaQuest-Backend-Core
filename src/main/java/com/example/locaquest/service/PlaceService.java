package com.example.locaquest.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.locaquest.dto.place.MapRouteRequest;
import com.example.locaquest.dto.place.Place;
import com.example.locaquest.dto.place.PlaceRequest;
import com.example.locaquest.dto.constant.MapRouteProfile;
import com.example.locaquest.dto.constant.PlaceSortCriteria;
import com.example.locaquest.component.MapComponent;

@Service
public class PlaceService {
    private final MapComponent mapComponent;

    public PlaceService(MapComponent mapComponent) {
        this.mapComponent = mapComponent;
    }

    public List<Place> search(PlaceRequest placeRequest) {
        double latitude = placeRequest.getLatitude();
        double longitude = placeRequest.getLongitude();
        String query = placeRequest.getQuery();
        int radius = placeRequest.getRadius();
        List<Place> places = mapComponent.searchNearbyPlaces(latitude, longitude, radius, query);
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
        return mapComponent.route(depLat, depLng, destLat, destLng, profile);
    }
}
