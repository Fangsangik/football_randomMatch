package com.side.football_project.global.common.exception.type;

import com.side.football_project.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ExceptionType {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "중복된 이메일 입니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    PASSWORD_DUPLICATED(HttpStatus.BAD_REQUEST, "이전과 같은 비밀번호로 수정할 수 없습니다."),
    PASSWORD_NOT_CONFIRM(HttpStatus.BAD_REQUEST, "수정할 비밀번호가 올바르게 입력되었는지 확인해주세요."),
    USER_NOT_MATCH(HttpStatus.BAD_REQUEST, "유저 정보가 일치하지 않습니다."),
    NOT_ALLOWED(HttpStatus.BAD_REQUEST, "권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
