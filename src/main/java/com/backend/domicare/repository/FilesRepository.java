package com.backend.domicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.File;

@Repository
public interface FilesRepository extends JpaRepository<File, Long> {
    File findByName(String name);
    boolean existsByName(String name);
}
