package com.backend.domicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Role;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {
    public Role findByName(String name);
}
