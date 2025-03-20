package com.backend.domicare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.backend.domicare.dto.ProductDTO;
import com.backend.domicare.model.Product;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface  ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    Product convertToProduct(ProductDTO productDTO);
    ProductDTO convertToProductDTO(Product product);
}
