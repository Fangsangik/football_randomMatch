package com.side.football_project.domain.user.service;

import com.side.football_project.domain.user.dto.FindUserWithRadiusDto;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 위치 기반 검색 서비스
 */
@Service
@RequiredArgsConstructor
public class LocationService {
    private final UserRepository userRepository;

    /** 지구 반지름 (단위: km) */
    private static final int EARTH_RADIUS_KM = 6_371;

    /**
     * 주어진 DTO의 중심 좌표와 반경, 티어에 따라
     * DB 내 사용자 목록 중 해당 반경 이내의 사용자만 필터링하여 반환
     */
    public List<User> findUsersWithinRadius(FindUserWithRadiusDto dto) {
        double centerLat = dto.getCenterLatitude();
        double centerLng = dto.getCenterLongitude();
        int    radiusKm  = dto.getRadiusKm();

        // 해당 티어 사용자 전체 조회
        List<User> all = userRepository.findByTier(dto.getUserTier().name());

        return all.stream()
                // 위경도 값이 있는 사용자만
                .filter(u -> u.getLatitude()  != 0.0
                        && u.getLongitude() != 0.0)
                // Haversine 공식으로 거리 계산 후 반경 이내 필터
                .filter(u -> haversineDistance(
                        centerLat,
                        centerLng,
                        u.getLatitude(),
                        u.getLongitude()
                ) <= radiusKm)
                .collect(Collectors.toList());
    }

    /**
     * Haversine 공식으로 두 위경도 좌표 간의 직선거리(km) 계산
     *
     * @param lat1 기준점 위도
     * @param lon1 기준점 경도
     * @param lat2 대상   위도
     * @param lon2 대상   경도
     * @return 두 점 사이 거리 (단위: km)
     */
    private double haversineDistance(double lat1, double lon1,
                                     double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
