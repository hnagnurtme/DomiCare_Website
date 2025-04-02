package com.backend.domicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> , JpaSpecificationExecutor<Product> {
    @Modifying
    @Query("DELETE FROM Product p WHERE p.category.id = :categoryId")
    void deleteByCategoryId(Long categoryId);

    boolean existsByName(String name);
    boolean existsByNameAndCategoryId(String name, Long categoryId);
}
