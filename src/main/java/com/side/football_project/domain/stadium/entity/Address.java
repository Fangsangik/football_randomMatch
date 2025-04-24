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
}