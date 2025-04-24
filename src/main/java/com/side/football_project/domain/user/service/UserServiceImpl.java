package com.side.football_project.domain.user.service;

import com.side.football_project.domain.user.dto.UserPasswordUpdateDto;
import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.repository.UserRepository;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.domain.user.type.UserTier;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.UserErrorCode;
import com.side.football_project.global.security.auth.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new CustomException(UserErrorCode.EMAIL_DUPLICATED);
        }

        User user = User.builder()
                .name(requestDto.getName())
                .phoneNumber(requestDto.getPhoneNumber())
                .age(requestDto.getAge())
                .role(requestDto.getUserRole())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .build();

        userRepository.save(user);

        return UserResponseDto.toDto(user);
    }

    @Override
    public void login(UserRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        validatePassword(requestDto.getPassword(), user.getPassword());
    }

    @Override
    public UserResponseDto findUser(Long userId, User user) {
        return UserResponseDto.toDto(user);
    }

    @Override
    public void updateName(UserRequestDto requestDto, User user) {

        User findUser = userRepository.findByIdOrElseThrow(user.getId());
        findUser.updateName(requestDto.getName());
    }

    @Override
    public void updatePassword(UserPasswordUpdateDto passwordUpdateDto, User user) {
        validatePassword(passwordUpdateDto.getCurrentPassword(), user.getPassword());

        if (passwordUpdateDto.getCurrentPassword().equals(passwordUpdateDto.getNewPassword())) {
            throw new CustomException(UserErrorCode.PASSWORD_DUPLICATED);
        } else if (!passwordUpdateDto.getNewPassword().equals(passwordUpdateDto.getNewPasswordConfirm())) {
            throw new CustomException(UserErrorCode.PASSWORD_NOT_CONFIRM);
        }

        user.updatePassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UserRequestDto requestDto, User user) {

        validatePassword(requestDto.getPassword(), user.getPassword());
        userRepository.delete(user);
    }

    /**
     * 유저의 평균 평점을 기반으로 UserTier 업데이트
     */
    @Transactional
    @Override
    public void updateUserTier(Long userId, User user) {
        if (!UserRole.ADMIN.equals(user.getRole())) {
            throw new CustomException(UserErrorCode.NOT_ALLOWED);
        }

        Double avgRate = userRepository.findAvgRateByUserId(userId);

        // 평점이 없는 경우 기본값 설정
        UserTier newTier = (avgRate == null) ? UserTier.ROOKIE : UserTier.determineTier(avgRate);
        user.updateTier(newTier);
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new CustomException(UserErrorCode.PASSWORD_MISMATCH);
        }
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findByIdOrElseThrow(userId);
    }
}
