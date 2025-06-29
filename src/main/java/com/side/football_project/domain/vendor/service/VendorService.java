package com.side.football_project.domain.vendor.service;

import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.vendor.dto.*;
import com.side.football_project.domain.vendor.entity.Vendor;

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
     * 특정 업체 조회
     */
    VendorResponseDto getVendorById(Long vendorId);
    
    /**
     * 내 업체 정보 조회 (Vendor 자체)
     */
    VendorResponseDto getMyVendorInfo(Vendor vendor);

    /**
     * 업체 정보 수정
     */
    UpdateVendorResponseDto updateVendor(UpdateVendorRequestDto requestDto, Vendor vendor);

    GetMyApplyStatusDto getMyApplyStatus(Vendor vendor);
}