package com.side.football_project.domain.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateVendorRequestDto {
    private String name;
    private String address;
    private String phoneNumber;
    private String description;

    public UpdateVendorRequestDto(String name, String address, String phoneNumber, String description) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }
}
