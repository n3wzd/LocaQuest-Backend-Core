package com.example.locaquest.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Place {
    private String name;
    private double latitude = 0;
    private double longitude = 0;
    private double rating = 0;
    private int reviewCount = 0;
    private List<String> types = new ArrayList<>();
    private double distanceFromOrigin = 0;

    public void setDistanceFromOrigin(double lat, double lon) {
        this.distanceFromOrigin = calculateDistance(lat, lon, this.latitude, this.longitude);
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /*private static double calculateCustomScore(Place place, User user) {
        double score = 0.0;
        score += place.getRating() * 0.5;
        score += place.getReviewCount() * 0.3;
        score += user.getPreferences().contains(place.getType()) ? 10 : 0;
        return score;
    }*/
}
