package com.side.football_project.domain.admin.controller;

import com.side.football_project.domain.admin.dto.*;
import com.side.football_project.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<AdminResponseDto> createAdmin(@RequestBody AdminRequestDto adminRequestDto) {
        AdminResponseDto responseDto = adminService.createAdmin(adminRequestDto);
        return ResponseEntity.ok(responseDto);
    }
}
