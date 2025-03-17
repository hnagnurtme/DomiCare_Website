package com.backend.domicare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.FileEntity;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {
    List<FileEntity> findByName(String name);
}