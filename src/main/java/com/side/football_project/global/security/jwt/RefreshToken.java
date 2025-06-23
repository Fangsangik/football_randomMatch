package com.side.football_project.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshToken {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN = "REFRESH";

    /**
     * 리프레시 토큰을 발급하는 메서드
     *
     * @return 리프레시 토큰
     */

    public void addRefreshToken(String email, String token, long expiration) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN + email, token, Duration.ofMillis(expiration));
    }
}
