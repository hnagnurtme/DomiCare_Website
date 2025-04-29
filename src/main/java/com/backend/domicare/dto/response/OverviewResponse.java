package com.backend.domicare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OverviewResponse {
    private Long totalCustomers;
    private Long totalSales;
    private Long totalOrders;
    private Long totalProducts;
    private Long totalCategory;
    private Long totalAcceptedBookings;
    private Long totalPendingBookings;
    private Long totalReviews;
}
