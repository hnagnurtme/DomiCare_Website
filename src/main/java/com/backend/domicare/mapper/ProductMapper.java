package com.backend.domicare.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.backend.domicare.dto.ProductDTO;
import com.backend.domicare.dto.request.UpdateProductRequest;
import com.backend.domicare.dto.response.ProductResponse;
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
                .ratingStar(product.getOveralRating())
                .discount(product.getDiscount())
                .priceAfterDiscount(product.getPrice() - (product.getPrice() * product.getDiscount() / 100))
                .landingImages(product.getLandingImages())
                .createAt(product.getCreateAt())
                .createBy(product.getCreateBy())
                .updateAt(product.getUpdateAt())
                .updateBy(product.getUpdateBy())
                .reviewDTOs(product.getReviews() != null ? ReviewMapper.INSTANCE.convertToReviewDTOs(product.getReviews()) : null)
                .categoryID(product.getCategory() != null ? product.getCategory().getId() : null)
                .build();
    }
    List<ProductDTO> convertToProductDTOs(List<Product> products);

    default ProductResponse convertToProductResponse(Product product) {
        if (product == null) {
            return null;
        }
        return ProductResponse.builder()
                .description(product.getDescription())
                .id(product.getId())
                .image(product.getImage())
                .name(product.getName())
                .price(product.getPrice())
                .ratingStar(product.getOveralRating())
                .discount(product.getDiscount())
                .priceAfterDiscount(product.getPrice() - (product.getPrice() * product.getDiscount() / 100))
                .landingImages(product.getLandingImages())
                .createAt(product.getCreateAt())
                .createBy(product.getCreateBy())
                .updateAt(product.getUpdateAt())
                .updateBy(product.getUpdateBy())
                .reviewDTOs(product.getReviews() != null ? ReviewMapper.INSTANCE.convertToReviewDTOs(product.getReviews()) : null)
                .categoryMini(CategoryMapper.INSTANCE.convertToCategoryMini(product.getCategory()))
                .build();
    }
    List<Product> convertToProducts(List<ProductDTO> productDTOs);

    List<ProductResponse> convertToProductResponses(List<Product> products);

    default ProductDTO convertToProductDTOByUpdateProductRequest(UpdateProductRequest updateProductRequest){
        if (updateProductRequest == null) {
            return null;
        }
    
        Double discount = updateProductRequest.getDiscount();
        Double price = updateProductRequest.getPrice();
        Double priceAfterDiscount = price;
    
        if (discount != null) {
            priceAfterDiscount = price - (price * discount / 100);
        }
    
        return ProductDTO.builder()
                .description(updateProductRequest.getDescription())
                .id(updateProductRequest.getOldProductId())
                .name(updateProductRequest.getName())
                .price(price)
                .discount(discount)
                .priceAfterDiscount(priceAfterDiscount)
                .categoryID(updateProductRequest.getCategoryId())
                .build();
    }
    
}
