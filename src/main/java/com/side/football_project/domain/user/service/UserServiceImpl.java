package com.side.football_project.domain.user.service;

import com.side.football_project.domain.admin.entity.Admin;
import com.side.football_project.domain.user.dto.LoginRequestDto;
import com.side.football_project.domain.user.dto.LoginResponseDto;
import com.side.football_project.domain.user.dto.UserPasswordUpdateDto;
import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.repository.UserRepository;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.domain.user.type.UserTier;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.UserErrorCode;
import com.side.football_project.global.security.jwt.JwtProvider;
import com.side.football_project.global.security.jwt.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshToken refreshToken;
    private final AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new CustomException(UserErrorCode.EMAIL_DUPLICATED);
        }

        // 비밀번호 확인 검증
        if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
            throw new CustomException(UserErrorCode.PASSWORD_MISMATCH);
        }

        User user = User.builder()
                .name(requestDto.getName())
                .phoneNumber(requestDto.getPhoneNumber())
                .age(requestDto.getAge())
                .role(UserRole.NORMAL)  // 일반 사용자는 항상 NORMAL 역할로 고정
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .build();

        userRepository.save(user);

        return UserResponseDto.toDto(user);
    }

    @Transactional
    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증만 수행 (AuthenticationManager 제거)
        validatePassword(requestDto.getPassword(), user.getPassword());

        String accessToken = jwtProvider.createToken(requestDto.getEmail());
        String refreshTokenValue = jwtProvider.createRefreshToken(requestDto.getEmail());
        Long expiresIn = jwtProvider.getExpirationTime();

        // Redis에 리프레시 토큰 저장
        refreshToken.addRefreshToken(requestDto.getEmail(), refreshTokenValue, expiresIn);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .message("로그인 성공")
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().toString())
                .tier(user.getTier().toString())
                .build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String accessToken = header.substring(7);
            long expiresIn = jwtProvider.getTokenExpirationTime(accessToken);

            if (expiresIn <= 0) {
                expiresIn = 5 * 60 * 1000; // 기본 만료 시간 5분 설정
            }

            jwtProvider.addToBlackList(accessToken, expiresIn);
        } else {
            throw new CustomException(UserErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    public UserResponseDto findUser(Long userId, User user) {
        return UserResponseDto.toDto(user);
    }

    @Transactional
    @Override
    public void updateName(UserRequestDto requestDto, User user) {

        User findUser = userRepository.findByIdOrElseThrow(user.getId());
        findUser.updateName(requestDto.getName());
    }

    @Transactional
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

    @Transactional
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

    @Override
    public void checkAdminRole(Admin admin) {
        if (!UserRole.ADMIN.equals(admin.getRole())) {
            throw new CustomException(UserErrorCode.NOT_ALLOWED);
        }
    }

    @Transactional
    @Override
    public LoginResponseDto refreshToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            throw new CustomException(UserErrorCode.INVALID_TOKEN);
        }

        String refreshTokenValue = header.substring(7);
        
        // 리프레시 토큰 검증
        if (!jwtProvider.validateRefreshToken(refreshTokenValue)) {
            throw new CustomException(UserErrorCode.INVALID_TOKEN);
        }

        // 리프레시 토큰에서 사용자 정보 추출
        String email = jwtProvider.getUsername(refreshTokenValue);
        User user = userRepository.findByEmailOrElseThrow(email);

        // 새로운 액세스 토큰과 리프레시 토큰 생성
        String newAccessToken = jwtProvider.createToken(user.getEmail());
        String newRefreshTokenValue = jwtProvider.createRefreshToken(user.getEmail());

        // Redis에 새로운 리프레시 토큰 저장
        refreshToken.addRefreshToken(user.getEmail(), newRefreshTokenValue, jwtProvider.getExpirationTime());

        // 기존 리프레시 토큰을 블랙리스트에 추가 (선택사항)
        long remainingTime = jwtProvider.getTokenExpirationTime(refreshTokenValue);
        if (remainingTime > 0) {
            jwtProvider.addToBlackList(refreshTokenValue, remainingTime);
        }

        return LoginResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenValue)
                .tokenType("Bearer")
                .expiresIn(jwtProvider.getExpirationTime())
                .message("토큰 갱신 성공")
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().toString())
                .tier(user.getTier().toString())
                .build();
    }
}
