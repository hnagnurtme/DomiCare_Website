package com.backend.domicare.dto.request;

import java.time.Instant;

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
public class UpdateBookingRequest {
    @NotNull(message = "Không được để trống tên người đặt")
    private String name;
    @NotNull(message = "Không được để trống sản phẩm")
    private Long bookingId;
    @NotNull(message = "Không được để trống địa chỉ")
    private String address;
    @NotNull(message = "Chọn định kì hoặc không")
    private Boolean isPeriodic;
    private String note;
    @NotNull(message = "Không được để trống thời gian bắt đầu")
    private Instant startTime; 
    @NotBlank(message = "Không được để trống trạng thái")
    private String status;
    @NotNull(message = "Không được để trống số điện thoại")
    private String phone;

    @NotNull(message = "Không được để trống ID sản phẩm")
    private Long productId;
}
