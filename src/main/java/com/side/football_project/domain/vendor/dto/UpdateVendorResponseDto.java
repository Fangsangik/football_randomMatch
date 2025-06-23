package com.side.football_project.domain.reservation.dto;

import com.side.football_project.domain.vendor.entity.Vendor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateVendorResponseDto {
    private Long id;
    private String companyName;
    private String address;
    private String phoneNumber;
    private String description;


    @Builder
    public UpdateVendorResponseDto(Long id, String companyName, String address, String phoneNumber, String description) {
        this.id = id;
        this.companyName = companyName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }

    public static UpdateVendorResponseDto toDto(Vendor vendor) {
        return UpdateVendorResponseDto.builder()
                .id(vendor.getId())
                .companyName(vendor.getCompanyName())
                .address(String.valueOf(vendor.getAddress()))
                .phoneNumber(vendor.getPhoneNumber())
                .description(vendor.getDescription())
                .build();
    }
}
