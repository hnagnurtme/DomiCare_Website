package com.backend.domicare.service.imp;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.domicare.dto.FileDTO;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.exception.NotFoundFileException;
import com.backend.domicare.mapper.FileMapper;
import com.backend.domicare.model.File;
import com.backend.domicare.repository.FilesRepository;
import com.backend.domicare.service.FileService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImp implements FileService {
    private final FilesRepository filesRepository;
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImp.class);
    private final Cloudinary cloudinary;
    @Override
    @Transactional
    public FileDTO uploadFile(MultipartFile file,String uniqueName, boolean isDuplicate) {
        // Kiểm tra xem file đã tồn tại trong cơ sở dữ liệu chưa
        boolean fileExists = filesRepository.existsByName(uniqueName);
        if (fileExists ) {
            if (isDuplicate) {
                // Nếu cho phép trùng tên, xóa file cũ
                File existingFile = filesRepository.findByName(uniqueName);
                if (existingFile != null) {
                    filesRepository.delete(existingFile);
                }
            } else {
                // Nếu không cho phép trùng tên, ném ngoại lệ
                throw new RuntimeException("File with the same name already exists");
            }
        }
        try {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("url");
            String type = (String) uploadResult.get("resource_type");
            String size = String.valueOf(file.getSize());
            if (url == null || type == null || size == null) {
                throw new RuntimeException("Failed to upload file");
            }
            File uploadedFile = new File();
            uploadedFile.setUrl(url);
            uploadedFile.setType(type);
            uploadedFile.setSize(size);
            uploadedFile.setName(uniqueName);
            filesRepository.save(uploadedFile);
            return FileMapper.INSTANCE.convertToFileDTO(uploadedFile);
        } catch (IOException e) {
            // Sử dụng logging thay vì in ra console
            logger.error("Error uploading file: {}", e.getMessage(), e);
            throw new RuntimeException("Error uploading file", e);
        }
    }

    @Override
    public FileDTO fetchFileById(Long id) {
        File file = filesRepository.findById(id).orElseThrow(() -> new NotFoundFileException("File not found with id: " + id));
        return FileMapper.INSTANCE.convertToFileDTO(file);
    }

    @Override
    public void deleteFile(Long id) {
        // Lấy file từ cơ sở dữ liệu
        File file = filesRepository.findById(id).orElseThrow(() -> new NotFoundFileException("File not found with id: " + id));

        // Lấy URL của file
        String fileUrl = file.getUrl();
        
        // Trích xuất public_id từ URL
        String publicId = extractPublicIdFromUrl(fileUrl);

        try {
            // Xóa file trên Cloudinary
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            logger.info("File with public_id {} has been deleted from Cloudinary", publicId);

            // Xóa file khỏi cơ sở dữ liệu
            filesRepository.delete(file);
            logger.info("File with id {} has been deleted from database", id);

        } catch (IOException e) {
            // Log lỗi nếu có vấn đề trong việc xóa file
            logger.error("Error deleting file from Cloudinary: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting file", e);
        }
    }

    // Hàm trích xuất public_id từ URL Cloudinary
    private String extractPublicIdFromUrl(String fileUrl) {
        // Lấy phần public_id từ URL
        String[] urlParts = fileUrl.split("/v\\d+/");
        if (urlParts.length > 1) {
            String[] publicIdParts = urlParts[1].split("\\.");
            return publicIdParts[0];
        }
        return null;
    }

    @Override
    public FileDTO updateFile(Long id, FileDTO file) {
        
        throw new UnsupportedOperationException("Unimplemented method 'updateFile'");
    }

    @Override
    public FileDTO fetchFileByName(String name) {
        File file = filesRepository.findByName(name);
        if (file == null) {
            throw new NotFoundException("File not found with name: " + name);
        }
        return FileMapper.INSTANCE.convertToFileDTO(file);

    }

    @Override
    public List<FileDTO> fetchAllFiles() {
        List<File> files = filesRepository.findAll();
        return FileMapper.INSTANCE.convertToFileDTOs(files);
    }
    
}
