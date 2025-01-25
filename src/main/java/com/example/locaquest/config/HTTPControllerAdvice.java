package com.example.locaquest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.locaquest.controller.UserController;
import com.example.locaquest.controller.PlaceController;
import com.example.locaquest.exception.EmailExistsException;
import com.example.locaquest.exception.EmailNotExistsException;
import com.example.locaquest.exception.ServiceException;
import com.example.locaquest.exception.WrongPasswordException;
import com.example.locaquest.exception.TokenException;

@ControllerAdvice(assignableTypes = { UserController.class, PlaceController.class })
public class HTTPControllerAdvice {

    static final private Logger logger = LoggerFactory.getLogger(HTTPControllerAdvice.class);

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<String> handleEmailExistsException(EmailExistsException e) {
        logger.warn("EmailExistsException occurred: {}", e.toString());
        return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
    }

    @ExceptionHandler(EmailNotExistsException.class)
    public ResponseEntity<String> handleEmailNotExistsException(EmailNotExistsException e) {
        logger.warn("EmailNotExistsException occurred: {}", e.toString());
        return ResponseEntity.badRequest().body("존재하지 않는 이메일입니다.");
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<String> handleWrongPasswordException(WrongPasswordException e) {
        logger.warn("WrongPasswordException occurred: {}", e.toString());
        return ResponseEntity.badRequest().body("비밀번호가 잘못되었습니다.");
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> handleTokenException(TokenException e) {
        logger.warn("TokenException occurred: {}", e.toString());
        return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("IllegalArgumentException occurred: {}", e.toString());
        return ResponseEntity.badRequest().body("입력 양식이 잘못되었습니다.");
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleServiceException(ServiceException e) {
        logger.warn("ServiceException occurred: {}", e.toString());
        return ResponseEntity.badRequest().body("서버 오류가 발생했습니다.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unexpected error occurred: {}", e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류!");
    }
}
