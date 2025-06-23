package com.side.football_project.domain.admin.service;

import com.side.football_project.domain.admin.dto.AdminRequestDto;
import com.side.football_project.domain.admin.dto.AdminResponseDto;

public interface AdminService {
    AdminResponseDto createAdmin(AdminRequestDto adminRequestDto);
    AdminResponseDto authenticateAdmin(String email, String password, String adminKey);
}
