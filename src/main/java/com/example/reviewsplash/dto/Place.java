package com.example.reviewsplash.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Place {
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal rating;
    private int reviews;
    private List<String> types;

    public Place() {
        reviews = 0;
        rating = new BigDecimal("0");
        types = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }
}
