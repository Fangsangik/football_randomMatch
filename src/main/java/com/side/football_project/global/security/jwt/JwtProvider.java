package com.side.football_project.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration-time}")
    private long expirationTime;

    @Value("${jwt.refresh-expiration-time}")
    private long refreshExpirationTime;

    private final BlackListToken blackListToken;

    // JWT Access Token 생성
   public String createToken(String username) {
       Date date = new Date();
       Date expirationDate = new Date(date.getTime() + expirationTime);

       return Jwts.builder()
               .subject(username)
               .issuedAt(date)
               .expiration(expirationDate)
               .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
               .compact();
   }

   public String getUsername(String token) {
       return getClaims(token).getSubject();
   }

   public long getExpirationTime() {
       return expirationTime;
   }

   public boolean validateAccessToken(String token) {
       log.info("토큰 검증 시작. 토큰 길이: {}", token != null ? token.length() : 0);
       
       try {
           Claims claims = getClaims(token);
           Date expiration = claims.getExpiration();
           Date now = new Date();
           boolean isValid = !expiration.before(now);
           
           log.info("토큰 만료시간: {}, 현재시간: {}, 유효함: {}", expiration, now, isValid);
           return isValid;
       } catch (ExpiredJwtException e) {
           log.error("JWT token is expired: {}", e.getMessage());
           return false;
       } catch (JwtException e) {
           log.error("Invalid JWT token: {}", e.getMessage());
           return false;
       } catch (Exception e) {
           log.error("토큰 검증 중 예외 발생: {}", e.getMessage());
           return false;
       }
   }

    private Claims getClaims(String token) {
        if (!StringUtils.hasText(token)) {
            throw new MalformedJwtException("토큰이 비어 있습니다.");
        }

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 리프레시 토큰 생성 (Redis 저장은 서비스 레이어에서 처리)
    public String createRefreshToken(String username) {
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + refreshExpirationTime);

        return Jwts.builder()
                .subject(username)
                .issuedAt(date)
                .expiration(expirationDate)
                .claim("type", "refresh")
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    // 리프레시 토큰 검증
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = getClaims(token);
            String tokenType = claims.get("type", String.class);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            
            return "refresh".equals(tokenType) && !expiration.before(now);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("리프레시 토큰 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    // 토큰에서 만료 시간까지의 남은 시간 계산 (밀리초)
    public long getTokenExpirationTime(String token) {
        try {
            Claims claims = getClaims(token);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            return expiration.getTime() - now.getTime();
        } catch (JwtException e) {
            return 0;
        }
    }

    public void addToBlackList(String token, long expiration) {
        blackListToken.addToBlackList(token, expiration);
    }
}
