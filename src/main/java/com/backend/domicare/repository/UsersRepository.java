package com.backend.domicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    
}
