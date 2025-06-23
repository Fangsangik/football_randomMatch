package com.side.football_project.domain.vendor.service;

import com.side.football_project.domain.admin.entity.Admin;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.vendor.dto.UpdateVendorRequestDto;
import com.side.football_project.domain.vendor.dto.UpdateVendorResponseDto;
import com.side.football_project.domain.vendor.dto.VendorRequestDto;
import com.side.football_project.domain.vendor.dto.VendorResponseDto;
import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.domain.vendor.type.VendorStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VendorService {
    
    /**
     * 업체 가입 신청 (독립적)
     */
    VendorResponseDto applyVendor(VendorRequestDto requestDto);
    
    /**
     * 업체 로그인
     */
    String login(String email, String password);
    
    /**
     * 업체 승인 (관리자용)
     */
    void approveVendor(Long vendorId, Admin admin);
    
    /**
     * 업체 거절 (관리자용)
     */
    void rejectVendor(Long vendorId, String reason, Admin admin);
    
    /**
     * 모든 업체 신청 목록 조회 (관리자용)
     */
    Page<VendorResponseDto> getAllVendorApplications(Admin admin, int page, int size);
    
    /**
     * 상태별 업체 목록 조회
     */
    List<VendorResponseDto> getVendorsByStatus(VendorStatus status);
    
    /**
     * 특정 업체 조회
     */
    VendorResponseDto getVendorById(Long vendorId);
    
    /**
     * 내 업체 정보 조회 (Vendor 자체)
     */
    VendorResponseDto getMyVendorInfo(Vendor vendor);


    UpdateVendorResponseDto updateVendor(UpdateVendorRequestDto requestDto, Vendor vendor);
}