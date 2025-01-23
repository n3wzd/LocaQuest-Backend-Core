package com.example.locaquest.dto;

import com.example.locaquest.dto.constant.MapRouteProfile;

public class MapRouteRequest {
    private double depLatitude = 0;
    private double depLongitude = 0;
    private double destLatitude = 0;
    private double destLongitude = 0;
    private MapRouteProfile mapRouteProfile = MapRouteProfile.walking;

    public double getDepLatitude() {
        return depLatitude;
    }

    public void setDepLatitude(double depLatitude) {
        this.depLatitude = depLatitude;
    }

    public double getDepLongitude() {
        return depLongitude;
    }

    public void setDepLongitude(double depLongitude) {
        this.depLongitude = depLongitude;
    }

    public double getDestLatitude() {
        return destLatitude;
    }

    public void setDestLatitude(double destLatitude) {
        this.destLatitude = destLatitude;
    }

    public double getDestLongitude() {
        return destLongitude;
    }

    public void setDestLongitude(double destLongitude) {
        this.destLongitude = destLongitude;
    }

    public MapRouteProfile getMapRouteProfile() {
        return mapRouteProfile;
    }

    public void setMapRouteProfile(MapRouteProfile mapRouteProfile) {
        this.mapRouteProfile = mapRouteProfile;
    }
}
