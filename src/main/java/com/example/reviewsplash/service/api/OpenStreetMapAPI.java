package com.example.reviewsplash.service.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.reviewsplash.dto.OSMResult;
import com.example.reviewsplash.dto.Place;
import com.example.reviewsplash.dto.constant.MapRouteProfile;
import com.example.reviewsplash.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenStreetMapAPI {

    private static final String OVERPASS_BASE_URL = "https://overpass-api.de/api/interpreter";
    private static final String ROUTER_BASE_URL = "http://router.project-osrm.org/route/v1";
    private final RestClient restClient;
    final private ObjectMapper objectMapper = new ObjectMapper();
    static final private Logger logger = LoggerFactory.getLogger(GooglePlaceAPI.class);

    public OpenStreetMapAPI(RestClient restClient) {
        this.restClient = restClient;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Place> searchNearbyPlaces(double latitude, double longitude, int radius, String userQuery) {
        List<String> amenityList = createAmenity(userQuery);
        String query = buildSearchQuery(latitude, longitude, radius, amenityList, userQuery);
        String response = restClient.get()
                .uri(OVERPASS_BASE_URL + "?data=" + query)
                .retrieve()
                .body(String.class);

        try {
            OSMResult datas = objectMapper.readValue(response, OSMResult.class);
            List<Place> placeResult = new ArrayList<>();
            for(OSMResult.Elements data : datas.getElements()) {
                if(data.getTags() != null) {
                    if (data.getTags().getName() != null) {
                        placeResult.add(createPlace(data));
                    }
                }
            }
            return placeResult;
        } catch(JsonProcessingException e) {
            throw new ServiceException(String.format("JSON Failed: %s", e.toString()));
        } catch(ServiceException e) {
            throw new ServiceException(String.format("API Failed: %s", e.toString()));
        }
    }

    private String buildSearchQuery(double latitude, double longitude, int radius, List<String> amenityList, String name) {
        StringBuilder queryBuilder = new StringBuilder("[out:json];(");
        for (String amenity : amenityList) {
            queryBuilder.append(String.format("node[\"amenity\"=\"%s\"](around:%d,%f,%f);", amenity, radius, latitude, longitude));
        }
        queryBuilder.append(String.format("node[\"name\"~\"%s\"](around:%d,%f,%f););out body;", name, radius, latitude, longitude));
        return queryBuilder.toString();
    }

    private Place createPlace(OSMResult.Elements data) {
        List<String> types = new ArrayList<>();
        if(data.getTags().getAmenity() != null) {
            types.add(data.getTags().getAmenity());
        }
        Place place = new Place();
        place.setName(data.getTags().getName());
        place.setLatitude(data.getLat());
        place.setLongitude(data.getLon());
        place.setTypes(types);
        return place;
    }

    private List<String> createAmenity(String userQuery) {
        return OSMAmenityMapper.getAmenityList(userQuery);
    }

    public String route(double depLat, double depLng, double destLat, double destLng, MapRouteProfile profile) {
        String query = buildRouteQuery(depLat, depLng, destLat, destLng, profile);
        logger.info("TEST: {}", ROUTER_BASE_URL + query);
        return restClient.get()
                .uri(ROUTER_BASE_URL + query)
                .retrieve()
                .body(String.class);
    }

    private String buildRouteQuery(double depLat, double depLng, double destLat, double destLng, MapRouteProfile profile) {
        return String.format(
            "/%s/%f,%f;%f,%f?overview=simplified&steps=true",
            profile, depLng, depLat, destLng, destLat
        );
    }
}
