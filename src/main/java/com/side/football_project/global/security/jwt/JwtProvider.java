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

    // JWT 생성
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

   public boolean validateAccessToken(String token) {

       try {
           return !getClaims(token).getExpiration().before(new Date());
       } catch (ExpiredJwtException e) {
           log.error("JWT token is expired: {}", e.getMessage());
           return false;
       } catch (JwtException e) {
           log.error("Invalid JWT token: {}", e.getMessage());
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

}
