package com.side.football_project.domain.shorts.dto;

import com.side.football_project.domain.shorts.entity.Shorts;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ShortsResponseDto {
    private final Long id;

    private final String title;

    private final String description;

    private final String url;

    private final LocalDateTime createdAt;

    @Builder
    public ShortsResponseDto(Long id, String title, String description, String url, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.createdAt = createdAt;
    }

    public static ShortsResponseDto toDto(Shorts shorts) {
        return new ShortsResponseDto(
                shorts.getId(),
                shorts.getTitle(),
                shorts.getDescription(),
                shorts.getUrl(),
                shorts.getCreatedAt()
        );
    }
}
