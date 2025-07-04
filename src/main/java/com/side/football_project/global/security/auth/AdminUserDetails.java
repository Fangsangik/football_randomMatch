package com.side.football_project.global.security.auth;

import com.side.football_project.domain.admin.entity.Admin;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class AdminUserDetails implements UserDetails {
    
    private final Admin admin;
    
    public AdminUserDetails(Admin admin) {
        this.admin = admin;
    }
    
    @Override
    public String getUsername() {
        return admin.getEmail();
    }
    
    @Override
    public String getPassword() {
        return admin.getPassword();
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
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
        return true;
    }
}