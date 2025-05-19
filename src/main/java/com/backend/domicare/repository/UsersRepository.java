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

    // Find all users by id join Booking anf BookingStatus and createBy
    @Query("SELECT u FROM User u JOIN u.bookings b WHERE u.id = :id AND b.bookingStatus = :status AND b.createBy = :createBy")
    Optional<User> findByIdAndBookingStatus(@Param("id") Long id, @Param("status") String status , @Param("createBy") String createBy);

    // Find all users by id join Booking anf BookingStatus and updateBy
    @Query("SELECT u FROM User u JOIN u.bookings b WHERE u.id = :id AND b.bookingStatus = :status AND b.updateBy = :updateBy")
    Optional<User> findByIdAndBookingStatusUpdateBy(@Param("id") Long id, @Param("status") String status , @Param("updateBy") String updateBy);

}
