package com.backend.domicare.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.domicare.dto.ProductDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.dto.request.AddProductImageRequest;
import com.backend.domicare.dto.request.AddProductRequest;
import com.backend.domicare.dto.request.UpdateProductRequest;
import com.backend.domicare.exception.CategoryNotFoundException;
import com.backend.domicare.exception.NotFoundCategoryException;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.exception.NotFoundFileException;
import com.backend.domicare.exception.NotFoundProductException;
import com.backend.domicare.exception.ProductNameAlreadyExists;
import com.backend.domicare.exception.ProductNotInCategory;
import com.backend.domicare.exception.UrlAlreadyExistsException;
import com.backend.domicare.mapper.ProductMapper;
import com.backend.domicare.model.Category;
import com.backend.domicare.model.File;
import com.backend.domicare.model.Product;
import com.backend.domicare.repository.CategoriesRepository;
import com.backend.domicare.repository.FilesRepository;
import com.backend.domicare.repository.ProductsRepository;
import com.backend.domicare.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImp implements ProductService {
    private final ProductsRepository productRepository;
    private final CategoriesRepository categoryRepository;
    private final FilesRepository fileRepository;

    @Override
    @Transactional
    public ProductDTO addProduct(AddProductRequest request) {
        // Extract product and category IDs from request
        Long categoryID = request.getCategoryId();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(request.getName());
        productDTO.setDescription(request.getDescription());
        productDTO.setPrice(request.getPrice());
        productDTO.setDiscount(request.getDiscount());
    
        // Extract main image and landing image IDs from request
        Long mainImageId = request.getMainImageId();
        List<Long> landingImageIds = request.getLandingImageIds();
        
        // Check if category exists
        Category category = categoryRepository.findById(categoryID)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryID));

        // Check if product with the same name already exists in the specific category
        if (productRepository.existsByNameAndCategoryId(productDTO.getName(), categoryID)) {
            throw new ProductNameAlreadyExists("Product with the same name already exists in this category");
        }

        // Check if the image is already associated with the product
        if(mainImageId != null){
            File mainImage = fileRepository.findById(mainImageId)
                .orElseThrow(() -> new NotFoundFileException("Main image not found"));
            productDTO.setImage(mainImage.getUrl());
        }
        if (landingImageIds != null) {
            List<String> landingImages = new ArrayList<>();
            for (Long imageId : landingImageIds) {
                File landingImage = fileRepository.findById(imageId)
                    .orElseThrow(() -> new NotFoundFileException("Landing image not found"));
                landingImages.add(landingImage.getUrl());
            }
            productDTO.setLandingImages(landingImages);
        }
    
        // Convert DTO to entity and set category
        Product productEntity = ProductMapper.INSTANCE.convertToProduct(productDTO);
        productEntity.setCategory(category);

        // Save product entity
        productRepository.save(productEntity);

        // Convert saved entity back to DTO
        ProductDTO savedProductDTO = ProductMapper.INSTANCE.convertToProductDTO(productEntity);

        // Set category information in DTO
        savedProductDTO.setCategoryID(categoryID);

        return savedProductDTO;
    }


    @Override
    public ProductDTO updateProduct(UpdateProductRequest request) {
        // Extract product ID and category ID from request
        ProductDTO productDTO = request.getUpdateProduct();
        Long id = request.getOldProductId();
        Long newCategoryID = productDTO.getCategoryID();
        Long oldCategoryID = request.getOldCategoryId();
        if ( newCategoryID == null) {
            newCategoryID = oldCategoryID;
        }

        // Check if category not found
        Category oldCategory = categoryRepository.findById(oldCategoryID)
            .orElseThrow(() -> new CategoryNotFoundException("Old Category not found"));
        // Check if product not in old category

        if (oldCategory.getProducts().stream().noneMatch(p -> p.getId().equals(id))) {
            throw new ProductNotInCategory("Product not found in the old category");
        }

        // Check if new category not found
        Category newCategory = categoryRepository.findById(newCategoryID)
            .orElseThrow(() -> new CategoryNotFoundException("New Category not found"));

        // Find existing product or throw exception
        Product productEntity = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundProductException("Product not found"));
    
        // Check if category has changed
        if (newCategoryID != null && !newCategoryID.equals(oldCategoryID)) {
            // Find the new category and old category
        
            // Remove product from the old category's product list
            List<Product> oldCategoryProducts = oldCategory.getProducts();

            oldCategoryProducts.removeIf(p -> p.getId().equals(id));
            categoryRepository.save(oldCategory); // Save the updated old category
    
            // Add product to the new category's product list
            List<Product> newCategoryProducts = newCategory.getProducts();
            if (newCategoryProducts == null) {
                newCategoryProducts = new ArrayList<>();
            }
            newCategoryProducts.add(productEntity);
            newCategory.setProducts(newCategoryProducts); // Update the new category's product list
            categoryRepository.save(newCategory); // Save the updated new category
    
            // Set the product's category to the new category
            productEntity.setCategory(newCategory);
        }
    
        // Update product information from DTO

        if ( productDTO.getName() != null) {
            productEntity.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            productEntity.setDescription(productDTO.getDescription());
        }
        if (productDTO.getPrice() > 0) {
            productEntity.setPrice(productDTO.getPrice());
        }
        if (productDTO.getImage() != null) {
            productEntity.setImage(productDTO.getImage());
        }
        if (productDTO.getDiscount() != null) {
            productEntity.setDiscount(productDTO.getDiscount());
        }
    
        // Save the updated product and return DTO
        productRepository.save(productEntity);
    
        // Convert updated product entity to DTO
        ProductDTO updatedProductDTO = ProductMapper.INSTANCE.convertToProductDTO(productEntity);
        // Set the category ID in the DTO
        updatedProductDTO.setCategoryID(newCategoryID);
        return updatedProductDTO;
    }
    


    @Override
    @Transactional
    public void deleteProduct(Long id) {
        // Retrieve product or throw exception
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));

        // Ensure product is associated with a category
        Category category = product.getCategory();
        if (category == null) {
            throw new NotFoundCategoryException("Product does not belong to any category");
        }

        // Remove the product from the category's product list (if necessary)
        List<Product> products = category.getProducts();
        if (products.removeIf(p -> p.getId().equals(id))) {
            // If the product is successfully removed from the category's list, delete it
            productRepository.deleteById(id);
        } else {
            throw new ProductNotInCategory("Product not found in the category");
        }
    }

    @Override
    public ProductDTO fetchProductById(Long id) {
        // Retrieve product by ID or throw exception
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundProductException("Product not found"));
        return ProductMapper.INSTANCE.convertToProductDTO(product);
    }


    @Override
    public ResultPagingDTO getAllProducts(Specification<Product> spec,Pageable pageable){
    // Fetch paginated products based on specification
        Page<Product> allProducts = this.productRepository.findAll(spec, pageable);
        // Convert to DTO list
        List<ProductDTO> productDTOs = ProductMapper.INSTANCE.convertToProductDTOs(allProducts.getContent());
        // Create ResultPagingDTO object
        ResultPagingDTO result = new ResultPagingDTO();
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();
        meta.setPage(allProducts.getNumber()+ 1);
        meta.setSize(allProducts.getSize());
        meta.setTotal(allProducts.getTotalElements());
        meta.setTotalPages(allProducts.getTotalPages());
        result.setMeta(meta);
        result.setData(productDTOs);
        // Return the result
        return result;
    }

    @Override
    public ProductDTO addProductImage(AddProductImageRequest addProductImageRequest){
        // Extract product ID and image from request
        Long productId = addProductImageRequest.getProductId();
        Long imageId = addProductImageRequest.getImageId();
        Boolean isMainImage = addProductImageRequest.getIsMainImage();
        // Retrieve product by ID or throw exception
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundProductException("Product not found"));
        //
        // Retrieve image by ID or throw exception
        File image = fileRepository.findById(imageId)
            .orElseThrow(() -> new NotFoundFileException("Image not found"));
        if( isMainImage){
            product.setImage(image.getUrl());
        }
        else{
            // Check if the image is already associated with the product
            if (product.getLandingImages() == null) {
                product.setLandingImages(new ArrayList<>());
            }
            if (!product.getLandingImages().contains(image.getUrl())) {
                product.getLandingImages().add(image.getUrl());
            } else {
                throw new UrlAlreadyExistsException("Image already exists in the product's landing images");
            }
        }
        // Check if the image is already associated with the product
        productRepository.save(product);
        
        return ProductMapper.INSTANCE.convertToProductDTO(product);
    }

    @Override
    public List<Product> findAllByIdIn(List<Long> ids){
        // Fetch products by IDs
        List<Product> products = productRepository.findAllById(ids);
        // Check if any products are found
        if (products.isEmpty()) {
            throw new NotFoundProductException("No products found with the provided IDs");
        }
        return products;

    }

}
