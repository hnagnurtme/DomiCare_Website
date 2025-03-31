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
import com.backend.domicare.dto.request.AddProductRequest;
import com.backend.domicare.dto.request.UpdateProductRequest;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.exception.ProductNameAlreadyExists;
import com.backend.domicare.mapper.ProductMapper;
import com.backend.domicare.model.Category;
import com.backend.domicare.model.Product;
import com.backend.domicare.repository.CategoriesRepository;
import com.backend.domicare.repository.ProductsRepository;
import com.backend.domicare.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImp implements ProductService {
    private final ProductsRepository productRepository;
    private final CategoriesRepository categoryRepository;

    @Override
    @Transactional
    public ProductDTO addProduct(AddProductRequest request) {
        // Extract product and category IDs from request
        Long categoryID = request.getCategoryId();
        ProductDTO productDTO = request.getProduct();
        // Check if category exists
        Category category = categoryRepository.findById(categoryID)
            .orElseThrow(() -> new NotFoundException("Category not found"));

        // Check if product already exists
        if (productRepository.existsByName(productDTO.getName())) {
            throw new ProductNameAlreadyExists("Product with the same name already exists");
        }

        // Convert DTO to entity and set category
        Product productEntity = ProductMapper.INSTANCE.convertToProduct(productDTO);
        productEntity.setCategory(category);

        // Save category 
        List<Product> products = category.getProducts();
        if (products == null) {
            products = new ArrayList<>();
        }
        products.add(productEntity);
        category.setProducts(products);
        // Save category entity
        categoryRepository.save(category);

        // Save product entity and return DTO
        productRepository.save(productEntity);
        
        //
        // Convert saved entity back to DTO
        ProductDTO savedProductDTO = ProductMapper.INSTANCE.convertToProductDTO(productEntity);
        // Set category information in DTO
        savedProductDTO.setCategoryID(categoryID);
        return  savedProductDTO;
    }

    @Override
    public ProductDTO updateProduct(UpdateProductRequest request) {
        // Extract product ID and category ID from request
        ProductDTO productDTO = request.getUpdateProduct();
        Long id = productDTO.getId();
        Long newCategoryID = productDTO.getCategoryID();
        Long oldCategoryID = request.getOldCategoryId();
    
        // Find existing product or throw exception
        Product productEntity = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));
    
        // Check if category has changed
        if (newCategoryID != null && !newCategoryID.equals(oldCategoryID)) {
            // Find the new category and old category
            Category newCategory = categoryRepository.findById(newCategoryID)
                .orElseThrow(() -> new NotFoundException("New Category not found"));
    
            Category oldCategory = categoryRepository.findById(oldCategoryID)
                .orElseThrow(() -> new NotFoundException("Old Category not found"));
    
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
        productEntity.setName(productDTO.getName());
        productEntity.setDescription(productDTO.getDescription());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setImage(productDTO.getImage());
    
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
            throw new NotFoundException("Product is not associated with any category");
        }

        // Remove the product from the category's product list (if necessary)
        List<Product> products = category.getProducts();
        if (products.removeIf(p -> p.getId().equals(id))) {
            // If the product is successfully removed from the category's list, delete it
            productRepository.deleteById(id);
        } else {
            throw new NotFoundException("Product not found in the category");
        }
    }

    @Override
    public ProductDTO fetchProductById(Long id) {
        // Retrieve product by ID or throw exception
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));
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
        meta.setPage(allProducts.getNumber());
        meta.setSize(allProducts.getSize());
        meta.setTotal(allProducts.getTotalElements());
        meta.setTotalPages(allProducts.getTotalPages());
        result.setMeta(meta);
        result.setData(productDTOs);
        // Return the result
        return result;
    }
}
