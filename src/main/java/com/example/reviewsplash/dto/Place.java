package com.example.reviewsplash.dto;

import java.util.ArrayList;
import java.util.List;

public class Place {
    private String name;
    private double latitude = 0;
    private double longitude = 0;
    private double rating = 0;
    private int reviewCount = 0;
    private List<String> types = new ArrayList<>();
    private double distanceFromOrigin = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getDistanceFromOrigin() {
        return distanceFromOrigin;
    }

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
