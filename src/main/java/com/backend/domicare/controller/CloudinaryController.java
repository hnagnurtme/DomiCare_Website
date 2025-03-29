package com.backend.domicare.controller;

import com.backend.domicare.dto.FileDTO;
import com.backend.domicare.dto.response.Message;
import com.backend.domicare.model.File;
import com.backend.domicare.service.CloudiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cloudinary")
public class CloudinaryController {
    private final CloudiaryService cloudinaryService;

    @PostMapping("/files")
    public ResponseEntity<FileDTO> uploadFile(@RequestBody MultipartFile file) {
       FileDTO response = cloudinaryService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<FileDTO> getFile(@PathVariable String id) {
        FileDTO response = cloudinaryService.fetchFileById(Long.parseLong(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
