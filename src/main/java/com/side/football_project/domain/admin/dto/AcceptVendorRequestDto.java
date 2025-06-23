package com.side.football_project.domain.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AcceptVendorRequestDto {
    private Long venderId;

    public AcceptVendorRequestDto(Long venderId) {
        this.venderId = venderId;
    }

    public static AcceptVendorRequestDto of(Long venderId) {
        return new AcceptVendorRequestDto(venderId);
    }
}
