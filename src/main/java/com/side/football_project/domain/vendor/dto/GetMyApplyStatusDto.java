package com.side.football_project.domain.vendor.dto;

import com.side.football_project.domain.vendor.type.VendorStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMyApplyStatusDto {

    private Long vendorId;
    private VendorStatus vendorStatus;
    private String message;

    @Builder
    public GetMyApplyStatusDto(Long vendorId, VendorStatus vendorStatus, String message) {
        this.vendorId = vendorId;
        this.vendorStatus = vendorStatus;
        this.message = message;
    }
}
