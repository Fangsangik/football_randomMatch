package com.side.football_project.global.security.filter;

import com.side.football_project.global.security.jwt.JwtProvider;
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
    private final UserDetailsService userDetailsService;
    private final List<String> WHITE_LIST = List.of( "/auth/**", "/api/home/**",
            "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/api/chat/**", "/kakao/callback");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (isWhiteListed(requestURI)){
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);
        if (token == null || !jwtProvider.validateAccessToken(token)) {
            log.error("유효하지 않은 토큰입니다.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
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

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            setAuthentication(request, userDetails);
        } catch (UsernameNotFoundException e) {
            log.error("사용자를 찾을 수 없습니다.");
        }
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

    private boolean isWhiteListed(String requestURI) {
        return WHITE_LIST.stream().anyMatch(uri -> uri.equals(requestURI) || requestURI.matches(uri.replace("**", ".*")));
    }
}
