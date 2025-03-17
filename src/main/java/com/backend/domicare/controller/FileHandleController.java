package com.backend.domicare.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.domicare.dto.response.FileDTOResponse;
import com.backend.domicare.dto.response.Message;
import com.backend.domicare.model.FileEntity;
import com.backend.domicare.repository.FileRepository;
import com.backend.domicare.service.FileHandleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileHandleController {
    private final FileHandleService fileHandleService;
    private final FileRepository fileRepository;
    @PostMapping("/files")
    public ResponseEntity<Message> uploadFile(@RequestBody MultipartFile file) {
    
        try{
            fileHandleService.store(file);
            String message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(200).body(new Message(message));
        } catch (IOException e) {
            System.err.println("Error uploading file: " + e.getMessage());
            String message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(500).body(new Message(message));
        }
    }
    

    @GetMapping("/files")
    public ResponseEntity<FileDTOResponse> getFileByName(@RequestParam("filename") String filename) {
        Stream<FileEntity> fileEntityList = fileHandleService.getFileByName(filename);
        
        if (fileEntityList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        else
        {
            List<FileEntity> files = fileEntityList.collect(Collectors.toList());
            if (files.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            FileEntity dbFile = files.get(0);
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();
        
            FileDTOResponse fileDTOResponse = new FileDTOResponse(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length
            );
            
            return ResponseEntity.status(HttpStatus.OK).body(fileDTOResponse);
        }
    }
    

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        Optional<FileEntity> optionalFile = fileRepository.findById(id);
        if (!optionalFile.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        FileEntity fileEntity = optionalFile.get();
        
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Disposition", "inline; filename=\"" + fileEntity.getName() + "\"")
                    .contentType(MediaType.parseMediaType(fileEntity.getType())) 
                    .body(fileEntity.getData()); 
        } catch (Exception e) {
            System.err.println("Error downloading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/files/{filename}")
    public ResponseEntity<Message> deleteFile(@PathVariable("filename") String filename) {
        try {
            fileHandleService.deleteFile(filename);
            String message = "Deleted the file successfully: " + filename;
            return ResponseEntity.status(200).body(new Message(message));
        } catch (Exception e) {
            System.err.println("Error deleting file: " + e.getMessage());
            String message = "Could not delete the file: " + filename + "!";
            return ResponseEntity.status(500).body(new Message(message));
        }
    }
}
