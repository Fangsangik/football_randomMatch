package com.side.football_project.domain.admin.service;


import com.side.football_project.domain.admin.dto.*;
import com.side.football_project.domain.admin.entity.Admin;
import com.side.football_project.domain.admin.repository.AdminRepository;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.domain.vendor.repository.VendorRepository;
import com.side.football_project.domain.vendor.service.VendorService;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.AdminErrorCode;
import com.side.football_project.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
}
