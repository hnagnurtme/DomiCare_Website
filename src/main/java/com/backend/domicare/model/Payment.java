package com.backend.domicare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PAYMENTS")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    private String paymentDate;
    private Float totalAmount;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
}

enum PaymentMethod {
    CASH, CREDIT_CARD, DEBIT_CARD, PAYPAL
}

enum PaymentStatus {
    PENDING, PAID, CANCELLED
}
