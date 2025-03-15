package com.backend.domicare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.User;


@Repository
public interface UsersRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    public User findByEmail(String email);
    public void deleteUserById(Long id);
    public User findUserById(Long id);
    public User findByEmailAndPassword(String email, String password);
    public boolean existsByEmail(String email);
    public Optional<User> findByEmailConfirmationToken(String emailConfirmationToken);
    public Optional<User> findByGoogleId(String googleId);
}
