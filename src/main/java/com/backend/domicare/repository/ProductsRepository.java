package com.backend.domicare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Product;
@Repository
/**
 * Repository interface for managing Product entities.
 * This interface extends JpaRepository and JpaSpecificationExecutor to provide CRUD operations and query capabilities.
 */
public interface ProductsRepository extends JpaRepository<Product, Long> , JpaSpecificationExecutor<Product> {
    @Modifying
    @Query("UPDATE Product p SET p.isDeleted = true WHERE p.category.id = :categoryId")
    void softDeleteByCategoryId(Long categoryId);

    boolean existsByName(String name);
    boolean existsByNameAndCategoryId(String name, Long categoryId);


    //Soft delete product by id
    @Modifying
    @Query("UPDATE Product p SET p.isDeleted = true WHERE p.id = :id")
    void softDeleteById(Long id);


    //Soft delete product by category ids
    @Modifying
    @Query("UPDATE Product p SET p.isDeleted = true WHERE p.category.id IN :ids")
    void softDeleteByCategoryIds(List<Long> ids);


    @Query(value = "SELECT * FROM product WHERE CONVERT(name USING utf8) COLLATE utf8_general_ci LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<Product> searchAccentInsensitive(@Param("keyword") String keyword);

}
