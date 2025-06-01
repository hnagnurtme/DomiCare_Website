package com.backend.domicare.service.imp;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.backend.domicare.dto.response.ProductResponse;
import com.backend.domicare.exception.CategoryNotFoundException;
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
import com.backend.domicare.utils.FormatStringAccents;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImp implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImp.class);

    private final ProductsRepository productRepository;
    private final CategoriesRepository categoryRepository;
    private final FilesRepository fileRepository;

    @Override
    @Transactional
    public ProductDTO addProduct(AddProductRequest request) {
        logger.info("[Product] Adding a new product with name: {}", request.getName());

        Long categoryID = request.getCategoryId();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(request.getName());
        productDTO.setDescription(request.getDescription());
        productDTO.setPrice(request.getPrice());
        productDTO.setDiscount(request.getDiscount());

        String mainImageId = request.getMainImageId();
        List<String> landingImageIds = request.getLandingImageIds();

        Category category = categoryRepository.findByIdAndNotDeleted(categoryID);
        if (category == null) {
            logger.error("[Product] Category not found with ID: {}", categoryID);
            throw new CategoryNotFoundException("Category not found");
        }

        if (productRepository.existsByNameAndCategoryId(productDTO.getName(), categoryID)) {
            logger.error("[Product] Product with the same name already exists in category ID: {}", categoryID);
            throw new ProductNameAlreadyExists("Product with the same name already exists in this category");
        }

        if (mainImageId != null) {
            File mainImage = fileRepository.findByUrl(mainImageId);
            if (mainImage == null) {
                logger.error("[Product] Main image not found with ID: {}", mainImageId);
                throw new NotFoundFileException("Main image not found");
            }
            productDTO.setImage(mainImage.getUrl());
        }

        if (landingImageIds != null) {
            List<String> landingImages = new ArrayList<>();
            for (String imageId : landingImageIds) {
                File landingImage = fileRepository.findByUrl(imageId);
                if (landingImage == null) {
                    logger.error("[Product] Landing image not found with ID: {}", imageId);
                    throw new NotFoundFileException("Landing image not found");
                }
                landingImages.add(landingImage.getUrl());
            }
            productDTO.setLandingImages(landingImages);
        }

        Product productEntity = ProductMapper.INSTANCE.convertToProduct(productDTO);
        productEntity.setCategory(category);
        productEntity.setDeleted(false);
        productEntity.setNameUnsigned(FormatStringAccents.removeTones(productDTO.getName()));

        productRepository.save(productEntity);

        ProductDTO savedProductDTO = ProductMapper.INSTANCE.convertToProductDTO(productEntity);
        savedProductDTO.setCategoryID(categoryID);

        logger.info("[Product] Product added successfully with ID: {}", productEntity.getId());
        return savedProductDTO;
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(UpdateProductRequest request) {
        logger.info("[Product] Updating product with ID: {}", request.getOldProductId());

        ProductDTO productDTO = ProductMapper.INSTANCE.convertToProductDTOByUpdateProductRequest(request);
        Long id = request.getOldProductId();
        Long newCategoryID = productDTO.getCategoryID();
        Long oldCategoryID = request.getOldCategoryId();
        if (newCategoryID == null) {
            newCategoryID = oldCategoryID;
        }

        Category oldCategory = categoryRepository.findById(oldCategoryID)
                .orElseThrow(() -> {
                    logger.error("[Product] Old category not found with ID: {}", oldCategoryID);
                    return new CategoryNotFoundException("Old Category not found");
                });

        if (oldCategory.getProducts().stream().noneMatch(p -> p.getId().equals(id))) {
            logger.error("[Product] Product with ID: {} not found in old category ID: {}", id, oldCategoryID);
            throw new ProductNotInCategory("Product not found in the old category");
        }

        final Long finalNewCategoryID = newCategoryID;
        Category newCategory = categoryRepository.findById(finalNewCategoryID)
                .orElseThrow(() -> {
                    logger.error("[Product] New category not found with ID: {}", finalNewCategoryID);
                    return new CategoryNotFoundException("New Category not found");
                });

        Product productEntity = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("[Product] Product not found with ID: {}", id);
                    return new NotFoundProductException("Product not found");
                });

        if (newCategoryID != null && !newCategoryID.equals(oldCategoryID)) {
            List<Product> oldCategoryProducts = oldCategory.getProducts();
            oldCategoryProducts.removeIf(p -> p.getId().equals(id));
            categoryRepository.save(oldCategory);

            List<Product> newCategoryProducts = newCategory.getProducts();
            if (newCategoryProducts == null) {
                newCategoryProducts = new ArrayList<>();
            }
            newCategoryProducts.add(productEntity);
            newCategory.setProducts(newCategoryProducts);
            categoryRepository.save(newCategory);

            productEntity.setCategory(newCategory);
        }

        if (productDTO.getName() != null) {
            productEntity.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            productEntity.setDescription(productDTO.getDescription());
        }
        if (productDTO.getPrice() > 0) {
            productEntity.setPrice(productDTO.getPrice());
        }
        if (productDTO.getDiscount() != null) {
            productEntity.setDiscount(productDTO.getDiscount());
        }

        String mainImageId = request.getMainImageId();
        if (mainImageId != null) {
            File mainImage = fileRepository.findByUrl(mainImageId);
            if (mainImage == null) {
                logger.error("[Product] Main image not found with ID: {}", mainImageId);
                throw new NotFoundFileException("Main image not found");
            }
            productEntity.setImage(mainImage.getUrl());
        }
        List<String> landingImageIds = request.getLandingImages();
        if (landingImageIds != null && !landingImageIds.isEmpty()) {
            List<File> foundFiles = fileRepository.findByUrls(landingImageIds);

            if (foundFiles.size() != landingImageIds.size()) {
                logger.error("[Product] One or more landing images not found. Expected: {}, Found: {}",
                        landingImageIds.size(), foundFiles.size());
                throw new NotFoundFileException("Some landing images not found");
            }

            List<String> landingImages = foundFiles.stream()
                    .map(File::getUrl)
                    .toList();
            productEntity.setLandingImages(landingImages);
        }
        productRepository.save(productEntity);

        ProductDTO updatedProductDTO = ProductMapper.INSTANCE.convertToProductDTO(productEntity);
        updatedProductDTO.setCategoryID(newCategoryID);

        logger.info("[Product] Product updated successfully with ID: {}", id);
        return updatedProductDTO;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        logger.info("[Product] Deleting product with ID: {}", id);

        Product product = productRepository.findByIdAndNotDeleted(id);
        if (product == null) {
            logger.error("[Product] Product not found with ID: {}", id);
            throw new NotFoundProductException("Product not found");
        }

        product.setDeleted(true);
        productRepository.save(product);

        logger.info("[Product] Product deleted successfully with ID: {}", id);
    }

    @Override
    public ProductDTO fetchProductById(Long id) {
        logger.info("[Product] Fetching product with ID: {}", id);

        Product product = productRepository.findByIdAndNotDeleted(id);
        if (product == null) {
            logger.error("[Product] Product not found with ID: {}", id);
            throw new NotFoundProductException("Product not found");
        }

        logger.info("[Product] Product fetched successfully with ID: {}", id);
        return ProductMapper.INSTANCE.convertToProductDTO(product);
    }

    @Override
    public ResultPagingDTO getAllProducts(Specification<Product> spec, Pageable pageable) {
        logger.info("[Product] Fetching all products with pagination");
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDeleted"), false));
        Page<Product> allProducts = this.productRepository.findAll(spec, pageable);
        List<ProductResponse> productDTOs = ProductMapper.INSTANCE.convertToProductResponses(allProducts.getContent());

        ResultPagingDTO result = new ResultPagingDTO();
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();
        meta.setPage(allProducts.getNumber() + 1);
        meta.setSize(allProducts.getSize());
        meta.setTotal(allProducts.getTotalElements());
        meta.setTotalPages(allProducts.getTotalPages());
        result.setMeta(meta);
        result.setData(productDTOs);

        logger.info("[Product] Products fetched successfully with total elements: {}", allProducts.getTotalElements());
        return result;
    }

    @Override
    public ProductDTO addProductImage(AddProductImageRequest addProductImageRequest) {
        logger.info("[Product] Adding image to product with ID: {}", addProductImageRequest.getProductId());

        Long productId = addProductImageRequest.getProductId();
        String imageId = addProductImageRequest.getImageId();
        Boolean isMainImage = addProductImageRequest.getIsMainImage();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.error("[Product] Product not found with ID: {}", productId);
                    return new NotFoundProductException("Product not found");
                });

        File image = fileRepository.findByUrl(imageId);
        if (image == null) {
            logger.error("[Product] Image not found with ID: {}", imageId);
            throw new NotFoundFileException("Image not found");
        }

        if (isMainImage) {
            product.setImage(image.getUrl());
        } else {
            if (product.getLandingImages() == null) {
                product.setLandingImages(new ArrayList<>());
            }
            if (!product.getLandingImages().contains(image.getUrl())) {
                product.getLandingImages().add(image.getUrl());
            } else {
                logger.warn("[Product] Image with URL: {} already exists in the product's landing images",
                        image.getUrl());
                throw new UrlAlreadyExistsException("Image already exists in the product's landing images");
            }
        }

        productRepository.save(product);

        logger.info("[Product] Image added successfully to product with ID: {}", productId);
        return ProductMapper.INSTANCE.convertToProductDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllByIdIn(List<Long> ids) {
        logger.info("[Product] Fetching products with IDs: {}", ids);

        List<Product> products = productRepository.findAllById(ids);
        if (products.isEmpty()) {
            logger.error("[Product] No products found with the provided IDs: {}", ids);
            throw new NotFoundProductException("No products found with the provided IDs");
        }

        logger.info("[Product] Products fetched successfully with count: {}", products.size());
        return products;
    }
}
