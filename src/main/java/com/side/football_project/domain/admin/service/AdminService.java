package com.side.football_project.domain.admin.service;

import com.side.football_project.domain.admin.dto.AdminRequestDto;
import com.side.football_project.domain.admin.dto.AdminResponseDto;
import com.side.football_project.domain.admin.entity.Admin;
import com.side.football_project.domain.vendor.dto.VendorResponseDto;
import com.side.football_project.domain.vendor.type.VendorStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    AdminResponseDto createAdmin(AdminRequestDto adminRequestDto);
    AdminResponseDto authenticateAdmin(String email, String password, String adminKey);
    
    // 벤더 관리 메서드들
    void approveVendor(Long vendorId, Admin admin);
    void rejectVendor(Long vendorId, String reason, Admin admin);
    Page<VendorResponseDto> getAllVendorApplications(Admin admin, int page, int size);
    List<VendorResponseDto> getVendorsByStatus(VendorStatus status, Admin admin);
    List<VendorResponseDto> getApprovedVendors(Admin admin);
}
