package com.side.football_project.global.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.validation.ObjectError;

@Configuration
public class RedisConfig {

    @Bean
    public RedissonClient redissonClient() {
       Config config = new Config();
        config.useSingleServer()
              .setAddress("redis://localhost:6379")
              .setConnectionMinimumIdleSize(10)
              .setConnectionPoolSize(64);

        return Redisson.create(config);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // RedisConnectionFactory 설정을 여기에 추가할 수 있습니다.
        // 예: LettuceConnectionFactory를 사용하여 Redis 연결을 생성할 수 있습니다.
        return new LettuceConnectionFactory("localhost", 6379);
    }
}
