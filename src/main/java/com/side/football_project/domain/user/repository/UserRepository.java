package com.side.football_project.domain.user.repository;

import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.UserErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("select avg(mu.rate) from MatchUser mu where mu.user.id = :userId")
    Double findAvgRateByUserId(Long userId);


    default User findByIdOrElseThrow(Long userId) {
        return findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    List<User> findByTier(String tier);
}
