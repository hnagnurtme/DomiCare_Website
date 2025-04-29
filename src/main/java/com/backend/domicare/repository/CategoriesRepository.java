package com.backend.domicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Category;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Long> , JpaSpecificationExecutor<Category>{
    Category findByName(String name);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    //Soft delete category by id
    @Modifying
    @Query("UPDATE Category c SET c.isDeleted = true WHERE c.id = :id")
    void softDeleteById(Long id);


}
