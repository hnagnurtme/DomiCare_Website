package com.backend.domicare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopSaleResponse {
    private Long id;
    private String name;
    private String avatar;
    private String email;
    private Double totalSalePrice;
    private Float totalSuccessBookingPercent;
}
