package com.side.football_project.domain.stadium.controller;

import com.side.football_project.domain.stadium.dto.StadiumRequestDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumUpdateDto;
import com.side.football_project.domain.stadium.service.StadiumService;
import com.side.football_project.domain.stadium.service.VendorStadiumService;
import com.side.football_project.domain.stadium.service.VendorStadiumServiceImpl;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.global.util.UserDetailsUtil;
import com.side.football_project.global.util.VendorDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stadiums")
@RequiredArgsConstructor
public class StadiumController {
    
    private final VendorStadiumService vendorStadiumService;
    private final StadiumService stadiumService;

    /**
     * 경기장 생성 (Vendor)
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<StadiumResponseDto> createStadium(
            @RequestBody StadiumRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        StadiumResponseDto stadium = vendorStadiumService.createStadium(requestDto, vendor);
        return ResponseEntity.ok(stadium);
    }

    /**
     * 경기장 수정 (Vendor)
     */
    @PutMapping("/{stadiumId}")
    @ResponseBody
    public ResponseEntity<StadiumUpdateDto> updateStadium(
            @PathVariable Long stadiumId,
            @RequestBody StadiumUpdateDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        requestDto.setId(stadiumId); // URL의 ID를 DTO에 설정
        StadiumUpdateDto updatedStadium = vendorStadiumService.updateStadium(requestDto, vendor);
        return ResponseEntity.ok(updatedStadium);
    }

    /**
     * 경기장 삭제 (Vendor)
     */
    @DeleteMapping("/{stadiumId}")
    @ResponseBody
    public ResponseEntity<Void> deleteStadium(
            @PathVariable Long stadiumId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        vendorStadiumService.deleteStadium(stadiumId, vendor);
        return ResponseEntity.ok().build();
    }

    /**
     * 경기장 상세 조회 (공통)
     */
    @GetMapping("/{stadiumId}")
    @ResponseBody
    public ResponseEntity<StadiumResponseDto> getStadium(@PathVariable Long stadiumId) {
        StadiumResponseDto stadium = stadiumService.findById(stadiumId);
        return ResponseEntity.ok(stadium);
    }
}
