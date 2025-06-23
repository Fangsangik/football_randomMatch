package com.side.football_project.global.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "cloud.aws.enabled", havingValue = "true")
@RequiredArgsConstructor
public class S3Service implements FileUploadService {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            return getFileUrl(fileName);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    public String getFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, Region.AP_NORTHEAST_2.id(), fileName);
    }
}
