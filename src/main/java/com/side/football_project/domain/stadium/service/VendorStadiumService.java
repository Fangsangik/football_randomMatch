package com.side.football_project.domain.stadium.service;

import com.side.football_project.domain.stadium.dto.FindMyStadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumRequestDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumUpdateDto;
import com.side.football_project.domain.vendor.entity.Vendor;

import java.util.List;

public interface VendorStadiumService {
    StadiumResponseDto createStadium(StadiumRequestDto requestDto, Vendor vendor);
    void deleteStadium(Long id, Vendor vendor);
    StadiumUpdateDto updateStadium(StadiumUpdateDto requestDto, Vendor vendor);
    List<StadiumResponseDto> getMyStadiums(Vendor vendor);
    FindMyStadiumResponseDto findMyStadiums(Long stadiumId, Vendor vendor);
}
