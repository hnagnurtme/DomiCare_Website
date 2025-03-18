package com.backend.domicare.service;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.domicare.model.FileEntity;

@Service
public interface FileHandleService {
    FileEntity store(MultipartFile file , String filename)  throws IOException;

    Stream<FileEntity> getAllFiles();

    Stream<FileEntity>  getFileByName(String filename);

    public void deleteFile(String filename);
}

