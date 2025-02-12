package com.example.locaquest.config.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.locaquest.controller.UserController;
import com.example.locaquest.controller.PlaceController;
import com.example.locaquest.controller.UserStatusController;
import com.example.locaquest.controller.ActivityController;
import com.example.locaquest.exception.JavaMailException;
import com.example.locaquest.exception.EmailExistsException;
import com.example.locaquest.exception.EmailNotExistsException;
import com.example.locaquest.exception.ServiceException;
import com.example.locaquest.exception.WrongPasswordException;
import com.example.locaquest.util.LogUtil;

import jakarta.servlet.http.HttpServletRequest;

import com.example.locaquest.exception.TokenException;

@ControllerAdvice(assignableTypes = { UserController.class, PlaceController.class, UserStatusController.class, ActivityController.class })
public class HTTPControllerAdvice {

    final private String filePath = "config.HTTPControllerAdvice";

    @ExceptionHandler(JavaMailException.class)
    public ResponseEntity<String> handleJavaMailException(JavaMailException e, HttpServletRequest request) {
        LogUtil.warn(e.toString(), filePath, "JavaMailException", request);
        return ResponseEntity.badRequest().body("이메일 전송 오류!");
    }
    
    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<String> handleEmailExistsException(EmailExistsException e, HttpServletRequest request) {
        LogUtil.warn(e.toString(), filePath, "EmailExistsException", request);
        return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
    }

    @ExceptionHandler(EmailNotExistsException.class)
    public ResponseEntity<String> handleEmailNotExistsException(EmailNotExistsException e, HttpServletRequest request) {
    	LogUtil.warn(e.toString(), filePath, "EmailNotExistsException", request);
        return ResponseEntity.badRequest().body("존재하지 않는 이메일입니다.");
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<String> handleWrongPasswordException(WrongPasswordException e, HttpServletRequest request) {
        LogUtil.warn(e.toString(), filePath, "WrongPasswordException", request);
        return ResponseEntity.badRequest().body("비밀번호가 잘못되었습니다.");
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> handleTokenException(TokenException e, HttpServletRequest request) {
        LogUtil.warn(e.toString(), filePath, "TokenException", request);
        return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        LogUtil.warn(e.toString(), filePath, "IllegalArgumentException", request);
        return ResponseEntity.badRequest().body("입력 양식이 잘못되었습니다.");
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleServiceException(ServiceException e, HttpServletRequest request) {
        LogUtil.warn(e.toString(), filePath, "ServiceException", request);
        return ResponseEntity.badRequest().body("서버 오류가 발생했습니다.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e, HttpServletRequest request) {
        LogUtil.warn(e.toString(), filePath, "Exception", request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류!");
    }
}
