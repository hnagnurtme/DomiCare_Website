package com.backend.domicare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.backend.domicare.dto.CategoryDTO;
import com.backend.domicare.model.Category;
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    Category convertToCategory(CategoryDTO categoryDTO);
    CategoryDTO convertToCategoryDTO(Category category);
}
