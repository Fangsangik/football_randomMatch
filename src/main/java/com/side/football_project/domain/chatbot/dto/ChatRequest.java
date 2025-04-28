package com.side.football_project.domain.chatbot.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 클라이언트로부터 들어오는 챗 요청 메시지 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private String message;
}

