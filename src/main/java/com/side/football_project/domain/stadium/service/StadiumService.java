package com.side.football_project.domain.stadium.service;

import com.side.football_project.domain.stadium.dto.StadiumRequestDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumUpdateDto;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.user.entity.User;

public interface StadiumService {
    StadiumResponseDto createStadium(StadiumRequestDto requestDto, User user);
    StadiumResponseDto findStadium(Long id);
    StadiumUpdateDto updateStadium(StadiumUpdateDto requestDto, User user);
    Stadium findByIdWithLock(Long id);
    Stadium findStadiumById(Long id);
    void deleteStadium(Long id, User user);
}
