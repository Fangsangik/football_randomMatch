package com.side.football_project.domain.user.service;

import com.side.football_project.domain.admin.dto.AdminRequestDto;
import com.side.football_project.domain.admin.dto.AdminResponseDto;
import com.side.football_project.domain.admin.domain.Admin;

public interface AdminService {
    AdminResponseDto createAdmin(AdminRequestDto adminRequestDto, Admin admin);
}
