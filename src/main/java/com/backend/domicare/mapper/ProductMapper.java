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
        return ProductDTO.builder()
                .description(updateProductRequest.getDescription())
                .id(updateProductRequest.getCategoryId())
                .name(updateProductRequest.getName())
                .price(updateProductRequest.getPrice())
                .discount(updateProductRequest.getDiscount())
                .priceAfterDiscount(updateProductRequest.getPrice() - (updateProductRequest.getPrice() * updateProductRequest.getDiscount() / 100))
                .categoryID(updateProductRequest.getCategoryId())
                .build();
    }
}
// @NotNull(message = "Không được để trống danh mục cũ")
// private Long oldCategoryId;

// @NotNull(message = "Không được để trống id sản phẩm cũ")
// private Long oldProductId;

// @NotNull(message = "Không được để trống danh mục")
// private Long categoryId;
// @NotNull(message = "Không được để trống tên sản phẩm")
// private String name;
// @NotNull(message = "Không được để trống mô tả")
// private String description;
// @NotNull(message = "Không được để trống giá")
// private double price;
// @NotNull(message = "Không được để trống ảnh")
// private Long mainImageId;
// private Double discount;
// private List<Long> landingImageIds;