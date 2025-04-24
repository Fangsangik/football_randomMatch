package com.side.football_project.global.common.exception.type;

import com.side.football_project.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeamErrorCode implements ExceptionType {
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다."),
    TEAM_UPDATE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "수정/삭제는 팀 대표자만 가능합니다."),
    TEAM_FULL(HttpStatus.BAD_REQUEST, "팀이 가득 찼습니다."),
    ALREADY_TEAM_MEMBER(HttpStatus.BAD_REQUEST, "이미 팀에 가입된 사용자입니다."),;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
