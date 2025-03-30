package com.backend.domicare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.domicare.dto.FileDTO;
import com.backend.domicare.service.FileService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cloudinary")
public class FileController {
    private final FileService fileService;

    @PostMapping("/files")
    public ResponseEntity<?> uploadFile(@Valid @RequestBody MultipartFile file) {
        FileDTO fileDTO = fileService.uploadFile(file, file.getOriginalFilename());
        return ResponseEntity.status(HttpStatus.CREATED).body(fileDTO);
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

}
