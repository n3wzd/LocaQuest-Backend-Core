package com.example.locaquest.dto.place;

import com.example.locaquest.dto.constant.MapRouteProfile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapRouteRequest {
    private double depLatitude = 0;
    private double depLongitude = 0;
    private double destLatitude = 0;
    private double destLongitude = 0;
    private MapRouteProfile mapRouteProfile = MapRouteProfile.walking;
}
