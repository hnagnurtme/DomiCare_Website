package com.backend.domicare.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.backend.domicare.dto.ProductDTO;
import com.backend.domicare.model.Product;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface  ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    Product convertToProduct(ProductDTO productDTO);
    default ProductDTO convertToProductDTO(Product product) {
        if (product == null) {
            return null;
        }
        return ProductDTO.builder()
                .description(product.getDescription())
                .id(product.getId())
                .image(product.getImage())
                .name(product.getName())
                .price(product.getPrice())
                .categoryID(product.getCategory() != null ? product.getCategory().getId() : null)
                .build();
    }
    List<ProductDTO> convertToProductDTOs(List<Product> products);
    List<Product> convertToProducts(List<ProductDTO> productDTOs);
}
