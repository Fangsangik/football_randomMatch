package com.side.football_project.domain.stadium.controller;

import com.side.football_project.domain.stadium.dto.FindMyStadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumRequestDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumUpdateDto;
import com.side.football_project.domain.stadium.service.VendorStadiumService;
import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.global.util.VendorDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        StadiumResponseDto stadiumResponse = vendorStadiumService.createStadium(requestDto, vendor);

        return ResponseEntity.ok(stadiumResponse);
    }

    @GetMapping("my-stadiums")
    @ResponseBody
    public List<ResponseEntity<StadiumResponseDto>> getStadium(@AuthenticationPrincipal UserDetails userDetails){

        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        List<StadiumResponseDto> stadiumResponse = vendorStadiumService.getMyStadiums(vendor);
        return stadiumResponse.stream()
                .map(ResponseEntity::ok)
                .toList();
    }

    @GetMapping("/my-stadiums/{stadiumId}")
    @ResponseBody
    public ResponseEntity<FindMyStadiumResponseDto> findMyStadiums(@AuthenticationPrincipal UserDetails userDetails,
                                                                   @PathVariable Long stadiumId) {

        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        FindMyStadiumResponseDto stadiumResponse = vendorStadiumService.findMyStadiums(stadiumId, vendor);

        return ResponseEntity.ok(stadiumResponse);
    }

    @PatchMapping("/update")
    @ResponseBody
    public ResponseEntity<StadiumUpdateDto> updateStadium(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestBody StadiumUpdateDto requestDto) {

        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        StadiumUpdateDto updatedStadium = vendorStadiumService.updateStadium(requestDto, vendor);

        return ResponseEntity.ok(updatedStadium);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteStadium(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody Long stadiumId) {

        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        vendorStadiumService.deleteStadium(stadiumId, vendor);

        return ResponseEntity.noContent().build();
    }
}
