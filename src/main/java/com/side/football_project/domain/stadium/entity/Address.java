package com.side.football_project.domain.stadium.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Address {

    private String city;
    private String state;
    private String postalCode;
    private String specificAddress;

    public Address(String city, String state, String postalCode, String specificAddress) {
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.specificAddress = specificAddress;
    }

    public String getFullAddress() {
        // 한국 주소 표준 순서: 시/도 시/군/구 상세주소 (우편번호 제외)
        StringBuilder address = new StringBuilder();
        
        if (state != null && !state.trim().isEmpty()) {
            address.append(state.trim()).append(" ");
        }
        if (city != null && !city.trim().isEmpty()) {
            address.append(city.trim()).append(" ");
        }
        if (specificAddress != null && !specificAddress.trim().isEmpty()) {
            address.append(specificAddress.trim());
        }
        
        return address.toString().trim();
    }

}