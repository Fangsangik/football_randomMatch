package com.side.football_project.global.util;

import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.global.security.auth.VendorUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

public class VendorDetailsUtil {
    
    /**
     * UserDetails에서 Vendor 엔티티 추출
     */
    public static Vendor getVendor(UserDetails userDetails) {
        if (userDetails instanceof VendorUserDetails) {
            return ((VendorUserDetails) userDetails).getVendor();
        }
        throw new RuntimeException("Vendor UserDetails가 아닙니다.");
    }
}