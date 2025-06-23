package com.side.football_project.domain.vendor.dto;

import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.domain.vendor.type.VendorStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VendorResponseDto {
    
    private final Long id;
    private final Long userId;
    private final String userName;
    private final String userEmail;
    private final String businessNumber;
    private final String companyName;
    private final String businessAddress;
    private final String description;
    private VendorStatus status;
    private final LocalDateTime appliedAt;
    private final LocalDateTime approvedAt;
    private final LocalDateTime rejectedAt;
    private final String rejectionReason;
    
    @Builder
    private VendorResponseDto(Long id, Long userId, String userName, String userEmail,
                             String businessNumber, String companyName, String businessAddress,
                             String description, VendorStatus status, LocalDateTime appliedAt,
                             LocalDateTime approvedAt, LocalDateTime rejectedAt, String rejectionReason) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.businessNumber = businessNumber;
        this.companyName = companyName;
        this.businessAddress = businessAddress;
        this.description = description;
        this.status = status;
        this.appliedAt = appliedAt;
        this.approvedAt = approvedAt;
        this.rejectedAt = rejectedAt;
        this.rejectionReason = rejectionReason;
    }
    
    public static VendorResponseDto toDto(Vendor vendor) {
        return VendorResponseDto.builder()
                .id(vendor.getId())
                .userId(vendor.getId()) // Vendor 자체 ID 사용
                .userName(vendor.getName())
                .userEmail(vendor.getEmail())
                .businessNumber(vendor.getBusinessNumber())
                .companyName(vendor.getCompanyName())
                .businessAddress(vendor.getBusinessAddress())
                .description(vendor.getDescription())
                .status(vendor.getStatus())
                .appliedAt(vendor.getAppliedAt())
                .approvedAt(vendor.getApprovedAt())
                .rejectedAt(vendor.getRejectedAt())
                .rejectionReason(vendor.getRejectionReason())
                .build();
    }

    public static Vendor toEntity(VendorResponseDto dto) {
        return Vendor.builder()
                .email(dto.getUserEmail())
                .name(dto.getUserName())
                .businessNumber(dto.getBusinessNumber())
                .companyName(dto.getCompanyName())
                .businessAddress(dto.getBusinessAddress())
                .description(dto.getDescription())
                .build();
    }

    public void acceptVendor(VendorStatus vendorStatus) {
        this.status = vendorStatus;
    }
}