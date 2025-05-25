package com.backend.domicare.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Booking;
import com.backend.domicare.model.BookingStatus;


@Repository
public interface BookingsRepository extends JpaRepository<Booking, Long> {
    
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    List<Booking> findByUserId(@Param("userId") Long userId);

    @Query("DELETE FROM Booking b WHERE b.id IN :ids")
    void softDeleteByIds(@Param("ids") List<Long> ids);

    @Query("select b from Booking b join fetch b.user join fetch b.products where b.id = :id")
    Optional<Booking> findByIdWithUserAndProducts(@Param("id") Long id);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingStatus = :status")
    Long countBookingsByStatus(@Param("status") BookingStatus status);
    @Query("SELECT b FROM Booking b WHERE b.updateBy = :updateBy AND b.bookingStatus = :status")
    List<Booking> findByUpdateByAndStatus(@Param("updateBy") String updateBy, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.createBy = :createBy AND b.bookingStatus = :status")
    List<Booking> findByCreateByAndStatus(@Param("createBy") String createBy, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.updateBy = :updateBy AND b.bookingStatus IN :status")
    List<Booking> findByUpdateByAndStatusIn(@Param("updateBy") String updateBy, @Param("status") List<BookingStatus> status);
}
