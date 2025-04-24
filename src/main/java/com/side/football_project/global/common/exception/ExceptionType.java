package com.side.football_project.global.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionType {
    HttpStatus getHttpStatus();

    String getErrorCode();

    String getMessage();
}
