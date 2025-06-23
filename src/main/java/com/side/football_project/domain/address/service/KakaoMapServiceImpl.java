package com.side.football_project.domain.address.service;

import com.side.football_project.domain.address.dto.KakaoGeoResponse;
import com.side.football_project.domain.address.dto.LatLngDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KakaoMapServiceImpl implements KakoMapService{

    @Value("${kakao_client_id}")
    private String apiKey;
    private final RestTemplate restTemplate;

    @Override
    public LatLngDto getCoordinates(String address) {
        // 주소 정리: 아파트 동호수 등 불필요한 정보 제거
        String cleanedAddress = cleanAddress(address);
        
        String url = "https://dapi.kakao.com/v2/local/search/address.json";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("query", cleanedAddress);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoGeoResponse> response =
                    restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, KakaoGeoResponse.class);

            System.out.println("카카오 API 응답: " + response.getStatusCode());
            System.out.println("원본 주소: " + address);
            System.out.println("정리된 주소: " + cleanedAddress);
            System.out.println("요청 URL: " + uriBuilder.toUriString());
            
            if (response.getBody() != null) {
                System.out.println("응답 본문 documents 수: " + (response.getBody().getDocuments() != null ? response.getBody().getDocuments().size() : "null"));
            }

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null
                    && response.getBody().getDocuments() != null && !response.getBody().getDocuments().isEmpty()) {

                KakaoGeoResponse.Document document = response.getBody().getDocuments().get(0);
                double latitude = Double.parseDouble(document.getY());
                double longitude = Double.parseDouble(document.getX());

                System.out.println("변환 성공 - 위도: " + latitude + ", 경도: " + longitude);
                return new LatLngDto(latitude, longitude);
            }

        } catch (Exception e) {
            System.err.println("카카오 API 호출 오류: " + e.getMessage());
            e.printStackTrace();
        }

        // 카카오 API 실패 시 지역별 기본 좌표 반환
        LatLngDto fallbackCoordinate = getFallbackCoordinate(address, cleanedAddress);
        System.out.println("카카오 API 실패로 기본 좌표 사용: " + fallbackCoordinate.getLatitude() + ", " + fallbackCoordinate.getLongitude());
        return fallbackCoordinate;
    }
    
    private String cleanAddress(String address) {
        if (address == null) return "";
        
        // 주소 순서 정리 및 불필요한 정보 제거
        String cleaned = address.trim();
        
        // 동호수 정보 제거 (예: "235동 1503호" 제거)
        cleaned = cleaned.replaceAll("\\d+동\\s+\\d+호", "");
        
        // 여러 공백을 하나로
        cleaned = cleaned.replaceAll("\\s+", " ");
        
        // 주소 순서 표준화: "시/도 시/군/구 읍/면/동 나머지주소" 순으로 정리
        String[] parts = cleaned.split("\\s+");
        
        StringBuilder standardAddress = new StringBuilder();
        
        // 경기도 화성시 동탄면 한빛길 10 형태로 정리
        for (String part : parts) {
            if (part.contains("도") || part.contains("시") || part.contains("군") || 
                part.contains("구") || part.contains("면") || part.contains("동") ||
                part.contains("길") || part.contains("로")) {
                standardAddress.append(part).append(" ");
            } else if (part.matches("\\d+")) {
                // 우편번호나 번지수
                standardAddress.append(part).append(" ");
            }
        }
        
        return standardAddress.toString().trim();
    }
    
    private LatLngDto getFallbackCoordinate(String originalAddress, String cleanedAddress) {
        String address = (originalAddress + " " + cleanedAddress).toLowerCase();
        
        // 주요 지역별 중심 좌표
        if (address.contains("화성") || address.contains("동탄")) {
            return new LatLngDto(37.2020, 127.0770); // 화성시 동탄신도시
        } else if (address.contains("서울")) {
            return new LatLngDto(37.5665, 126.9780); // 서울시청
        } else if (address.contains("부산")) {
            return new LatLngDto(35.1796, 129.0756); // 부산시청
        } else if (address.contains("대구")) {
            return new LatLngDto(35.8714, 128.6014); // 대구시청
        } else if (address.contains("인천")) {
            return new LatLngDto(37.4563, 126.7052); // 인천시청
        } else if (address.contains("광주")) {
            return new LatLngDto(35.1595, 126.8526); // 광주시청
        } else if (address.contains("대전")) {
            return new LatLngDto(36.3504, 127.3845); // 대전시청
        } else if (address.contains("울산")) {
            return new LatLngDto(35.5384, 129.3114); // 울산시청
        } else if (address.contains("세종")) {
            return new LatLngDto(36.4800, 127.2890); // 세종시청
        } else if (address.contains("경기")) {
            return new LatLngDto(37.4138, 127.5183); // 경기도청
        } else {
            return new LatLngDto(37.5665, 126.9780); // 기본값: 서울시청
        }
    }
}
