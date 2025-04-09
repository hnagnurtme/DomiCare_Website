package com.backend.domicare.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.backend.domicare.dto.FileDTO;

public interface FileService {
    public FileDTO uploadFile(MultipartFile file , String uniqueName, boolean isDuplicate);
    public FileDTO fetchFileById(Long id);
    public void deleteFile(Long id);
    public FileDTO updateFile(Long id, FileDTO file);
    public FileDTO fetchFileByName(String name);

    public List<FileDTO> fetchAllFiles();
}
