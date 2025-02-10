package com.example.locaquest.dto.place;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OSMResult {
    private List<Elements> elements;

    public List<Elements> getElements() {
        return elements;
    }

    public void setElements(List<Elements> elements) {
        this.elements = elements;
    }

    public static class Elements {
        @JsonProperty("type")
        private String type;

        @JsonProperty("id")
        private long id;

        @JsonProperty("lat")
        private double lat;

        @JsonProperty("lon")
        private double lon;

        @JsonProperty("tags")
        private Tags tags;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public Tags getTags() {
            return tags;
        }

        public void setTags(Tags tags) {
            this.tags = tags;
        }

        public static class Tags {

            @JsonProperty("amenity")
            private String amenity;

            @JsonProperty("name")
            private String name;

            @JsonProperty("name:en")
            private String nameEn;

            @JsonProperty("name:ko")
            private String nameKo;

            public String getAmenity() {
                return amenity;
            }

            public void setAmenity(String amenity) {
                this.amenity = amenity;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNameEn() {
                return nameEn;
            }

            public void setNameEn(String nameEn) {
                this.nameEn = nameEn;
            }

            public String getNameKo() {
                return nameKo;
            }

            public void setNameKo(String nameKo) {
                this.nameKo = nameKo;
            }
        }
    }
}
