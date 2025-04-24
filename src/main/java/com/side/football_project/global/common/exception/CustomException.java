package com.side.football_project.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    public CustomException(ExceptionType exceptionType) {
        super();
        this.httpStatus = exceptionType.getHttpStatus();
        this.errorCode = exceptionType.getErrorCode();
        this.message = exceptionType.getMessage();
    }
}
