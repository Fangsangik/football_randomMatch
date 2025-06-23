package com.side.football_project.global.security.filter;

import com.side.football_project.global.security.auth.AdminUserDetailsService;
import com.side.football_project.global.security.auth.CustomUserDetailsService;
import com.side.football_project.global.security.auth.VendorUserDetailsService;
import com.side.football_project.global.security.jwt.BlackListToken;
import com.side.football_project.global.security.jwt.JwtProvider;
import com.side.football_project.global.util.UrlUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final VendorUserDetailsService vendorUserDetailsService;
    private final AdminUserDetailsService adminUserDetailsService;
    private final BlackListToken blackListToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        log.info("요청 URI: {}, Method: {}", requestURI, method);
        
        if (UrlUtil.isWhiteListed(requestURI, method)){
            log.info("WHITE_LIST에 포함된 경로: {} [{}]", requestURI, method);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("인증이 필요한 경로: {}", requestURI);
        String token = getTokenFromRequest(request);
        if (token == null) {
            log.error("토큰이 없습니다. URI: {}", requestURI);
            // HTML 페이지 요청인 경우 로그인 페이지로 리다이렉트
            if (UrlUtil.isHtmlPageRequest(requestURI)) {
                response.sendRedirect("/auth/login?redirect=" + requestURI);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 없습니다.");
            }
            return;
        }
        
        if (!jwtProvider.validateAccessToken(token)) {
            log.error("유효하지 않은 토큰입니다. URI: {}", requestURI);
            // HTML 페이지 요청인 경우 로그인 페이지로 리다이렉트
            if (UrlUtil.isHtmlPageRequest(requestURI)) {
                response.sendRedirect("/auth/login?redirect=" + requestURI);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            }
            return;
        }

        if (blackListToken.isBlackListed(token)) {
            log.error("블랙리스트에 등록된 토큰입니다. URI: {}", requestURI);
            // HTML 페이지 요청인 경우 로그인 페이지로 리다이렉트
            if (UrlUtil.isHtmlPageRequest(requestURI)) {
                response.sendRedirect("/auth/login?redirect=" + requestURI);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "블랙리스트에 등록된 토큰입니다.");
            }
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("SecurityContext에 인증 정보 없음. 사용자 인증 진행");
            authenticate(request, token);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(HttpServletRequest request, String token) {
        String email = jwtProvider.getUsername(token);
        String requestURI = request.getRequestURI();

        try {
            UserDetails userDetails = null;
            
            // 요청 URI에 따라 적절한 UserDetailsService 선택
            if (UrlUtil.isAdminPath(requestURI)) {
                userDetails = adminUserDetailsService.loadUserByUsername(email);
            } else if (UrlUtil.isVendorPath(requestURI)) {
                userDetails = vendorUserDetailsService.loadUserByUsername(email);
            } else {
                // 일반 사용자 또는 모든 서비스에서 차례로 시도
                userDetails = loadUserFromAnyService(email);
            }
            
            if (userDetails != null) {
                setAuthentication(request, userDetails);
            }
        } catch (UsernameNotFoundException e) {
            log.error("사용자를 찾을 수 없습니다: {}", email);
        }
    }
    
    /**
     * 모든 UserDetailsService에서 사용자를 찾아보는 메서드
     */
    private UserDetails loadUserFromAnyService(String email) {
        // 1. 일반 사용자부터 시도
        try {
            return customUserDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            log.debug("일반 사용자에서 찾을 수 없음: {}", email);
        }
        
        // 2. Vendor 시도
        try {
            return vendorUserDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            log.debug("Vendor에서 찾을 수 없음: {}", email);
        }
        
        // 3. Admin 시도
        try {
            return adminUserDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            log.debug("Admin에서 찾을 수 없음: {}", email);
        }
        
        throw new UsernameNotFoundException("어떤 서비스에서도 사용자를 찾을 수 없습니다: " + email);
    }

    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        final String headerPrefix = "Bearer ";
        if (bearerToken != null && bearerToken.startsWith(headerPrefix)) {
            return bearerToken.substring(headerPrefix.length()).trim();
        }

        return null;
    }

}
