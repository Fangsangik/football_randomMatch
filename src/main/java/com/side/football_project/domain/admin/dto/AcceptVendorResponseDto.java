package com.side.football_project.domain.admin.dto;

public class AcceptVendorResponseDto {
    private String message;

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
