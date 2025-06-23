package com.side.football_project.domain.stadium.repository;

import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.StadiumErrorCode;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select st from Stadium st where st.id =:id")
    Optional<Stadium> findByIdWithLock(@Param("id") Long id);

    List<Stadium> findByVendorOrderByCreatedAtDesc(Vendor vendor);

    default Stadium findByIdOrElseThrow(Long stadiumId) {
        return findById(stadiumId).orElseThrow(() -> new CustomException(StadiumErrorCode.STADIUM_NOT_FOUND));
    }

    // 사용자용 검색 쿼리들
    @Query("SELECT s FROM Stadium s WHERE " +
           "(:region IS NULL OR :region = '' OR s.address.city LIKE CONCAT('%', :region, '%') OR s.address.state LIKE CONCAT('%', :region, '%')) AND " +
           "(:keyword IS NULL OR :keyword = '' OR s.name LIKE CONCAT('%', :keyword, '%') OR s.description LIKE CONCAT('%', :keyword, '%')) AND " +
           "s.status = com.side.football_project.domain.stadium.entity.StadiumStatus.AVAILABLE " +
           "ORDER BY s.createdAt DESC")
    List<Stadium> searchStadiumsQuery(@Param("region") String region, @Param("keyword") String keyword);

    // 위치 기반 근처 경기장 검색 (Haversine 공식 사용)
    @Query("SELECT s FROM Stadium s WHERE " +
           "s.status = com.side.football_project.domain.stadium.entity.StadiumStatus.AVAILABLE AND " +
           "s.latitude IS NOT NULL AND s.longitude IS NOT NULL AND " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
           "cos(radians(s.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(s.latitude)))) <= :radiusKm " +
           "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
           "cos(radians(s.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(s.latitude))))")
    List<Stadium> findNearbyStadiumsQuery(@Param("latitude") Double latitude, 
                                         @Param("longitude") Double longitude, 
                                         @Param("radiusKm") Double radiusKm);

    // 페이징 처리를 위한 기본 구현
    default List<Stadium> searchStadiums(String region, String keyword, int page, int size) {
        List<Stadium> allResults = searchStadiumsQuery(region, keyword);
        int start = page * size;
        int end = Math.min(start + size, allResults.size());
        
        if (start >= allResults.size()) {
            return List.of();
        }
        
        return allResults.subList(start, end);
    }

    default List<Stadium> findNearbyStadiums(Double latitude, Double longitude, Double radiusKm, int page, int size) {
        List<Stadium> allResults = findNearbyStadiumsQuery(latitude, longitude, radiusKm);
        int start = page * size;
        int end = Math.min(start + size, allResults.size());
        
        if (start >= allResults.size()) {
            return List.of();
        }
        
        return allResults.subList(start, end);
    }
}
