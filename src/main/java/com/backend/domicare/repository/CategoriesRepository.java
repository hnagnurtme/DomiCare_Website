package com.backend.domicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Category;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Long> , JpaSpecificationExecutor<Category>{
    Category findByName(String name);
}
