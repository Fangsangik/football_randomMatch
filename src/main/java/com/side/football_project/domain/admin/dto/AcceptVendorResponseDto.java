package com.side.football_project.domain.admin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AcceptVendorResponseDto {
    private String message;

    @Builder
    public AcceptVendorResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static AcceptVendorResponseDto of(String message) {
        return new AcceptVendorResponseDto(message);
    }
}
