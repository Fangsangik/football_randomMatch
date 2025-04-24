package com.side.football_project.global.common.exception.type;

import com.side.football_project.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MatchErrorCode implements ExceptionType {

    MATCH_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 경기를 찾을 수 없습니다."),
    MATCH_NOT_FINISHED(HttpStatus.BAD_REQUEST, "경기가 종료되지 않았습니다."),;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
