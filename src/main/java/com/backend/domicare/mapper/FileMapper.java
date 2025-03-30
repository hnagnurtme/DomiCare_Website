package com.backend.domicare.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.backend.domicare.dto.FileDTO;
import com.backend.domicare.model.File;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);
    File convertToFile(FileDTO fileDTO);
    FileDTO convertToFileDTO(File file);
}