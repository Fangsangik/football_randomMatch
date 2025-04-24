package com.side.football_project.domain.shorts.service;

import com.side.football_project.domain.shorts.dto.ShortsRequestDto;
import com.side.football_project.domain.shorts.dto.ShortsResponseDto;
import com.side.football_project.domain.shorts.entity.Shorts;
import com.side.football_project.domain.user.entity.User;
import org.springframework.data.domain.Page;

public interface ShortsService {
    ShortsResponseDto createShorts(ShortsRequestDto requestDto, User user);

    ShortsResponseDto findShorts(Long id);

    Page<ShortsResponseDto> findShortsFeed(int page, int size);

    ShortsResponseDto updateShorts(Long id, ShortsRequestDto requestDto, User user);

    void deleteShorts(Long id, User user);
}
