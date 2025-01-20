package com.example.reviewsplash.dto;

public class PlaceRequest {
    private Location location;
    private String query;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
