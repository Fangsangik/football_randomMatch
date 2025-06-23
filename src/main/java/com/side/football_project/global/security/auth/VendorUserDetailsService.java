package com.side.football_project.global.security.auth;

import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.domain.vendor.repository.VendorRepository;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.VendorErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("vendorUserDetailsService")
@RequiredArgsConstructor
public class VendorUserDetailsService implements UserDetailsService {
    
    private final VendorRepository vendorRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Vendor vendor = vendorRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(VendorErrorCode.VENDOR_NOT_FOUND));
        
        return new VendorUserDetails(vendor);
    }
}