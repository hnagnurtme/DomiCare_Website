package com.backend.domicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Payment;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment, Long> {
    
}
