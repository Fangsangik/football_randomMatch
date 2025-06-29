package com.side.football_project.domain.sms;

import lombok.Getter;

@Getter
public class SmsRequestDto {
    /**
     * 수신번호 (예: "01012345678")
     */
    private String to;

    /**
     * 발송할 메시지 내용
     */
    private String text;

    public SmsRequestDto() {}

    public SmsRequestDto(String to, String text) {
        this.to = to;
        this.text = text;
    }
}

