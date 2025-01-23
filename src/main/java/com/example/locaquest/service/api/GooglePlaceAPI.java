package com.example.locaquest.service.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.locaquest.dto.GooglePlaceSearchResult;
import com.example.locaquest.dto.Place;
import com.example.locaquest.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GooglePlaceAPI {
    private static final String API_BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    private final RestClient restClient;
    final private ObjectMapper objectMapper = new ObjectMapper();
    static final private Logger logger = LoggerFactory.getLogger(GooglePlaceAPI.class);

    @Value("${google.key}")
    private String apiKey;

    public GooglePlaceAPI(RestClient restClient) {
        this.restClient = restClient;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Place> searchNearbyPlaces(double latitude, double longitude, int radius, String type) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(API_BASE_URL)
                .queryParam("location", latitude + "," + longitude)
                .queryParam("radius", radius)
                .queryParam("type", type)
                .queryParam("key", apiKey);
        String response = restClient.get()
                .uri(uriBuilder.toUriString())
                .retrieve()
                .body(String.class);
        // logger.info("Google Place Search API called: {}", response);
        
        try {
            GooglePlaceSearchResult datas = objectMapper.readValue(response, GooglePlaceSearchResult.class);
            if(!"OK".equals(datas.getStatus())) {
                if("ZERO_RESULTS".equals(datas.getStatus())) {
                    return new ArrayList<>();
                } else {
                    throw new ServiceException(datas.getStatus());
                }
            }
            List<Place> placeResult = new ArrayList<>();
            for(GooglePlaceSearchResult.Result data : datas.getResults()) {
                placeResult.add(createPlace(data));
            }
            return placeResult;
        } catch(JsonProcessingException e) {
            throw new ServiceException(String.format("JSON Failed: %s", e.toString()));
        } catch(ServiceException e) {
            throw new ServiceException(String.format("API Failed: %s", e.toString()));
        }
    }

    private Place createPlace(GooglePlaceSearchResult.Result data) {
        Place place = new Place();
        place.setName(data.getName());
        place.setLatitude(data.getGeometry().getLocation().getLat());
        place.setLongitude(data.getGeometry().getLocation().getLng());
        place.setRating(data.getRating());
        place.setReviewCount(data.getUserRatingsTotal());
        place.setTypes(data.getTypes());
        return place;
    }
}
