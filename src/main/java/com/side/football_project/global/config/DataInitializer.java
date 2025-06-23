package com.side.football_project.global.config;

import com.side.football_project.domain.admin.entity.Admin;
import com.side.football_project.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${admin.key:FOOTBALL_ADMIN_MASTER_KEY_2025}")
    private String masterAdminKey;
    
    @Value("${admin.default.email:admin@football.com}")
    private String defaultAdminEmail;
    
    @Value("${admin.default.password:Football@Admin2025}")
    private String defaultAdminPassword;

    @Override
    public void run(String... args) throws Exception {
        initializeDefaultAdmin();
    }

    private void initializeDefaultAdmin() {
        if (!adminRepository.existsByEmail(defaultAdminEmail)) {
            Admin defaultAdmin = Admin.builder()
                    .email(defaultAdminEmail)
                    .password(passwordEncoder.encode(defaultAdminPassword))
                    .adminKey(masterAdminKey)
                    .build();
            
            adminRepository.save(defaultAdmin);
            
            log.info("=== 기본 관리자 계정이 생성되었습니다 ===");
            log.info("이메일: {}", defaultAdminEmail);
            log.info("비밀번호: {}", defaultAdminPassword);
            log.info("관리자 키: {}", masterAdminKey);
            log.info("======================================");
        } else {
            log.info("기본 관리자 계정이 이미 존재합니다: {}", defaultAdminEmail);
        }
    }
}