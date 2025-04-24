package com.side.football_project.domain.stadium.dto;

import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.stadium.entity.StadiumStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StadiumUpdateDto {
    private Long id;
    private String name;
    private String description;
    private Integer capacity;
    private String city;
    private String state;
    private StadiumStatus stadiumStatus;
    private String postalCode;
    private String specificAddress;

    @Builder
    public StadiumUpdateDto(Long id, String name, String description,StadiumStatus stadiumStatus, Integer capacity, String city, String state, String postalCode, String specificAddress) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.city = city;
        this.state = state;
        this.stadiumStatus = stadiumStatus;
        this.postalCode = postalCode;
        this.specificAddress = specificAddress;
    }

    public static StadiumUpdateDto toEntity(Stadium stadium) {
        return StadiumUpdateDto.builder()
                .name(stadium.getName())
                .stadiumStatus(stadium.getStatus())
                .description(stadium.getDescription())
                .capacity(stadium.getCapacity())
                .build();
    }
}
