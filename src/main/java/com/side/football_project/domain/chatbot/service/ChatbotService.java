package com.side.football_project.domain.chatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    @Value("${gcp.api.key}")
    private String gcpApiKey;

    private final RestTemplate restTemplate;

    // Vertex AI Text Generation 엔드포인트 (text-bison-001 모델)
    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta2/models/text-bison-001:generateText";

    /**
     * Google Gemini(text-bison-001) 모델로 채팅 메시지를 보낸 뒤
     * 생성된 텍스트를 리턴합니다.
     */
    public String chat(String sendMessage) {
        // 1) HTTP 헤더 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2) 요청 바디 구성
        Map<String, Object> body = Map.of(
                "prompt", Map.of("text", sendMessage),
                "temperature", 0.7,
                "maxOutputTokens", 256
        );
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // 3) URL에 API 키 쿼리 파라미터로 추가
        String url = GEMINI_URL + "?key=" + gcpApiKey;

        try {
            // 4) 호출 및 응답 수신
            ResponseEntity<JsonNode> resp = restTemplate
                    .postForEntity(url, request, JsonNode.class);

            if (!resp.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Vertex AI 호출 실패: " + resp.getStatusCode());
            }

            // 5) JSON 파싱: candidates[0].output 추출
            JsonNode candidates = resp.getBody().path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                return candidates.get(0).path("output").asText("");
            } else {
                return "";
            }

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                throw new RuntimeException("Vertex AI Rate limit exceeded", e);
            }
            throw new RuntimeException("Vertex AI 호출 중 오류 발생", e);
        }
    }
}
