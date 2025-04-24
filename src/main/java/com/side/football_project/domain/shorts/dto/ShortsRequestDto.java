package com.side.football_project.domain.shorts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ShortsRequestDto {
    private final String title;

    private final String description;

    private final String url;

    @Builder
    public ShortsRequestDto(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }
}
