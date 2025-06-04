package com.backend.domicare.dto.request;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BookingRequest {
    private String name;
    @NotNull(message = "Không được để trống số điện thoại")
    private String phone;
    @NotNull(message = "Không được để trống địa chỉ")
    private String address;

    @NotNull(message = "Không được để trống sản phẩm")
    private List<Long> productIds;
    @NotNull(message = "Chọn định kì hoặc không")
    private Boolean isPeriodic;
    private String note;
    @NotNull(message = "Không được để trống thời gian bắt đầu")
    private Instant startTime;

    private String guestEmail;

}
