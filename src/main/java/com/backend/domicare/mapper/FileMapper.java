package com.backend.domicare.mapper;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.backend.domicare.dto.FileDTO;
import com.backend.domicare.model.File;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    File convertToFile(FileDTO fileDTO);

    // Sửa lại phương thức convertToFileDTO
    default FileDTO convertToFileDTO(File file) {
        if (file == null) {
            return null;
        }

        // Trích xuất public_id từ URL
        String publicIdExtract = extractPublicIdFromUrl(file.getUrl());

        return FileDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .url(file.getUrl())
                .createAt(file.getCreatedAt())
                .createBy(file.getCreateBy())
                .updateAt(file.getUpdatedAt())
                .updateBy(file.getUpdateBy())
                .type(file.getType())
                .size(file.getSize())
                .publicId(publicIdExtract)  // Gán public_id đã trích xuất
                .build();
    }

    // Hàm trích xuất public_id từ URL Cloudinary
    private String extractPublicIdFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;
        }
        // Tách URL để lấy public_id
        String[] urlParts = fileUrl.split("/v\\d+/");
        if (urlParts.length > 1) {
            String[] publicIdParts = urlParts[1].split("\\.");
            return publicIdParts[0];
        }
        return null;
    }

    List<FileDTO> convertToFileDTOs(List<File> files);
    List<File> convertToFiles(List<FileDTO> fileDTOs);
}
