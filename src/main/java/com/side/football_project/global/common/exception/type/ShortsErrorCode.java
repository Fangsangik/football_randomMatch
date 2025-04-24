package com.side.football_project.global.common.exception.type;

import com.side.football_project.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ShortsErrorCode implements ExceptionType {
    SHORTS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 숏츠를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
