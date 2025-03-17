package com.backend.domicare.service.imp;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.backend.domicare.model.FileEntity;
import com.backend.domicare.repository.FileRepository;
import com.backend.domicare.service.FileHandleService;

@Service
public class FileHandleServiceImp implements FileHandleService {

    @Autowired
    private FileRepository fileRepository;

    @Override
    public FileEntity store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
       
        FileEntity fileEntity = new FileEntity(fileName, file.getContentType(), file.getBytes());
        try {
            return fileRepository.save(fileEntity);
        } catch (Exception e) {
            throw new IOException("Could not save the file", e);
        }
    }

    @Override
    public Stream<FileEntity> getAllFiles() {
        return fileRepository.findAll().stream();
    }

    @Override
    public Stream<FileEntity> getFileByName(String filename) {
        List<FileEntity> fileEntityList = fileRepository.findByName(filename);
        if (fileEntityList != null && !fileEntityList.isEmpty()) {
            return Stream.of(fileEntityList.get(fileEntityList.size() - 1)); 
        } else {
            return Stream.empty(); 
        }
    }


   @Override
    public void deleteFile(String filename) {
        List<FileEntity> fileEntityList = fileRepository.findByName(filename);
        
        if (fileEntityList != null && !fileEntityList.isEmpty()) {
            fileRepository.delete(fileEntityList.get(fileEntityList.size() - 1)); 
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
    }

}