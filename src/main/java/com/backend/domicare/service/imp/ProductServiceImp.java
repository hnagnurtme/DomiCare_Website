package com.backend.domicare.service.imp;

import org.springframework.stereotype.Service;

import com.backend.domicare.dto.ProductDTO;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.mapper.ProductMapper;
import com.backend.domicare.model.Product;
import com.backend.domicare.repository.ProductsRepository;
import com.backend.domicare.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements  ProductService {
    private final ProductsRepository productRepository;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        Product product = ProductMapper.INSTANCE.convertToProduct(productDTO);
        if (product == null) {
            throw  new NotFoundException("Product not found");
        }
        productRepository.save(product);

        return ProductMapper.INSTANCE.convertToProductDTO(product);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO product) {
        boolean isExist = productRepository.existsById(id);
        if (!isExist) {
            throw new NotFoundException("Product not found");
        }
        Product productEntity = ProductMapper.INSTANCE.convertToProduct(product);
        productEntity.setId(id);
        productRepository.save(productEntity);
        return ProductMapper.INSTANCE.convertToProductDTO(productEntity);
    }
    
    @Override
    public void deleteProduct(Long id) {
        boolean isExist = productRepository.existsById(id);
        if (!isExist) {
            throw new NotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }
    
    @Override
    public ProductDTO fetchProductById(Long id) {
        boolean isExist = productRepository.existsById(id);
        if (!isExist) {
            throw new NotFoundException("Product not found");
        }
        return ProductMapper.INSTANCE.convertToProductDTO(productRepository.findById(id).get());
    }
}
