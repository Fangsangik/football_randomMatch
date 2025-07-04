package com.side.football_project.global.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadFile(MultipartFile file);
    String getFileUrl(String fileName);
}