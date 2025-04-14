package com.backend.domicare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Booking;

@Repository
public interface BookingsRepository extends JpaRepository<Booking, Long> {
    
    /**
     * Find all bookings by user ID
     * @param userId the ID of the user
     * @return list of bookings for the specified user
     */
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    List<Booking> findByUserId(@Param("userId") Long userId);
}
