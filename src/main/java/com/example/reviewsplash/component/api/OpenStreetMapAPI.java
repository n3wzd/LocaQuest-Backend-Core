package com.example.reviewsplash.component.api;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OpenStreetMapAPI {

    private static final String OSM_API_BASE_URL = "https://overpass-api.de/api/interpreter";
    private final RestTemplate restTemplate;

    public OpenStreetMapAPI(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String searchNearbyPlaces(double latitude, double longitude, int radius) {
        String query = buildQuery(latitude, longitude, radius);
        return callApi(query);
    }

    private String callApi(String query) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(OSM_API_BASE_URL)  // Deprecated
                .queryParam("data", query);
        return restTemplate.getForObject(uriBuilder.toUriString(), String.class);
    }

    private String buildQuery(double latitude, double longitude, int radius) {
        return String.format(
                "[out:json];(node(around:%d,%f,%f););out body;",
                radius, latitude, longitude
        );
    }
}
