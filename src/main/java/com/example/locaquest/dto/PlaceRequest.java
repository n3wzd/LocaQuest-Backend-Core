package com.example.locaquest.dto;

import com.example.locaquest.dto.constant.PlaceSortCriteria;

public class PlaceRequest {
    private double latitude = 0;
    private double longitude = 0;
    private String query;
    private int radius;
    private PlaceSortCriteria sortCriteria = PlaceSortCriteria.DISTANCE;

    private final static int MIN_RADIUS = 500;       // 0.5km
    private final static int MAX_RADIUS = 10000;     // 10km

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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        radius = Math.max(MIN_RADIUS, Math.min(MAX_RADIUS, radius));
        this.radius = radius;
    }

    public PlaceSortCriteria getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(PlaceSortCriteria sortCriteria) {
        this.sortCriteria = sortCriteria;
    }
}
