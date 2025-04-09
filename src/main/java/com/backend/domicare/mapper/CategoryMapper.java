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
    default CategoryDTO convertToCategoryDTO(Category category){
        if (category == null) {
            return null;
        }
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .image(category.getImage())
                .createAt(category.getCreateAt())
                .createBy(category.getCreateBy())
                .updateAt(category.getUpdateAt())
                .updateBy(category.getUpdateBy())
                .products(ProductMapper.INSTANCE.convertToProductDTOs(category.getProducts()))
                .build();
    }
}
