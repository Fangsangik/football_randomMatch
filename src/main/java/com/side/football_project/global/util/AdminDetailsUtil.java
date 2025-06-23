package com.side.football_project.global.util;

import com.side.football_project.domain.admin.entity.Admin;
import com.side.football_project.global.security.auth.AdminUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

public class AdminDetailsUtil {
    
    /**
     * UserDetails에서 Admin 엔티티 추출
     */
    public static Admin getAdmin(UserDetails userDetails) {
        if (userDetails instanceof AdminUserDetails) {
            return ((AdminUserDetails) userDetails).getAdmin();
        }
        throw new RuntimeException("Admin UserDetails가 아닙니다.");
    }
}