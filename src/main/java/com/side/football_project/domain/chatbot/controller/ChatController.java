package com.side.football_project.domain.chatbot.controller;

import com.side.football_project.domain.chatbot.dto.ChatRequest;
import com.side.football_project.domain.chatbot.dto.ChatResponse;
import com.side.football_project.domain.chatbot.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatbotService chatbotService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        log.info("[ChatController] 요청 메시지: {}", request.getMessage());
        String reply = chatbotService.chat(request.getMessage());
        log.info("[ChatController] 응답 메시지: {}", reply);
        return ResponseEntity.ok(new ChatResponse(reply));
    }
}
