package com.backend.domicare.service.imp;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.domicare.dto.FileDTO;
import com.backend.domicare.dto.response.ImageResponse;
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
    public FileDTO uploadFile(MultipartFile file, String uniqueName) {
        try {
            // Upload file first to get URL
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("url");
            String type = (String) uploadResult.get("resource_type");
            String size = String.valueOf(file.getSize());
            
            if (url == null || type == null || size == null) {
                throw new RuntimeException("Failed to upload file");
            }
            
            // Kiểm tra xem URL của file đã tồn tại trong cơ sở dữ liệu chưa
            boolean urlExists = filesRepository.existsByUrl(url);
            if (urlExists) {
                // Không cho phép trùng URL, xóa file vừa upload và ném ngoại lệ
                try {
                    String publicId = extractPublicIdFromUrl(url);
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    logger.info("[File] Deleted duplicate file from Cloudinary with URL: {}", url);
                } catch (IOException e) {
                    logger.error("[File] Error deleting duplicate file from Cloudinary: {}", e.getMessage(), e);
                }
                throw new RuntimeException("File with the same URL already exists");
            }
            File uploadedFile = new File();
            uploadedFile.setUrl(url);
            uploadedFile.setType(type);
            uploadedFile.setSize(size);
            uploadedFile.setName(uniqueName);
            filesRepository.save(uploadedFile);
            return FileMapper.INSTANCE.convertToFileDTO(uploadedFile);
        } catch (IOException e) {
            logger.error("[File] Error uploading file: {}", e.getMessage(), e);
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
            logger.info("[File] File with public_id {} has been deleted from Cloudinary", publicId);

            // Xóa file khỏi cơ sở dữ liệu
            filesRepository.delete(file);
            logger.info("[File] File with id {} has been deleted from database", id);

        } catch (IOException e) {
            // Log lỗi nếu có vấn đề trong việc xóa file
            logger.error("[File] Error deleting file from Cloudinary: {}", e.getMessage(), e);
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
            throw new NotFoundFileException("File not found with name: " + name);
        }
        return FileMapper.INSTANCE.convertToFileDTO(file);

    }

    @Override
    public List<FileDTO> fetchAllFiles() {
        List<File> files = filesRepository.findAll();
        return FileMapper.INSTANCE.convertToFileDTOs(files);
    }

    @Override
    public ImageResponse findByUrl(String url) {
        File file = filesRepository.findByUrl(url);
        if (file == null) {
            throw new NotFoundFileException("File not found with url: " + url);
        }
        return FileMapper.INSTANCE.convertToImageResponse(file);
    }

    @Override
    public List<ImageResponse> findByUrls(List<String> urls) {
        List<File> files = filesRepository.findByUrls(urls);
        if (files.isEmpty()) {
            throw new NotFoundFileException("Files not found with urls: " + urls);
        }
        return FileMapper.INSTANCE.convertToImageResponses(files);
    }
    
}
