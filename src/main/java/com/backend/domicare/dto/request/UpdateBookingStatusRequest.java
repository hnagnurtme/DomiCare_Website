package com.backend.domicare.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class UpdateBookingStatusRequest {
    @NotNull(message = "Không được để trống bookingId")
    private Long bookingId;
    @NotBlank(message = "Không được để trống trạng thái")
    private String status;
}
