package com.example.locaquest.config.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.locaquest.controller.TemplateController;
import com.example.locaquest.exception.AlreadyVerifiedException;
import com.example.locaquest.exception.EmailExistsException;
import com.example.locaquest.exception.ServiceException;
import com.example.locaquest.exception.TokenException;
import com.example.locaquest.util.LogUtil;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice(assignableTypes = TemplateController.class)
public class TemplateControllerAdvice {

	final private String filePath = "config.HTTPControllerAdvice";

    @ExceptionHandler(TokenException.class)
    public String handleTokenException(TokenException e, HttpServletRequest request) {
    	LogUtil.warn(e.toString(), filePath, "TokenException", request);
        return "VerifyInvalid";
    }

    @ExceptionHandler(AlreadyVerifiedException.class)
    public String handleAlreadyVerifiedException(AlreadyVerifiedException e, HttpServletRequest request) {
    	LogUtil.warn(e.toString(), filePath, "AlreadyVerifiedException", request);
        return "VerifyAlready";
    }

    @ExceptionHandler(EmailExistsException.class)
    public String handleEmailExistsException(EmailExistsException e, HttpServletRequest request) {
    	LogUtil.warn(e.toString(), filePath, "EmailExistsException", request);
        return "VerifyFailed";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        LogUtil.warn(e.toString(), filePath, "IllegalArgumentException", request);
        return "VerifyFailed";
    }

    @ExceptionHandler(ServiceException.class)
    public String handleServiceException(ServiceException e, HttpServletRequest request) {
    	LogUtil.warn(e.toString(), filePath, "ServiceException", request);
        return "VerifyFailed";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, HttpServletRequest request) {
    	LogUtil.warn(e.toString(), filePath, "Exception", request);
        return "VerifyFailed";
    }
}
