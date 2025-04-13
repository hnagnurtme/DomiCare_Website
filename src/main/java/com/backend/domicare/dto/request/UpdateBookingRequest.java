package com.backend.domicare.dto.request;

import jakarta.validation.constraints.NotNull;
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
public class UpdateBookingRequest {
    @NotNull(message = "Không được để trống sản phẩm")
    private Long bookingId;
    private String address;
    private Double totalHours;
    private String note;

}
