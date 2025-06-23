package com.side.football_project.global.security.auth;

import com.side.football_project.domain.admin.entity.Admin;
import com.side.football_project.domain.admin.repository.AdminRepository;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.AdminErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("adminUserDetailsService")
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {
    
    private final AdminRepository adminRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(AdminErrorCode.ADMIN_NOT_FOUND));
        
        return new AdminUserDetails(admin);
    }
}