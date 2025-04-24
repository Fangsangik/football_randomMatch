package com.side.football_project.domain.shorts.service;

import com.side.football_project.domain.shorts.dto.ShortsRequestDto;
import com.side.football_project.domain.shorts.dto.ShortsResponseDto;
import com.side.football_project.domain.shorts.entity.Shorts;
import com.side.football_project.domain.shorts.repository.ShortsRepository;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.ShortsErrorCode;
import com.side.football_project.global.common.exception.type.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShortsServiceImpl implements ShortsService {
    private final ShortsRepository shortsRepository;

    @Override
    public ShortsResponseDto createShorts(ShortsRequestDto requestDto, User user) {
        Shorts shorts = Shorts.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .url(requestDto.getUrl())
                .user(user)
                .build();

        shortsRepository.save(shorts);

        return ShortsResponseDto.toDto(shorts);
    }

    @Override
    public ShortsResponseDto findShorts(Long id) {
        Shorts shorts = shortsRepository.findByShortsIdOrElseThrow(id);
        return ShortsResponseDto.toDto(shorts);
    }

    @Override
    public Page<ShortsResponseDto> findShortsFeed(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Shorts> shortsPage = shortsRepository.findAll(pageable);
        return shortsPage.map(ShortsResponseDto::toDto);
    }

    @Override
    public ShortsResponseDto updateShorts(Long id, ShortsRequestDto requestDto, User user) {
        Shorts shorts = shortsRepository.findByShortsIdOrElseThrow(id);
        validateUser(user, shorts);
        shorts.updateShorts(requestDto.getTitle(), requestDto.getDescription());

        shortsRepository.save(shorts);

        return ShortsResponseDto.toDto(shorts);
    }

    @Override
    public void deleteShorts(Long id, User user) {
        Shorts shorts = shortsRepository.findByShortsIdOrElseThrow(id);
        validateUser(user, shorts);
        shortsRepository.delete(shorts);
    }

    private static void validateUser(User user, Shorts shorts) {
        if (!shorts.getUser().getId().equals(user.getId())) {
            throw new CustomException(UserErrorCode.NOT_ALLOWED);
        }
    }
}
