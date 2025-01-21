package com.example.reviewsplash.service.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.reviewsplash.dto.OSMResult;
import com.example.reviewsplash.dto.Place;
import com.example.reviewsplash.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenStreetMapAPI {

    private static final String API_BASE_URL = "https://overpass-api.de/api/interpreter";
    private final RestClient restClient;
    final private ObjectMapper objectMapper = new ObjectMapper();
    static final private Logger logger = LoggerFactory.getLogger(GooglePlaceAPI.class);

    public OpenStreetMapAPI(RestClient restClient) {
        this.restClient = restClient;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Place> searchNearbyPlaces(double latitude, double longitude, int radius, String type) {
        String query = buildQuery(latitude, longitude, radius, type);
        String response =  callApi(query);
        // logger.info("OSM API called: {}", response);

        try {
            OSMResult datas = objectMapper.readValue(response, OSMResult.class);
            List<Place> placeResult = new ArrayList<>();
            for(OSMResult.Elements data : datas.getElements()) {
                if(data.getTags() != null) {
                    placeResult.add(createPlace(data));
                }
            }
            return placeResult;
        } catch(JsonProcessingException e) {
            throw new ServiceException(String.format("JSON Failed: %s", e.toString()));
        } catch(ServiceException e) {
            throw new ServiceException(String.format("API Failed: %s", e.toString()));
        }
    }

    private String callApi(String query) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(API_BASE_URL)
                .queryParam("data", query);
        logger.info(decodeUrl(uriBuilder.toUriString()));
        return restClient.get()
                .uri(decodeUrl(uriBuilder.toUriString()))
                .retrieve()
                .body(String.class);
    }

    private String buildQuery(double latitude, double longitude, int radius, String type) {
        return String.format(
                "[out:json];node[\"amenity\"=\"%s\"](around:%d,%f,%f);out body;",
                type, radius, latitude, longitude
        );
    }

    private String decodeUrl(String url) {
        return url.replace("%5B", "[").replace("%5D", "]").replace("%20", " ").replace("%22", "\"").replace("%3D", "=");
    }

    private Place createPlace(OSMResult.Elements data) {
        List<String> types = new ArrayList<>();
        types.add(data.getTags().getAmenity());

        Place place = new Place();
        place.setName(data.getTags().getName());
        place.setLatitude(new BigDecimal(String.valueOf(data.getLat())));
        place.setLongitude(new BigDecimal(String.valueOf(data.getLon())));
        place.setTypes(types);
        return place;
    }
}
