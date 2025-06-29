package com.side.football_project.domain.stadium.service;

import com.side.football_project.domain.address.dto.LatLngDto;
import com.side.football_project.domain.address.service.KakaoMapServiceImpl;
import com.side.football_project.domain.stadium.dto.*;
import com.side.football_project.domain.stadium.entity.Address;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.stadium.entity.StadiumStatus;
import com.side.football_project.domain.reservation.repository.ReservationRepository;
import com.side.football_project.domain.stadium.repository.StadiumRepository;
import com.side.football_project.domain.stadium.repository.VendorStadiumRepository;
import com.side.football_project.domain.vendor.dto.VendorResponseDto;
import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.domain.vendor.repository.VendorRepository;
import com.side.football_project.domain.vendor.service.VendorService;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.StadiumErrorCode;
import com.side.football_project.global.common.exception.type.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorStadiumServiceImpl implements VendorStadiumService {

    private final StadiumRepository stadiumRepository;
    private final VendorService vendorService;
    private final VendorRepository vendorRepository;
    private final KakaoMapServiceImpl kakaoMapService;
    private final VendorStadiumRepository vendorStadiumRepository;
    private final ReservationRepository reservationRepository;


    //Todo : S3 image upload 기능 추가 필요
    @Transactional
    @Override
    public StadiumResponseDto createStadium(StadiumRequestDto requestDto, VendorResponseDto vendorDto) {
        Vendor vendor = vendorRepository.findById(vendorDto.getId())
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_ALLOWED));
        validateVendorRole(vendor);

        Address address = new Address(
                requestDto.getCity(),
                requestDto.getState(),
                requestDto.getPostalCode(),
                requestDto.getSpecificAddress()
        );

        String fullAddress = address.getFullAddress();
        LatLngDto latLng = kakaoMapService.getCoordinates(fullAddress);

        Stadium stadium = Stadium.builder()
                .name(requestDto.getName())
                .status(StadiumStatus.AVAILABLE)
                .description(requestDto.getDescription())
                .capacity(requestDto.getCapacity())
                .latitude(latLng.getLatitude())
                .longitude(latLng.getLongitude())
                .vendor(vendor)
                .build();

        stadium.applyAddress(address);

        vendorStadiumRepository.save(stadium);
        return StadiumResponseDto.toEntity(stadium);
    }

    //Todo : S3 image upload 기능 추가 필요 & 수정
    @Transactional
    @Override
    public StadiumUpdateDto updateStadium(StadiumUpdateDto requestDto, VendorResponseDto vendorDto) {
        Vendor vendor = vendorRepository.findById(vendorDto.getId())
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_ALLOWED));
        validateVendorRole(vendor);
        Stadium stadium = stadiumRepository.findByIdOrElseThrow(requestDto.getId());

        // 경기장 소유자 확인
        if (!stadium.getVendor().getId().equals(vendor.getId())) {
            throw new CustomException(UserErrorCode.NOT_ALLOWED);
        }

        // 주소가 변경된 경우 새로운 좌표 계산
        Address newAddress = new Address(requestDto.getCity(), requestDto.getState(),
                requestDto.getPostalCode(), requestDto.getSpecificAddress());
        String fullAddress = newAddress.getFullAddress();
        LatLngDto latLng = kakaoMapService.getCoordinates(fullAddress);

        // 기존 Stadium 엔티티 업데이트
        stadium.updateStadium(requestDto.getName(), requestDto.getDescription(),
                requestDto.getCapacity(), latLng.getLatitude(), latLng.getLongitude());
        stadium.applyAddress(newAddress);

        vendorStadiumRepository.save(stadium);
        return StadiumUpdateDto.toEntity(stadium);
    }

    @Override
    public FindMyStadiumResponseDto findMyStadiums(Long stadiumId, VendorResponseDto vendorDto) {
        Vendor vendor = vendorRepository.findById(vendorDto.getId())
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_ALLOWED));
        validateVendorRole(vendor);

        Stadium stadium = vendorStadiumRepository.findByIdAndVendor(stadiumId, vendor.getId())
                .orElseThrow(() -> new CustomException(StadiumErrorCode.STADIUM_NOT_FOUND));

        return FindMyStadiumResponseDto.toEntity(stadium);
    }

    @Override
    public List<StadiumResponseDto> getMyStadiums(VendorResponseDto vendorDto) {
        Vendor vendor = vendorRepository.findById(vendorDto.getId())
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_ALLOWED));
        validateVendorRole(vendor);
        List<Stadium> stadiums = stadiumRepository.findByVendorOrderByCreatedAtDesc(vendor);
        return stadiums.stream()
                .map(StadiumResponseDto::toEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteStadium(Long id, VendorResponseDto vendorDto) {
        Vendor vendor = vendorRepository.findById(vendorDto.getId())
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_ALLOWED));
        validateVendorRole(vendor);
        Stadium stadium = stadiumRepository.findByIdOrElseThrow(id);

        // 경기장 소유자 확인
        if (!stadium.getVendor().getId().equals(vendor.getId())) {
            throw new CustomException(UserErrorCode.NOT_ALLOWED);
        }

        stadiumRepository.deleteById(stadium.getId());
    }

    private static void validateVendorRole(Vendor vendor) {
        // Vendor인 경우에만 접근 허용 (기존 로직이 반대였음)
        if (vendor == null) {
            throw new CustomException(UserErrorCode.NOT_ALLOWED);
        }
        // 추가적인 Vendor 상태 검증 로직이 필요하다면 여기에 추가
    }

    /**
     * 특정 경기장의 통계 정보를 조회합니다.
     * @param stadiumId 경기장 ID
     * @param vendor 벤더 정보
     * @return 경기장 통계 정보
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStadiumStatistics(Long stadiumId, VendorResponseDto vendorDto) {
        Vendor vendor = vendorRepository.findById(vendorDto.getId())
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_ALLOWED));
        validateVendorRole(vendor);
        
        Stadium stadium = vendorStadiumRepository.findByIdAndVendor(stadiumId, vendor.getId())
                .orElseThrow(() -> new CustomException(StadiumErrorCode.STADIUM_NOT_FOUND));
        
        Map<String, Object> statistics = new HashMap<>();
        
        // 현재 예약 수
        int currentReservationCount = stadium.getCurrentReservationCount();
        statistics.put("currentReservations", currentReservationCount);
        statistics.put("capacity", stadium.getCapacity());
        statistics.put("occupancyRate", stadium.getCapacity() > 0 ? 
                       (double) currentReservationCount / stadium.getCapacity() * 100 : 0);
        
        // 최근 30일 예약 통계
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        Long totalReservationsLast30Days = reservationRepository.countByStadiumIdAndCreatedAtAfter(stadiumId, thirtyDaysAgo);
        statistics.put("reservationsLast30Days", totalReservationsLast30Days);
        
        // 일일 평균 예약 수
        double dailyAverageReservations = totalReservationsLast30Days / 30.0;
        statistics.put("dailyAverageReservations", Math.round(dailyAverageReservations * 100.0) / 100.0);
        
        // 경기장 상태
        statistics.put("stadiumStatus", stadium.getStatus().name());
        statistics.put("stadiumName", stadium.getName());
        
        return statistics;
    }
    
    /**
     * 벤더의 모든 경기장 통계를 조회합니다.
     * @param vendor 벤더 정보
     * @return 전체 경기장 통계 정보
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAllStadiumStatistics(VendorResponseDto vendorDto) {
        Vendor vendor = vendorRepository.findById(vendorDto.getId())
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_ALLOWED));
        validateVendorRole(vendor);
        
        List<Stadium> stadiums = stadiumRepository.findByVendorOrderByCreatedAtDesc(vendor);
        
        Map<String, Object> overallStats = new HashMap<>();
        
        // 전체 경기장 수
        overallStats.put("totalStadiums", stadiums.size());
        
        // 전체 용량 및 현재 예약 수
        int totalCapacity = stadiums.stream().mapToInt(Stadium::getCapacity).sum();
        int totalCurrentReservations = stadiums.stream().mapToInt(Stadium::getCurrentReservationCount).sum();
        
        overallStats.put("totalCapacity", totalCapacity);
        overallStats.put("totalCurrentReservations", totalCurrentReservations);
        overallStats.put("overallOccupancyRate", totalCapacity > 0 ? 
                        (double) totalCurrentReservations / totalCapacity * 100 : 0);
        
        // 최근 30일 전체 예약 통계
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        Long totalReservationsLast30Days = 0L;
        
        for (Stadium stadium : stadiums) {
            totalReservationsLast30Days += reservationRepository.countByStadiumIdAndCreatedAtAfter(
                    stadium.getId(), thirtyDaysAgo);
        }
        
        overallStats.put("totalReservationsLast30Days", totalReservationsLast30Days);
        overallStats.put("dailyAverageReservations", Math.round(totalReservationsLast30Days / 30.0 * 100.0) / 100.0);
        
        // 경기장별 간단 통계
        List<Map<String, Object>> stadiumStats = stadiums.stream()
                .map(stadium -> {
                    Map<String, Object> stadiumStat = new HashMap<>();
                    stadiumStat.put("stadiumId", stadium.getId());
                    stadiumStat.put("stadiumName", stadium.getName());
                    stadiumStat.put("currentReservations", stadium.getCurrentReservationCount());
                    stadiumStat.put("capacity", stadium.getCapacity());
                    stadiumStat.put("occupancyRate", stadium.getCapacity() > 0 ? 
                                   (double) stadium.getCurrentReservationCount() / stadium.getCapacity() * 100 : 0);
                    stadiumStat.put("status", stadium.getStatus().name());
                    return stadiumStat;
                })
                .collect(Collectors.toList());
        
        overallStats.put("stadiumStatistics", stadiumStats);
        
        return overallStats;
    }
}
