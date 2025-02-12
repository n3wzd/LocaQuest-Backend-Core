package com.example.locaquest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

import com.example.locaquest.dto.log.LogObject;

public class LogUtil {
    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);
    
    public static void info(String message, String file, String service) {
        logger.info(createLogObject(message, file, service, null).toString());
    }
    
    public static void info(String message, String file, String service, HttpServletRequest request) {
        logger.info(createLogObject(message, file, service, request).toString());
    }
    
    public static void debug(String message, String file, String service) {
        logger.debug(createLogObject(message, file, service, null).toString());
    }
    
    public static void debug(String message, String file, String service, HttpServletRequest request) {
        logger.debug(createLogObject(message, file, service, request).toString());
    }
    
    public static void warn(String message, String file, String service) {
        logger.warn(createLogObject(message, file, service, null).toString());
    }
    
    public static void warn(String message, String file, String service, HttpServletRequest request) {
        logger.warn(createLogObject(message, file, service, request).toString());
    }
    
    private static LogObject createLogObject(String message, String file, String service, HttpServletRequest request) {
        return LogObject.builder()
            .message(message)
            .request(request != null ? LogObject.RequestInfo.builder()
                .requestId(Optional.ofNullable(request.getHeader("x-request-id")).orElse("default-request-id"))
                .ipAddress(request.getRemoteAddr())
                .method(request.getMethod())
                .url(request.getRequestURI())
                .build() : null)
            .location(LogObject.LocationInfo.builder()
                .file(file)
                .service(service)
                .build())
            .build();
    }
}
