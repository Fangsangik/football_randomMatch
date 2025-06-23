package com.side.football_project.domain.user.service;

import com.side.football_project.domain.user.dto.AdminRequestDto;
import com.side.football_project.domain.user.dto.AdminResponseDto;
import com.side.football_project.domain.user.entity.Admin;
import com.side.football_project.domain.user.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final AdminRepository adminRepository;
    private final UserService userService;

    @Value("{admin.key}")
    private String adminKey;

    /**
     * createAdmin
     */
    @Override
    public AdminResponseDto createAdmin(AdminRequestDto adminRequestDto, Admin admin) {
         if (adminRepository.existsById(admin.getId())){
            throw new IllegalStateException("이미 존재하는 관리자입니다.");
         }

        Admin newAdmin = Admin.builder()
                .adminKey(adminKey)
                .email(adminRequestDto.getEmail())
                .password(adminRequestDto.getPassword())
                .build();

        adminRepository.save(newAdmin);
        return AdminResponseDto.toEntity(newAdmin);
    }

    public AcceptVenderResponseDto acceptVender (AcceptVenderRequesetDto acceptVenderRequestDto, Admin admin) {
        // 관리자 권한 확인
        userService.checkAdminRole(admin);

        // 벤더 승인 로직 구현
        // 예시: 벤더의 상태를 승인으로 변경하고 저장
        Vender vender = venderRepository.findById(acceptVenderRequestDto.getVenderId())
                .orElseThrow(() -> new CustomException(VenderErrorCode.VENDER_NOT_FOUND));

        vender.setStatus(VenderStatus.ACCEPTED);
        venderRepository.save(vender);

        return AcceptVenderDto.toEntity(vender);
    }
}
