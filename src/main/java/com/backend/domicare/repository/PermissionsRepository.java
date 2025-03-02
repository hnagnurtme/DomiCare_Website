package com.backend.domicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Permission;

@Repository
public interface PermissionsRepository  extends JpaRepository<Permission, Long> {
    
}
