package com.side.football_project.domain.vendor.service;

import com.side.football_project.domain.admin.entity.Admin;
import com.side.football_project.domain.vendor.dto.UpdateVendorRequestDto;
import com.side.football_project.domain.vendor.dto.UpdateVendorResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.stadium.repository.StadiumRepository;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.domain.vendor.dto.VendorRequestDto;
import com.side.football_project.domain.vendor.dto.VendorResponseDto;
import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.domain.vendor.repository.VendorRepository;
import com.side.football_project.domain.vendor.type.VendorStatus;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.AdminErrorCode;
import com.side.football_project.global.common.exception.type.VendorErrorCode;
import com.side.football_project.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VendorServiceImpl implements VendorService {
    
    private final VendorRepository vendorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final StadiumRepository stadiumRepository;

    @Transactional
    @Override
    public VendorResponseDto applyVendor(VendorRequestDto requestDto) {
        // 이메일 중복 체크
        if (vendorRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(VendorErrorCode.VENDOR_ALREADY_EXISTS);
        }
        
        // 사업자번호 중복 체크
        if (vendorRepository.existsByBusinessNumber(requestDto.getBusinessNumber())) {
            throw new CustomException(VendorErrorCode.BUSINESS_NUMBER_DUPLICATED);
        }
        
        // Vendor 생성 (독립적)
        Vendor vendor = Vendor.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .phoneNumber(requestDto.getPhoneNumber())
                .age(requestDto.getAge())
                .businessNumber(requestDto.getBusinessNumber())
                .companyName(requestDto.getCompanyName())
                .businessAddress(requestDto.getBusinessAddress())
                .description(requestDto.getDescription())
                .build();
        
        vendorRepository.save(vendor);
        
        return VendorResponseDto.toDto(vendor);
    }
    
    @Transactional
    @Override
    public String login(String email, String password) {
        Vendor vendor = vendorRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(VendorErrorCode.VENDOR_NOT_FOUND));
        
        if (!passwordEncoder.matches(password, vendor.getPassword())) {
            throw new CustomException(VendorErrorCode.VENDOR_ACCESS_DENIED);
        }
        
        return jwtProvider.createToken(email);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<VendorResponseDto> getVendorsByStatus(VendorStatus status) {
        return vendorRepository.findByStatusOrderByAppliedAtDesc(status).stream()
                .map(VendorResponseDto::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    @Override
    public VendorResponseDto getVendorById(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("업체를 찾을 수 없습니다."));
        
        return VendorResponseDto.toDto(vendor);
    }

    @Transactional(readOnly = true)
    @Override
    public VendorResponseDto getMyVendorInfo(Vendor vendor) {
        return VendorResponseDto.toDto(vendor);
    }
    

    // Admin 타입 오버로딩 메서드들
    @Transactional
    @Override
    public void approveVendor(Long vendorId, Admin admin) {
        validateAdmin(admin);

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("업체를 찾을 수 없습니다."));

        // 업체 승인
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

    // Paging 처리
    @Transactional(readOnly = true)
    @Override
    public Page<VendorResponseDto> getAllVendorApplications(Admin admin, int page, int size) {
        validateAdmin(admin);

        Pageable pageable = Pageable.ofSize(size).withPage(page);

        return vendorRepository.findAll(pageable)
                .map(VendorResponseDto::toDto);
    }

    @Transactional
    @Override
    public UpdateVendorResponseDto updateVendor(UpdateVendorRequestDto requestDto, Vendor vendor) {
        log.info("Vendor {} 정보 업데이트 요청", vendor.getId());

        // Vendor 정보 업데이트
        vendor.updateInfo(requestDto.getName(), requestDto.getAddress(), requestDto.getPhoneNumber(), requestDto.getDescription());
        // 저장
        vendorRepository.save(vendor);

        log.info("Vendor {} 정보 업데이트 완료", vendor.getId());
        return UpdateVendorResponseDto.toDto(vendor);
    }

    private void validateAdmin(Admin admin) {
        if (UserRole.VENDOR.equals(admin.getRole())) {
            throw new CustomException(AdminErrorCode.ADMIN_ACCESS_DENIED);
        }
    }

    private void validateVendor(Vendor vendor) {
        if (UserRole.VENDOR.equals(vendor.getRole())) {
            throw new CustomException(VendorErrorCode.VENDOR_NOT_FOUND);
        }
    }
}