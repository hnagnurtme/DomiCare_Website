package com.backend.domicare.dto.request;

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
    @NotNull(message = "Không được để trống địa chỉ")
    private String address;
    @NotNull(message = "Không được để trống thời gian")
    private Double totalHours;
    private String note;
    @NotNull(message = "Không được để trống người dùng")
    private Long userId;
    @NotNull(message = "Không được để trống sản phẩm")
    private List<Long> productIds;
}
