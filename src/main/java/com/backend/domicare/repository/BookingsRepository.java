package com.backend.domicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Booking;

@Repository
public interface BookingsRepository extends JpaRepository<Booking, Long> {
    
}
