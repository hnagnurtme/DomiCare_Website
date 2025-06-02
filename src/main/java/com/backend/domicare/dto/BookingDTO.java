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
    private Double totalPrice;
    private String note;
    private Instant startTime;
    private List<ProductDTO> products;
    private UserDTO userDTO;
    private UserDTO saleDTO;
    private Boolean isPeriodic;
    private BookingStatus bookingStatus;
    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;
    private String phone;
}
