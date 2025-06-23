package com.side.football_project.domain.stadium.service;

import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.entity.Stadium;

import java.util.List;

public interface StadiumService {
    StadiumResponseDto findStadium(Long id);
    Stadium findByIdWithLock(Long id);

    Stadium findStadiumById(Long id);
    // 사용자용 검색 메서드들
    List<StadiumResponseDto> searchStadiums(String region, String keyword, int page, int size);
    List<StadiumResponseDto> findNearbyStadiums(Double latitude, Double longitude, Double radiusKm, int page, int size);
    StadiumResponseDto findById(Long stadiumId);
}
