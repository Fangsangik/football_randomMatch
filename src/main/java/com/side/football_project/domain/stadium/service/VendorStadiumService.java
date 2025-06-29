package com.side.football_project.domain.stadium.service;

import com.side.football_project.domain.stadium.dto.FindMyStadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumRequestDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumUpdateDto;
import com.side.football_project.domain.vendor.dto.VendorResponseDto;

import java.util.List;
import java.util.Map;

public interface VendorStadiumService {
    StadiumResponseDto createStadium(StadiumRequestDto requestDto, VendorResponseDto vendorDto);
    void deleteStadium(Long id, VendorResponseDto vendorDto);
    StadiumUpdateDto updateStadium(StadiumUpdateDto requestDto, VendorResponseDto vendorDto);
    List<StadiumResponseDto> getMyStadiums(VendorResponseDto vendorDto);
    FindMyStadiumResponseDto findMyStadiums(Long stadiumId, VendorResponseDto vendorDto);
    
    // 통계 기능
    Map<String, Object> getStadiumStatistics(Long stadiumId, VendorResponseDto vendorDto);
    Map<String, Object> getAllStadiumStatistics(VendorResponseDto vendorDto);
}
