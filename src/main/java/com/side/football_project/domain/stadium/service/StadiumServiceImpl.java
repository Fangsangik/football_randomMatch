package com.side.football_project.domain.stadium.service;

import com.side.football_project.domain.stadium.dto.StadiumRequestDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumUpdateDto;
import com.side.football_project.domain.stadium.entity.Address;
import com.side.football_project.domain.stadium.entity.StadiumStatus;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.stadium.repository.StadiumRepository;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;
    private final UserService userService;

    @Transactional
    @Override
    public StadiumResponseDto createStadium(StadiumRequestDto requestDto, User user) {
        User findUser = userService.findUserById(user.getId());
        validateUserRole(findUser);

        Stadium stadium = Stadium.builder()
                .name(requestDto.getName())
                .status(StadiumStatus.AVAILABLE)
                .description(requestDto.getDescription())
                .capacity(requestDto.getCapacity())
                .user(findUser)
                .build();

        stadium.applyAddress(new Address(requestDto.getCity(), requestDto.getState(), requestDto.getPostalCode(), requestDto.getSpecificAddress()));

        stadiumRepository.save(stadium);
        return StadiumResponseDto.toEntity(stadium);
    }

    @Override
    public StadiumResponseDto findStadium(Long id) {
        Stadium stadium = stadiumRepository.findByIdOrElseThrow(id);
        return StadiumResponseDto.toEntity(stadium);
    }

    @Override
    public StadiumUpdateDto updateStadium(StadiumUpdateDto requestDto, User user) {
        User findUser = userService.findUserById(user.getId());
        Stadium stadium = stadiumRepository.findByIdOrElseThrow(requestDto.getId());

        validateUserRole(findUser);

        stadium = Stadium.builder()
                .name(requestDto.getName())
                .status(StadiumStatus.AVAILABLE)
                .description(requestDto.getDescription())
                .capacity(requestDto.getCapacity())
                .user(findUser)
                .build();

        stadium.applyAddress(new Address(requestDto.getCity(), requestDto.getState(), requestDto.getPostalCode(), requestDto.getSpecificAddress()));


        return StadiumUpdateDto.toEntity(stadium);
    }

    @Override
    public void deleteStadium(Long id, User user) {
        validateUserRole(user);
        Stadium stadium = stadiumRepository.findByIdOrElseThrow(id);
        stadiumRepository.deleteById(stadium.getId());
    }

    @Override
    public Stadium findByIdWithLock(Long id) {
        return stadiumRepository.findByIdWithLock(id).orElseThrow(() -> new IllegalStateException("찾을 수 없는 경기장입니다."));
    }

    @Override
    public Stadium findStadiumById(Long id) {
        return stadiumRepository.findByIdOrElseThrow(id);
    }

    private static void validateUserRole(User findUser) {
        if (UserRole.VENDOR.equals(findUser.getRole())) {
            throw new CustomException(UserErrorCode.NOT_ALLOWED);
        }
    }
}
