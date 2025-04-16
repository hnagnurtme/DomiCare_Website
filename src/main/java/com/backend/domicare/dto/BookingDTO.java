package com.backend.domicare.dto;

import java.time.Instant;
import java.util.List;

import com.backend.domicare.model.BookingStatus;

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
    private Double totalHours;
    private Double totalPrice;
    private String note;
    private Boolean isPeriodic;
    private BookingStatus bookingStatus;
    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;
    private List<ProductDTO> products;
    private UserDTO userDTO;
}
