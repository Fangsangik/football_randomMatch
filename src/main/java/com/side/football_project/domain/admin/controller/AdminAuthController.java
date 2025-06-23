package com.side.football_project.domain.admin.controller;

import com.side.football_project.domain.admin.entity.Admin;
import com.side.football_project.domain.admin.dto.AdminRequestDto;
import com.side.football_project.domain.admin.dto.AdminResponseDto;
import com.side.football_project.domain.admin.service.AdminService;
import com.side.football_project.domain.vendor.dto.VendorResponseDto;
import com.side.football_project.domain.vendor.service.VendorService;
import com.side.football_project.global.util.AdminDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminService adminService;
    private final VendorService vendorService;

    /**
     * 관리자 로그인 페이지 리다이렉트
     */
    @GetMapping("/login")
    public String adminLoginPage() {
        return "redirect:/admin/login.html";
    }

    /**
     * 관리자 가입 페이지 리다이렉트
     */
    @GetMapping("/register")
    public String adminRegisterPage() {
        return "redirect:/admin/register.html";
    }

    /**
     * 관리자 대시보드 페이지 리다이렉트
     */
    @GetMapping("/dashboard")
    public String adminDashboardPage() {
        return "redirect:/admin/dashboard.html";
    }

    /**
     * 관리자 토큰 검증
     */
    @GetMapping("/verify")
    @ResponseBody
    public ResponseEntity<Map<String, String>> verifyAdminToken(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("error", "유효하지 않은 토큰입니다."));
        }
        return ResponseEntity.ok(Map.of("status", "valid", "message", "유효한 관리자 토큰입니다."));
    }

    /**
     * 관리자 생성 (관리자 키 필요)
     */
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<AdminResponseDto> createAdmin(@RequestBody AdminRequestDto requestDto) {
        try {
            AdminResponseDto response = adminService.createAdmin(requestDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    /**
     * 관리자 로그인 (POST) - 관리자 키 방식
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String adminKey = request.get("adminKey");

        try {
            AdminResponseDto adminResponse = adminService.authenticateAdmin(email, password, adminKey);
            return ResponseEntity.ok(Map.of("accessToken", adminResponse.getAccessToken()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 업체 승인
     */
    @PostMapping("/vendor/{vendorId}/approve")
    @ResponseBody
    public ResponseEntity<Map<String, String>> approveVendor(@PathVariable Long vendorId,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        Admin admin = AdminDetailsUtil.getAdmin(userDetails);
        vendorService.approveVendor(vendorId, admin);
        return ResponseEntity.ok(Map.of("message", "업체가 승인되었습니다."));
    }

    /**
     * 업체 거절
     */
    @PostMapping("/vendor/{vendorId}/reject")
    @ResponseBody
    public ResponseEntity<Map<String, String>> rejectVendor(@PathVariable Long vendorId,
                                                            @RequestBody Map<String, String> request,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        Admin admin = AdminDetailsUtil.getAdmin(userDetails);
        String reason = request.get("reason");
        vendorService.rejectVendor(vendorId, reason, admin);
        return ResponseEntity.ok(Map.of("message", "업체 신청이 거절되었습니다."));
    }

    /**
     * 모든 업체 신청 목록 조회
     */
    @GetMapping("/vendor/applications")
    @ResponseBody
    public ResponseEntity<Page<VendorResponseDto>> getAllVendorApplications(@AuthenticationPrincipal UserDetails userDetails,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size) {
        Admin admin = AdminDetailsUtil.getAdmin(userDetails);
        Page<VendorResponseDto> applications = vendorService.getAllVendorApplications(admin, page, size);
        return ResponseEntity.ok(applications);
    }
}