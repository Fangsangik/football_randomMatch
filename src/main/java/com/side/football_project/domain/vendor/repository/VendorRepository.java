package com.side.football_project.domain.vendor.repository;

import com.side.football_project.domain.vendor.dto.UpdateVendorRequestDto;
import com.side.football_project.domain.vendor.dto.UpdateVendorResponseDto;
import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.domain.vendor.type.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    
    Optional<Vendor> findByEmail(String email);
    
    Optional<Vendor> findByBusinessNumber(String businessNumber);
    
    List<Vendor> findByStatus(VendorStatus status);
    
    List<Vendor> findByStatusOrderByAppliedAtDesc(VendorStatus status);
    
    boolean existsByEmail(String email);
    
    boolean existsByBusinessNumber(String businessNumber);

}