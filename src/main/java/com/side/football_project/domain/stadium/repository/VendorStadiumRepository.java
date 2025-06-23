package com.side.football_project.domain.stadium.repository;

import com.side.football_project.domain.stadium.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VendorStadiumRepository extends JpaRepository<Stadium, Long> {

    @Query("SELECT s FROM Stadium s WHERE s.id = ?1 AND s.vendor.id = ?2")
    Optional<Stadium> findByIdAndVendor(Long id, Long vendorId);
}
