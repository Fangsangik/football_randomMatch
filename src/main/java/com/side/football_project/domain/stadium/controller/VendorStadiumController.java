package com.side.football_project.domain.stadium.controller;

import com.side.football_project.domain.stadium.dto.FindMyStadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumRequestDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumUpdateDto;
import com.side.football_project.domain.stadium.service.VendorStadiumService;
import com.side.football_project.domain.vendor.dto.VendorResponseDto;
import com.side.football_project.domain.vendor.entity.Vendor;
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
@RequestMapping("/api/vendor/stadiums")
@RequiredArgsConstructor
public class VendorStadiumController {

    private final VendorStadiumService vendorStadiumService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<StadiumResponseDto> createStadium(@AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestBody StadiumRequestDto requestDto) {

        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        VendorResponseDto vendorDto = VendorResponseDto.toDto(vendor);
        StadiumResponseDto stadiumResponse = vendorStadiumService.createStadium(requestDto, vendorDto);

        return ResponseEntity.ok(stadiumResponse);
    }

    @GetMapping("my-stadiums")
    @ResponseBody
    public List<ResponseEntity<StadiumResponseDto>> getStadium(@AuthenticationPrincipal UserDetails userDetails){

        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        VendorResponseDto vendorDto = VendorResponseDto.toDto(vendor);
        List<StadiumResponseDto> stadiumResponse = vendorStadiumService.getMyStadiums(vendorDto);
        return stadiumResponse.stream()
                .map(ResponseEntity::ok)
                .toList();
    }

    @GetMapping("/my-stadiums/{stadiumId}")
    @ResponseBody
    public ResponseEntity<FindMyStadiumResponseDto> findMyStadiums(@AuthenticationPrincipal UserDetails userDetails,
                                                                   @PathVariable Long stadiumId) {

        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        VendorResponseDto vendorDto = VendorResponseDto.toDto(vendor);
        FindMyStadiumResponseDto stadiumResponse = vendorStadiumService.findMyStadiums(stadiumId, vendorDto);

        return ResponseEntity.ok(stadiumResponse);
    }

    @PatchMapping("/update")
    @ResponseBody
    public ResponseEntity<StadiumUpdateDto> updateStadium(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestBody StadiumUpdateDto requestDto) {

        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        VendorResponseDto vendorDto = VendorResponseDto.toDto(vendor);
        StadiumUpdateDto updatedStadium = vendorStadiumService.updateStadium(requestDto, vendorDto);

        return ResponseEntity.ok(updatedStadium);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteStadium(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody Long stadiumId) {

        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        VendorResponseDto vendorDto = VendorResponseDto.toDto(vendor);
        vendorStadiumService.deleteStadium(stadiumId, vendorDto);

        return ResponseEntity.noContent().build();
    }

    /**
     * 특정 경기장의 통계 정보 조회
     */
    @GetMapping("/statistics/{stadiumId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStadiumStatistics(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long stadiumId) {

        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        VendorResponseDto vendorDto = VendorResponseDto.toDto(vendor);
        Map<String, Object> statistics = vendorStadiumService.getStadiumStatistics(stadiumId, vendorDto);

        return ResponseEntity.ok(statistics);
    }

    /**
     * 벤더의 모든 경기장 통계 정보 조회
     */
    @GetMapping("/statistics")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllStadiumStatistics(
            @AuthenticationPrincipal UserDetails userDetails) {

        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        VendorResponseDto vendorDto = VendorResponseDto.toDto(vendor);
        Map<String, Object> statistics = vendorStadiumService.getAllStadiumStatistics(vendorDto);

        return ResponseEntity.ok(statistics);
    }
}
