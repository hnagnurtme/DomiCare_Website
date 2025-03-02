package com.backend.domicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
    
}
