package com.side.football_project.domain.stadium.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StadiumRequestDto {
    private Long userId;
    private String name;
    private String description;
    private Integer capacity;
    private String city;
    private String state;
    private String postalCode;
    private String specificAddress;

    public StadiumRequestDto(Long userId, String name, String description, Integer capacity, String city, String state, String postalCode, String specificAddress) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.specificAddress = specificAddress;
    }
}
