package com.backend.domicare.service.imp;

import com.backend.domicare.dto.FileDTO;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.mapper.FileMapper;
import com.backend.domicare.model.File;
import com.backend.domicare.repository.FilesRepository;
import com.backend.domicare.service.CloudiaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImp implements CloudiaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryServiceImp.class);

    private final Cloudinary cloudinary;
    private final FilesRepository filesRepository;

    @Override
    public FileDTO uploadFile(MultipartFile file) {
        try {
            // Tải tệp lên Cloudinary và lấy URL
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("url");

            // Lưu tệp vào cơ sở dữ liệu
            File uploadedFile = new File();
            uploadedFile.setUrl(url);
            filesRepository.save(uploadedFile);

            // Chuyển đổi đối tượng File sang FileDTO và trả về
            return FileMapper.INSTANCE.convertToFileDTO(uploadedFile);
        } catch (IOException e) {
            // Sử dụng logging thay vì in ra console
            logger.error("Error uploading file: {}", e.getMessage(), e);
            throw new RuntimeException("Error uploading file", e);
        }
    }

    @Override
    public FileDTO fetchFileById(Long id) {
        // Sử dụng Optional để tránh gọi findById() hai lần
        File file = filesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("File not found with ID: " + id));

        // Chuyển đổi đối tượng File sang FileDTO và trả về
        return FileMapper.INSTANCE.convertToFileDTO(file);
    }
}
