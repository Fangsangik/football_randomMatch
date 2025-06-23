package com.side.football_project.domain.shorts.controller;

import com.side.football_project.domain.shorts.dto.ShortsRequestDto;
import com.side.football_project.domain.shorts.dto.ShortsResponseDto;
import com.side.football_project.domain.shorts.service.ShortsService;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.common.service.FileUploadService;
import com.side.football_project.global.util.UserDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shorts")
public class ShortsController {
    private final ShortsService shortsService;
    private final FileUploadService fileUploadService;

    /**
     * 숏츠 생성
     *
     * @param file        숏츠 파일
     * @param title       숏츠 제목
     * @param description 숏츠 설명
     * @return 생성된 숏츠의 정보 {@link ShortsResponseDto}
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<ShortsResponseDto> createShorts(@RequestPart("file") MultipartFile file,
                                                          @RequestParam("title") String title,
                                                          @RequestParam("description") String description,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        String url = fileUploadService.uploadFile(file);
        ShortsRequestDto requestDto = new ShortsRequestDto(title, description, url);
        return ResponseEntity.ok(shortsService.createShorts(requestDto, user));
    }

    /**
     * 숏츠 조회
     *
     * @param shortsId 숏츠 ID
     * @return 숏츠 정보
     */
    @GetMapping("/{shortsId:[0-9]+}")
    @ResponseBody
    public ResponseEntity<ShortsResponseDto> findShorts(@PathVariable Long shortsId) {
        return ResponseEntity.ok(shortsService.findShorts(shortsId));
    }

    /**
     * 숏츠 피드 조회 (무한 스크롤용)
     *
     * @return 숏츠 리스트
     */
    @GetMapping("/feed")
    @ResponseBody
    public ResponseEntity<Page<ShortsResponseDto>> findShortsFeed(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Page<ShortsResponseDto> shorts = shortsService.findShortsFeed(page, size);
        return ResponseEntity.ok(shorts);
    }

    /**
     * 숏츠 수정
     *
     * @param shortsId   숏츠 ID
     * @param requestDto 숏츠 수정에 필요한 정보 {@link ShortsRequestDto}
     * @return 수정된 숏츠의 정보
     */
    @PatchMapping("/{shortsId:[0-9]+}")
    @ResponseBody
    public ResponseEntity<ShortsResponseDto> updateShorts(@PathVariable Long shortsId,
                                                          @RequestBody ShortsRequestDto requestDto,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        return ResponseEntity.ok(shortsService.updateShorts(shortsId, requestDto, user));
    }

    /**
     * 숏츠 삭제
     *
     * @param shortsId 숏츠 ID
     */
    @DeleteMapping("/{shortsId:[0-9]+}")
    @ResponseBody
    public ResponseEntity<Void> deleteShorts(@PathVariable Long shortsId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        shortsService.deleteShorts(shortsId, user);
        return ResponseEntity.noContent().build();
    }
}