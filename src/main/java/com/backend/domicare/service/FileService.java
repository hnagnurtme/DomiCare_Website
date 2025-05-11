package com.backend.domicare.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.backend.domicare.dto.FileDTO;
import com.backend.domicare.dto.response.ImageResponse;

public interface FileService {
    public FileDTO uploadFile(MultipartFile file , String uniqueName);
    public FileDTO fetchFileById(Long id);
    public void deleteFile(Long id);
    public FileDTO updateFile(Long id, FileDTO file);
    public FileDTO fetchFileByName(String name);
    public List<FileDTO> fetchAllFiles();

    public ImageResponse findByUrl(String url);
    public List<ImageResponse> findByUrls(List<String> urls);
}
