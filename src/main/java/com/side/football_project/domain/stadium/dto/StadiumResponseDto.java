package com.side.football_project.domain.stadium.dto;

import com.side.football_project.domain.stadium.entity.StadiumStatus;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StadiumResponseDto {
    private Long id;
    private String name;
    private StadiumStatus status;
    private String description;
    private Integer capacity;
    private User user;

    @Builder
    public StadiumResponseDto(Long id, String name, StadiumStatus status, String description,Integer capacity, User user) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
        this.capacity = capacity;
        this.user = user;
    }

    public static StadiumResponseDto toDto(Stadium stadium) {
        return StadiumResponseDto.builder()
                .id(stadium.getId())
                .name(stadium.getName())
                .status(stadium.getStatus())
                .description(stadium.getDescription())
                .capacity(stadium.getCapacity())
                .user(stadium.getUser())
                .build();
    }
    
    public static StadiumResponseDto toEntity(Stadium stadium) {
        return toDto(stadium);
    }
}
