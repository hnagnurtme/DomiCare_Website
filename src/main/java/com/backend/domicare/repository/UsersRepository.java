package com.backend.domicare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Token;
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

    @Modifying
    @Query(value = "DELETE FROM users_roles WHERE user_id = :userId", nativeQuery = true)
    void deleteRolesByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Token t JOIN FETCH t.user WHERE t.refreshToken = :refreshToken")
    Optional<Token> findByRefreshTokenWithUser(@Param("refreshToken") String refreshToken);
}
