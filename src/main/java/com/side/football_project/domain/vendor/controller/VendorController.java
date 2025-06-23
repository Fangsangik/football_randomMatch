package com.side.football_project.domain.vendor.controller;

import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.vendor.dto.UpdateVendorRequestDto;
import com.side.football_project.domain.vendor.dto.UpdateVendorResponseDto;
import com.side.football_project.domain.vendor.dto.VendorRequestDto;
import com.side.football_project.domain.vendor.dto.VendorResponseDto;
import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.domain.vendor.service.VendorService;
import com.side.football_project.domain.vendor.type.VendorStatus;
import com.side.football_project.global.util.VendorDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    /**
     * 업체 로그인 페이지 리다이렉트
     */
    @GetMapping("/login")
    public String vendorLoginPage() {
        return "redirect:/vendor/login.html";
    }

    /**
     * 업체 가입 신청 페이지 리다이렉트
     */
    @GetMapping("/signup")
    public String vendorSignupPage() {
        return "redirect:/vendor/signup.html";
    }

    /**
     * 업체 가입 신청 (POST)
     */
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<VendorResponseDto> applyVendor(@RequestBody VendorRequestDto requestDto) {
        VendorResponseDto response = vendorService.applyVendor(requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 업체 로그인
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, String>> vendorLogin(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String token = vendorService.login(email, password);
        return ResponseEntity.ok(Map.of("accessToken", token));
    }

    @PatchMapping("/fix-vendor-info")
    @ResponseBody
    public ResponseEntity<UpdateVendorResponseDto> fixVendorInfo(@RequestBody UpdateVendorRequestDto requestDto,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        UpdateVendorResponseDto response = vendorService.updateVendor(requestDto, vendor);
        return ResponseEntity.ok(response);
    }

    /**
     * 내 업체 정보 조회
     */
    @GetMapping("/my-info")
    @ResponseBody
    public ResponseEntity<VendorResponseDto> getMyVendorInfo(@AuthenticationPrincipal UserDetails userDetails) {
        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        VendorResponseDto response = vendorService.getMyVendorInfo(vendor);
        return ResponseEntity.ok(response);
    }

    /**
     * 승인된 업체 목록 조회
     */
    @GetMapping("/approved")
    @ResponseBody
    public ResponseEntity<List<VendorResponseDto>> getApprovedVendors() {
        List<VendorResponseDto> vendors = vendorService.getVendorsByStatus(VendorStatus.APPROVED);
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/{vendorId:[0-9]+}")
    @ResponseBody
    public ResponseEntity<VendorResponseDto> getVendorById(@PathVariable Long stadiumId) {
        VendorResponseDto vendor = vendorService.getVendorById(stadiumId);
        return ResponseEntity.ok(vendor);
    }
}