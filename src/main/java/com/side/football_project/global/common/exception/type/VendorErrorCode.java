package com.side.football_project.global.common.exception.type;

import com.side.football_project.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum VendorErrorCode implements ExceptionType {
    VENDOR_NOT_FOUND(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다."),
    VENDOR_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 업체입니다."),
    BUSINESS_NUMBER_DUPLICATED(HttpStatus.CONFLICT, "이미 등록된 사업자번호입니다."),
    VENDOR_NOT_PENDING(HttpStatus.BAD_REQUEST, "승인 대기 상태의 업체가 아닙니다."),
    VENDOR_ALREADY_APPROVED(HttpStatus.BAD_REQUEST, "이미 승인된 업체입니다."),
    VENDOR_ALREADY_REJECTED(HttpStatus.BAD_REQUEST, "이미 거절된 업체입니다."),
    VENDOR_ACCESS_DENIED(HttpStatus.FORBIDDEN, "업체 권한이 필요합니다."),
    INVALID_VENDOR_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 업체 상태입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrorCode() {
        return this.name();
    }
}