package com.side.football_project.domain.stadium.dto;

import com.side.football_project.domain.stadium.entity.Stadium;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindMyStadiumResponseDto {
    private Long stadiumId;
    private String address;
    private String phoneNumber;
    private String description;

    @Builder
    public FindMyStadiumResponseDto(Long stadiumId, String companyName, String address, String phoneNumber, String description) {
        this.stadiumId = stadiumId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }

    public static FindMyStadiumResponseDto toEntity(Stadium stadium) {
        return FindMyStadiumResponseDto.builder()
                .stadiumId(stadium.getId())
                .address(String.valueOf(stadium.getAddress()))
                .description(stadium.getDescription())
                .build();
    }
}
