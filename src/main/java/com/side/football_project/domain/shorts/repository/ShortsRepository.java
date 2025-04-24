package com.side.football_project.domain.shorts.repository;

import com.side.football_project.domain.shorts.entity.Shorts;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.ShortsErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortsRepository extends JpaRepository<Shorts, Long> {

    default Shorts findByShortsIdOrElseThrow(Long shortsId) {
        return findById(shortsId).orElseThrow(() -> new CustomException(ShortsErrorCode.SHORTS_NOT_FOUND));
    }
}
