package com.side.football_project.domain.stadium.repository;

import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.StadiumErrorCode;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select st from Stadium st where st.id =:id")
    Optional<Stadium> findByIdWithLock(@Param("id") Long id);

    default Stadium findByIdOrElseThrow(Long stadiumId) {
        return findById(stadiumId).orElseThrow(() -> new CustomException(StadiumErrorCode.STADIUM_NOT_FOUND));
    }
}
