package com.side.football_project.domain.vendor.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindMyStadiumResponseDto {
    private Long id;
    private String companyName;
    private String address;
    private String phoneNumber;
    private String description;

    public FindMyStadiumResponseDto(Long id, String companyName, String address, String phoneNumber, String description) {
        this.id = id;
        this.companyName = companyName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }
}
