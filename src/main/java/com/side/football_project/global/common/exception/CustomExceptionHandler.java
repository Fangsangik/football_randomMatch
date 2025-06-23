package com.side.football_project.global.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponseDto> customExceptionHandler(CustomException e) {
        return new ResponseEntity<>(new ExceptionResponseDto(e.getErrorCode(), e.getMessage()), e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * IllegalStateException 처리 (Admin 관련)
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponseDto> handleIllegalStateException(IllegalStateException e) {
        log.error("IllegalStateException occurred: {}", e.getMessage());
        return new ResponseEntity<>(
                new ExceptionResponseDto("ILLEGAL_STATE", e.getMessage()), 
                HttpStatus.BAD_REQUEST
        );
    }
    
    /**
     * IllegalArgumentException 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException occurred: {}", e.getMessage());
        return new ResponseEntity<>(
                new ExceptionResponseDto("ILLEGAL_ARGUMENT", e.getMessage()), 
                HttpStatus.BAD_REQUEST
        );
    }
    
    /**
     * RuntimeException 처리 (Vendor 관련)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponseDto> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException occurred: {}", e.getMessage());
        return new ResponseEntity<>(
                new ExceptionResponseDto("RUNTIME_ERROR", e.getMessage()), 
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    
    /**
     * 일반적인 Exception 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleGeneralException(Exception e) {
        log.error("Unexpected exception occurred: {}", e.getMessage(), e);
        return new ResponseEntity<>(
                new ExceptionResponseDto("INTERNAL_ERROR", "서버 내부 오류가 발생했습니다."), 
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
