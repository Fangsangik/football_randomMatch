package com.side.football_project.domain.vendor.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VendorRequestDto {
    
    // User 정보
    private final String email;
    private final String password;
    private final String name;
    private final String phoneNumber;
    private final int age;
    
    // Vendor 정보
    private final String businessNumber;
    private final String companyName;
    private final String businessAddress;
    private final String description;
    
    @Builder
    public VendorRequestDto(String email, String password, String name, String phoneNumber, 
                           int age, String businessNumber, String companyName, 
                           String businessAddress, String description) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.businessNumber = businessNumber;
        this.companyName = companyName;
        this.businessAddress = businessAddress;
        this.description = description;
    }
}