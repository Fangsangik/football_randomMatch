package com.side.football_project.global.security.auth;

import com.side.football_project.domain.vendor.entity.Vendor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class VendorUserDetails implements UserDetails {
    
    private final Vendor vendor;
    
    public VendorUserDetails(Vendor vendor) {
        this.vendor = vendor;
    }
    
    @Override
    public String getUsername() {
        return vendor.getEmail();
    }
    
    @Override
    public String getPassword() {
        return vendor.getPassword();
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_VENDOR"));
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return vendor.getStatus() != null;
    }
}