package com.backend.domicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.File;

@Repository
public interface FilesRepository extends JpaRepository<File, Long> {
    File findByName(String name);
    boolean existsByName(String name);

    @Modifying
    @Query("UPDATE File f SET f.isDeleted = true WHERE f.id = :id")
    void softDeleteById(Long id);
}
