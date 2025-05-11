package com.backend.domicare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.File;

@Repository
public interface FilesRepository extends JpaRepository<File, Long> {
    File findByName(String name);
    boolean existsByName(String name);
    
    @Query("SELECT COUNT(f) > 0 FROM File f WHERE f.url = :url")
    boolean existsByUrl(String url);

    @Modifying
    @Query("UPDATE File f SET f.isDeleted = true WHERE f.id = :id")
    void softDeleteById(Long id);
    
    @Query("SELECT f FROM File f WHERE f.url = :url")
    File findByUrl(String url);

    @Query("SELECT f FROM File f WHERE f.url IN :urls")
    List<File> findByUrls(List<String> urls);
}
