package com.backend.domicare.dto;

import java.time.Instant;
import java.util.List;

import com.backend.domicare.model.Payment;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDTO {
    private Long id;
    private String address;
    private Instant bookingDate;
    private Float totalHours;
    private Float totalPrice;
    private String note;
   
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;
    private List<ProductDTO> products;
    private UserDTO userDTO;
    private Payment payment;
}

enum BookingStatus {
    PENDING, ACCEPTED, REJECTED, CANCELLED
}