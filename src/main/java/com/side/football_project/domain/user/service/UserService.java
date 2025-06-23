package com.side.football_project.domain.user.service;

import com.side.football_project.domain.admin.entity.Admin;
import com.side.football_project.domain.user.dto.LoginRequestDto;
import com.side.football_project.domain.user.dto.LoginResponseDto;
import com.side.football_project.domain.user.dto.UserPasswordUpdateDto;
import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto requestDto);

    LoginResponseDto login(LoginRequestDto requestDto);

    UserResponseDto findUser(Long userId, User user);

    void updateName(UserRequestDto requestDto, User user);

    void updatePassword(UserPasswordUpdateDto passwordUpdateDto, User user);

    void deleteUser(UserRequestDto requestDto, User user);

    void updateUserTier(Long userId, User user);

    User findUserById(Long userId);

    List<User> getAllUser();

    void checkAdminRole(Admin admin);

    void logout(HttpServletRequest request);
    
    LoginResponseDto refreshToken(HttpServletRequest request);
}
