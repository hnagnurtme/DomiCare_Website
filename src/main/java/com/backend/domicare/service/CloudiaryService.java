package com.backend.domicare.service;

import com.backend.domicare.dto.FileDTO;
import com.backend.domicare.model.File;
import org.springframework.web.multipart.MultipartFile;

public interface CloudiaryService {
    public FileDTO uploadFile(MultipartFile file);
    public FileDTO fetchFileById(Long id);
}
