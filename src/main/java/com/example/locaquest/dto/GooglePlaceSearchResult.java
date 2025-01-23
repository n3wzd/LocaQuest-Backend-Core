package com.example.locaquest.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlaceSearchResult {
    private List<Result> results;
    private String status;
    private String errorMessage;
    private List<String> htmlAttributions;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static class Result {
        private String businessStatus;
        private Geometry geometry;
        private String icon;
        private String iconBackgroundColor;
        private String iconMaskBaseUri;
        private String name;
        private List<Photo> photos;
        private String placeId;
        private PlusCode plusCode;
        private double rating;
        private String reference;
        private String scope;
        private List<String> types;
        private int userRatingsTotal;
        private String vicinity;

        public String getBusinessStatus() {
            return businessStatus;
        }

        public void setBusinessStatus(String businessStatus) {
            this.businessStatus = businessStatus;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIconBackgroundColor() {
            return iconBackgroundColor;
        }

        public void setIconBackgroundColor(String iconBackgroundColor) {
            this.iconBackgroundColor = iconBackgroundColor;
        }

        public String getIconMaskBaseUri() {
            return iconMaskBaseUri;
        }

        public void setIconMaskBaseUri(String iconMaskBaseUri) {
            this.iconMaskBaseUri = iconMaskBaseUri;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public PlusCode getPlusCode() {
            return plusCode;
        }

        public void setPlusCode(PlusCode plusCode) {
            this.plusCode = plusCode;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public int getUserRatingsTotal() {
            return userRatingsTotal;
        }

        public void setUserRatingsTotal(int userRatingsTotal) {
            this.userRatingsTotal = userRatingsTotal;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public static class Geometry {
            private Location location;
            private Viewport viewport;

            public Location getLocation() {
                return location;
            }

            public void setLocation(Location location) {
                this.location = location;
            }

            public Viewport getViewport() {
                return viewport;
            }

            public void setViewport(Viewport viewport) {
                this.viewport = viewport;
            }
        }

        public static class Location {
            private double lat;
            private double lng;

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }
        }

        public static class Viewport {
            private Location northeast;
            private Location southwest;

            public Location getNortheast() {
                return northeast;
            }

            public void setNortheast(Location northeast) {
                this.northeast = northeast;
            }

            public Location getSouthwest() {
                return southwest;
            }

            public void setSouthwest(Location southwest) {
                this.southwest = southwest;
            }
        }

        public static class Photo {
            private int height;
            private List<String> htmlAttributions;
            private String photoReference;
            private int width;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public List<String> getHtmlAttributions() {
                return htmlAttributions;
            }

            public void setHtmlAttributions(List<String> htmlAttributions) {
                this.htmlAttributions = htmlAttributions;
            }

            public String getPhotoReference() {
                return photoReference;
            }

            public void setPhotoReference(String photoReference) {
                this.photoReference = photoReference;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }
        }

        public static class PlusCode {
            private String compoundCode;
            private String globalCode;

            public String getCompoundCode() {
                return compoundCode;
            }

            public void setCompoundCode(String compoundCode) {
                this.compoundCode = compoundCode;
            }

            public String getGlobalCode() {
                return globalCode;
            }

            public void setGlobalCode(String globalCode) {
                this.globalCode = globalCode;
            }
        }
    }
}
