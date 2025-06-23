package com.side.football_project.global.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(name = "cloud.aws.enabled", havingValue = "false", matchIfMissing = true)
public class MockS3Service implements FileUploadService {

    public String uploadFile(MultipartFile file) {
        log.info("Mock S3 Service: 파일 업로드 시뮬레이션 - {}", file.getOriginalFilename());
        
        // 실제 파일 업로드 대신 더미 URL 반환
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        return "http://localhost:8080/mock-files/" + fileName;
    }

    public String getFileUrl(String fileName) {
        return "http://localhost:8080/mock-files/" + fileName;
    }
}