package com.side.football_project.domain.stadium.service;

import com.side.football_project.domain.address.dto.LatLngDto;
import com.side.football_project.domain.address.service.KakaoMapServiceImpl;
import com.side.football_project.domain.stadium.dto.StadiumRequestDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumUpdateDto;
import com.side.football_project.domain.stadium.entity.Address;
import com.side.football_project.domain.stadium.entity.StadiumStatus;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.stadium.repository.StadiumRepository;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.domain.vendor.dto.VendorResponseDto;
import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.domain.vendor.service.VendorService;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;

    @Override
    @Transactional
    public StadiumResponseDto findStadium(Long id) {
        Stadium stadium = stadiumRepository.findByIdOrElseThrow(id);
        return StadiumResponseDto.toEntity(stadium);
    }

    @Override
    public Stadium findByIdWithLock(Long id) {
        return stadiumRepository.findByIdWithLock(id).orElseThrow(() -> new IllegalStateException("찾을 수 없는 경기장입니다."));
    }

    @Override
    public Stadium findStadiumById(Long id) {
        return stadiumRepository.findByIdOrElseThrow(id);
    }


    @Override
    public List<StadiumResponseDto> searchStadiums(String region, String keyword, int page, int size) {
        List<Stadium> stadiums = stadiumRepository.searchStadiums(region, keyword, page, size);
        return stadiums.stream()
                .map(StadiumResponseDto::toDto)
                .toList();
    }

    @Override
    public List<StadiumResponseDto> findNearbyStadiums(Double latitude, Double longitude, Double radiusKm, int page, int size) {
        List<Stadium> stadiums = stadiumRepository.findNearbyStadiums(latitude, longitude, radiusKm, page, size);
        return stadiums.stream()
                .map(StadiumResponseDto::toDto)
                .toList();
    }

    @Override
    public StadiumResponseDto findById(Long stadiumId) {
        Stadium stadium = stadiumRepository.findByIdOrElseThrow(stadiumId);
        return StadiumResponseDto.toDto(stadium);
    }
}
