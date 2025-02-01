package com.example.locaquest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.locaquest.controller.TemplateController;
import com.example.locaquest.exception.AlreadyVerifiedException;
import com.example.locaquest.exception.EmailExistsException;
import com.example.locaquest.exception.ServiceException;
import com.example.locaquest.exception.TokenException;

@ControllerAdvice(assignableTypes = TemplateController.class)
public class TemplateControllerAdvice {

    static final private Logger logger = LoggerFactory.getLogger(TemplateControllerAdvice.class);

    @ExceptionHandler(TokenException.class)
    public String handleTokenException(TokenException e) {
        logger.warn("TokenException occurred: {}", e.toString());
        return "VerifyInvalid";
    }

    @ExceptionHandler(AlreadyVerifiedException.class)
    public String handleAlreadyVerifiedException(AlreadyVerifiedException e) {
        logger.warn("AlreadyVerifiedException occurred: {}", e.toString());
        return "VerifyAlready";
    }

    @ExceptionHandler(EmailExistsException.class)
    public String handleEmailExistsException(EmailExistsException e) {
        logger.warn("EmailExistsException occurred: {}", e.toString());
        return "VerifyFailed";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("IllegalArgumentException occurred: {}", e.toString());
        return "VerifyFailed";
    }

    @ExceptionHandler(ServiceException.class)
    public String handleServiceException(ServiceException e) {
        logger.warn("ServiceException occurred: {}", e.toString());
        return "VerifyFailed";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        logger.error("Unexpected error occurred: {}", e.toString());
        return "VerifyFailed";
    }
}
