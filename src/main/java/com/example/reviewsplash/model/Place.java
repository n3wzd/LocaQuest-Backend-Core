package com.example.reviewsplash.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "places")
public class Place {

    @Id
    @Column(name = "place_id", nullable = false, unique = true, length = 255)
    private String placeId;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "rating", precision = 2, scale = 1)
    private BigDecimal rating;

    @Lob
    @Column(name = "address_components", columnDefinition = "json")
    private String addressComponents;

    @Lob
    @Column(name = "opening_hours", columnDefinition = "json")
    private String openingHours;

    @Lob
    @Column(name = "types", columnDefinition = "json")
    private String types;

    @Lob
    @Column(name = "reviews", columnDefinition = "json")
    private String reviews;

    @Lob
    @Column(name = "photos", columnDefinition = "json")
    private String photos;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
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

    public String getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(String addressComponents) {
        this.addressComponents = addressComponents;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }
}
