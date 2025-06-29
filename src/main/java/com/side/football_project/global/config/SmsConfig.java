package com.side.football_project.global.config;

import net.nurigo.java_sdk.api.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

    @Value("${sms.api.key}")
    private String apiKey;

    @Value("${sms.api.secret}")
    private String apiSecret;

    @Bean
    public Message messageService() {
        return new Message(apiKey, apiSecret);
    }
}