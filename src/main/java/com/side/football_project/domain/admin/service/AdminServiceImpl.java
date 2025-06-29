package com.side.football_project.domain.admin.service;


import com.side.football_project.domain.admin.dto.*;
import com.side.football_project.domain.admin.entity.Admin;
import com.side.football_project.domain.admin.repository.AdminRepository;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.domain.vendor.dto.VendorResponseDto;
import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.domain.vendor.repository.VendorRepository;
import com.side.football_project.domain.vendor.service.VendorService;
import com.side.football_project.domain.vendor.type.VendorStatus;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.AdminErrorCode;
import com.side.football_project.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final UserService userService;
    private final VendorService vendorService;
    private final VendorRepository vendorRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.key:FOOTBALL_ADMIN_MASTER_KEY_2025}")
    private String masterAdminKey;

    /**
     * createAdmin
     */
    @Override
    @Transactional
    public AdminResponseDto createAdmin(AdminRequestDto adminRequestDto) {
        // 관리자 키 검증
        if (!masterAdminKey.equals(adminRequestDto.getAdminKey())) {
            throw new CustomException(AdminErrorCode.INVALID_ADMIN_KEY);
        }
        
        // 이메일 중복 확인
        if (adminRepository.existsByEmail(adminRequestDto.getEmail())) {
            throw new CustomException(AdminErrorCode.ADMIN_EMAIL_DUPLICATED);
        }

        Admin newAdmin = Admin.builder()
                .adminKey(masterAdminKey)
                .email(adminRequestDto.getEmail())
                .password(passwordEncoder.encode(adminRequestDto.getPassword()))
                .build();

        adminRepository.save(newAdmin);
        return AdminResponseDto.toEntity(newAdmin);
    }

    /**
     * 관리자 인증 (이메일, 비밀번호, 관리자 키)
     */
    @Override
    @Transactional(readOnly = true)
    public AdminResponseDto authenticateAdmin(String email, String password, String adminKey) {
        // 관리자 키 검증
        if (!masterAdminKey.equals(adminKey)) {
            throw new CustomException(AdminErrorCode.INVALID_ADMIN_KEY);
        }

        // 이메일로 관리자 조회
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(AdminErrorCode.ADMIN_NOT_FOUND));

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new CustomException(AdminErrorCode.INVALID_PASSWORD);
        }

        // JWT 토큰 생성
        String accessToken = jwtProvider.createToken(email);

        return AdminResponseDto.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .accessToken(accessToken)
                .build();
    }

    // 벤더 관리 메서드들
    @Transactional
    @Override
    public void approveVendor(Long vendorId, Admin admin) {
        validateAdmin(admin);

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("업체를 찾을 수 없습니다."));

        vendor.approve();
        vendorRepository.save(vendor);
    }

    @Transactional
    @Override
    public void rejectVendor(Long vendorId, String reason, Admin admin) {
        validateAdmin(admin);

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("업체를 찾을 수 없습니다."));

        vendor.reject(reason);
        vendorRepository.save(vendor);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<VendorResponseDto> getAllVendorApplications(Admin admin, int page, int size) {
        validateAdmin(admin);

        Pageable pageable = Pageable.ofSize(size).withPage(page);

        return vendorRepository.findAll(pageable)
                .map(VendorResponseDto::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VendorResponseDto> getVendorsByStatus(VendorStatus status, Admin admin) {
        validateAdmin(admin);
        
        return vendorRepository.findByStatusOrderByAppliedAtDesc(status).stream()
                .map(VendorResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<VendorResponseDto> getApprovedVendors(Admin admin) {
        validateAdmin(admin);
        
        return vendorRepository.findByStatusOrderByAppliedAtDesc(VendorStatus.APPROVED).stream()
                .map(VendorResponseDto::toDto)
                .collect(Collectors.toList());
    }

    private void validateAdmin(Admin admin) {
        if (UserRole.VENDOR.equals(admin.getRole())) {
            throw new CustomException(AdminErrorCode.ADMIN_ACCESS_DENIED);
        }
    }
}
