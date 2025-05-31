package com.backend.domicare.dto.response;

import java.time.Instant;
import java.util.List;

import com.backend.domicare.dto.ProductDTO;
import com.backend.domicare.dto.UserDTO;
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
public class MiniBookingResponse {
    private Long id;
    private String address;
    private Double totalPrice;
    private String note;
    private Instant startTime;
    private List<ProductMini> products;
    private UserMini userDTO;
    private Boolean isPeriodic;
    private BookingStatus bookingStatus;
    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;
}
