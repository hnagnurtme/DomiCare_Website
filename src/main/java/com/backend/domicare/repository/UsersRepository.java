package com.backend.domicare.repository;

import java.time.Instant;
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

    User findByEmail(String email);

    void deleteUserById(Long id);

    User findUserById(Long id);

    User findByEmailAndPassword(String email, String password);
    
    boolean existsByEmail(String email);

    Optional<User> findByEmailConfirmationToken(String emailConfirmationToken);

    Optional<User> findByGoogleId(String googleId);

    @Query(value = "DELETE FROM users_roles WHERE user_id = :id", nativeQuery = true)
    void deleteAllRolesByUserID(@Param("id") Long id);

    @Query("SELECT t FROM Token t JOIN FETCH t.user WHERE t.refreshToken = :refreshToken")
    Optional<Token> findByRefreshTokenWithUser(@Param("refreshToken") String refreshToken);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName")
    Long countUsersByRoleName(@Param("roleName") String roleName);

    @Modifying
    @Query("UPDATE User u SET u.isDeleted = true WHERE u.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.isDeleted = false")
    Optional<User> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r " +
           "WHERE r.name = :roleName AND u.isDeleted = false")
    Long countAllUsers(@Param("roleName") String roleName);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r " +
           "WHERE r.name = :roleName AND u.isDeleted = false AND u.isActive = true")
    Long countAllActiveUsers(@Param("roleName") String roleName);

    
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r " +
           "WHERE r.name = :roleName AND u.isDeleted = false AND u.createAt >= CURRENT_DATE")
    Long countAllNewUsers(@Param("roleName") String roleName);
   
    //count all users created between startDate and endDate
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r " +
           "WHERE r.name = :roleName AND u.isDeleted = false AND u.createAt BETWEEN :startDate AND :endDate")
    Long countAllUsersBetween(@Param("roleName") String roleName, 
                              @Param("startDate") Instant startDate, 
                              @Param("endDate") Instant endDate);
}
