package com.example.locaquest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.locaquest.controller.TemplateController;
import com.example.locaquest.exception.EmailExistsException;
import com.example.locaquest.exception.ServiceException;

@ControllerAdvice(assignableTypes = TemplateController.class)
public class TemplateControllerAdvice {

    private final SpringTemplateEngine templateEngine;
    static final private Logger logger = LoggerFactory.getLogger(TemplateControllerAdvice.class);

    public TemplateControllerAdvice(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @ExceptionHandler(EmailExistsException.class)
    public String handleEmailExistsException(EmailExistsException e) {
        logger.warn("EmailExistsException occurred: {}", e.toString());
        return templateEngine.process("VerifyFailed", new Context());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("IllegalArgumentException occurred: {}", e.toString());
        return templateEngine.process("VerifyFailed", new Context());
    }

    @ExceptionHandler(ServiceException.class)
    public String handleServiceException(ServiceException e) {
        logger.warn("ServiceException occurred: {}", e.toString());
        return templateEngine.process("VerifyFailed", new Context());
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        logger.error("Unexpected error occurred: {}", e.toString());
        return templateEngine.process("VerifyFailed", new Context());
    }
}
