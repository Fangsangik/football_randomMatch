package com.side.football_project.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlackListToken {
    private final StringRedisTemplate redis;
    private static final String BLACKLIST_PREFIX = "blackList:";
    /**
     * 블랙리스트에 토큰 추가
     * @param token 블랙리스트에 추가할 토큰
     */

    public void addToBlackList(String token, long expiration) {
        // 토큰을 블랙리스트에 추가하는 로직
        redis.opsForValue().set(BLACKLIST_PREFIX + token, "BLACKLISTED", expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isBlackListed(String token) {
        // 토큰이 블랙리스트에 있는지 확인하는 로직
        return redis.hasKey(BLACKLIST_PREFIX + token);
    }
}
