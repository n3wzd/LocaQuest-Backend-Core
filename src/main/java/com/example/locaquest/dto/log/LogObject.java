package com.example.locaquest.dto.log;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class LogObject {
    private final String message;
    private final RequestInfo request;
    private final LocationInfo location;

    @Getter
    @Builder
    @ToString
    public static class RequestInfo {
        private final String requestId;
        private final String ipAddress;
        private final String method;
        private final String url;
    }

    @Getter
    @Builder
    @ToString
    public static class LocationInfo {
        private final String file;
        private final String service;
    }
}
