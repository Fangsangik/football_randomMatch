package com.side.football_project.global.util;

import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;

/**
 * URL 관련 유틸리티 클래스
 * WHITE_LIST 관리 및 URL 패턴 매칭 기능 제공
 */
public class UrlUtil {
    
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    
    /**
     * 인증이 필요하지 않은 WHITE_LIST 경로들
     * 모든 보안 관련 설정에서 공통으로 사용
     */
    public static final String[] WHITE_LIST_PATHS = {
            // 기본 경로
            "/", 
            
            // API 경로
            "/auth/**", 
            "/api/chat/**", 
            "/api/home/**",
            "/api/stadiums/search",
            "/api/stadiums/nearby",
            "/api/stadiums/*/available-times",
            "/api/stadiums/*",
            
            // 회원가입/로그인 관련
            "/vendor/signup", 
            "/admin/login", 
            "/admin/register", 
            "/admin/create", 
            "/vendor/login",
            
            // 정적 HTML 페이지 (인증 불필요)
            "/index.html", 
            "/teams/index.html", 
            "/teams/detail.html", 
            "/matches/index.html", 
            "/shorts/feed.html",
            "/admin/login.html", 
            "/admin/dashboard.html",
            "/auth/signup.html", 
            "/auth/login.html", 
            "/vendor/signup.html", 
            "/vendor/login.html",
            "/vendor/dashboard.html",
            "/vendor/stadium-management.html",
            "/vendor/stadium-list.html",
            "/vendor/reservation-management.html",
            "/admin/register.html",
            "/user/stadium-search.html",
            "/user/reservation.html",
            "/user/my-reservations.html",
            
            // 정적 리소스
            "/css/**", 
            "/js/**", 
            "/images/**", 
            "/favicon.ico", 
            "/static/**", 
            "/.well-known/**",
            
            // Swagger (개발용)
            "/swagger-ui.html", 
            "/swagger-ui/**", 
            "/v3/api-docs/**",
            
            // 기타
            "/kakao/callback",
            "/admin/fix-team-counts", 
            "/teams/fix-data"
    };
    
    /**
     * WHITE_LIST를 List로 반환
     */
    public static List<String> getWhiteListAsList() {
        return Arrays.asList(WHITE_LIST_PATHS);
    }
    
    /**
     * 주어진 URL이 WHITE_LIST에 포함되는지 확인
     * @param requestUri 요청 URI
     * @param method HTTP 메서드 (선택사항)
     * @return WHITE_LIST 포함 여부
     */
    public static boolean isWhiteListed(String requestUri, String method) {
        if (requestUri == null) {
            return false;
        }
        
        // 기본 WHITE_LIST 체크
        for (String pattern : WHITE_LIST_PATHS) {
            if (pathMatcher.match(pattern, requestUri)) {
                return true;
            }
        }
        
        // GET 요청만 허용하는 특별 경로들
        if ("GET".equals(method)) {
            return requestUri.equals("/teams") || 
                   requestUri.equals("/matches") || 
                   requestUri.equals("/shorts") ||
                   requestUri.matches("/teams/\\d+") ||                    // 팀 상세 조회
                   requestUri.matches("/teams/\\d+/members") ||            // 팀 멤버 목록 조회
                   requestUri.equals("/teams/list");                       // 팀 목록 조회
        }
        
        return false;
    }
    
    /**
     * 주어진 URL이 WHITE_LIST에 포함되는지 확인 (HTTP 메서드 무관)
     * @param requestUri 요청 URI
     * @return WHITE_LIST 포함 여부
     */
    public static boolean isWhiteListed(String requestUri) {
        return isWhiteListed(requestUri, null);
    }
    
    /**
     * 관리자 관련 경로인지 확인
     * @param requestUri 요청 URI
     * @return 관리자 경로 여부
     */
    public static boolean isAdminPath(String requestUri) {
        return requestUri != null && requestUri.startsWith("/admin");
    }
    
    /**
     * Vendor 관련 경로인지 확인
     * @param requestUri 요청 URI
     * @return Vendor 경로 여부
     */
    public static boolean isVendorPath(String requestUri) {
        return requestUri != null && requestUri.startsWith("/vendor");
    }
    
    /**
     * HTML 페이지 요청인지 확인
     * @param requestUri 요청 URI
     * @return HTML 페이지 요청 여부
     */
    public static boolean isHtmlPageRequest(String requestUri) {
        if (requestUri == null) {
            return false;
        }
        
        // HTML 파일 요청이거나 브라우저의 HTML 요청인 경우
        return requestUri.endsWith(".html") || 
               requestUri.equals("/") || 
               requestUri.equals("/teams") || 
               requestUri.equals("/matches") || 
               requestUri.equals("/shorts") ||
               (!requestUri.contains(".") && !requestUri.startsWith("/api"));
    }
}