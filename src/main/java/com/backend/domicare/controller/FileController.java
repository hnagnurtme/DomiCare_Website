package com.backend.domicare.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.domicare.dto.FileDTO;
import com.backend.domicare.dto.response.Message;
import com.backend.domicare.service.FileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cloudinary")
public class FileController {
    private final FileService fileService;

    @PostMapping("/files")
    public ResponseEntity<?> uploadFile(@Valid @RequestParam("file") MultipartFile file) {
        FileDTO fileDTO = fileService.uploadFile(file, file.getOriginalFilename(), false);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileDTO);
    }
    
    @PostMapping("/files/multiple")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        List<FileDTO> uploadedFiles = new ArrayList<>();
        
        for (MultipartFile file : files) {
            FileDTO fileDTO = fileService.uploadFile(file, file.getOriginalFilename(), false);
            uploadedFiles.add(fileDTO);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadedFiles);
    }
    
    @GetMapping("/files/{id}")
    public ResponseEntity<?> fetchFileById(@PathVariable Long id) {
        FileDTO fileDTO = fileService.fetchFileById(id);
        return ResponseEntity.status(HttpStatus.OK).body(fileDTO);
    }

    @GetMapping("/files")
    public ResponseEntity<?> fetchFileByName(@RequestParam String name) {
        FileDTO fileDTO = fileService.fetchFileByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(fileDTO);
    }

    @GetMapping("/files/all")
    public ResponseEntity<?> fetchAllFiles() {
        return ResponseEntity.status(HttpStatus.OK).body(fileService.fetchAllFiles());
    }

    @DeleteMapping("/files/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new Message("File deleted successfully"));
    }

}
