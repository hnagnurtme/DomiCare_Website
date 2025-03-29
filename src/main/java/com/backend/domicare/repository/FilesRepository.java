package com.backend.domicare.repository;

import com.backend.domicare.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<File, Long> {
}
