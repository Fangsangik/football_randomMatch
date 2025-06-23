package com.side.football_project.global.common.exception.type;

import com.side.football_project.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminErrorCode implements ExceptionType {
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다."),
    ADMIN_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 관리자입니다."),
    INVALID_ADMIN_KEY(HttpStatus.UNAUTHORIZED, "유효하지 않은 관리자 키입니다."),
    ADMIN_ACCESS_DENIED(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다."),
    ADMIN_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 사용 중인 관리자 이메일입니다."),
    ADMIN_PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "관리자 비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    ADMIN_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "관리자 로그인에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrorCode() {
        return this.name();
    }
}