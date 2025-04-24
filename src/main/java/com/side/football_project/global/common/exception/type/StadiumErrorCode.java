package com.side.football_project.global.common.exception.type;

import com.side.football_project.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StadiumErrorCode implements ExceptionType {
    STADIUM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 구장을 찾을 수 없습니다."),
    STADIUM_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "해당 구장은 사용 불가능합니다."),
    STADIUM_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 등록된 구장입니다."),
    STADIUM_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "등록되지 않은 구장입니다."),
    STADIUM_UPDATE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "수정/삭제는 구장 관리자만 가능합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
