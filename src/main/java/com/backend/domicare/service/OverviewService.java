package com.backend.domicare.service;

import com.backend.domicare.dto.response.OverviewResponse;

public interface OverviewService {
    public Long getTotalCustomers();
    public Long getTotalSales();
    public Long getTotalOrders();
    public Long getTotalProducts();
    public Long getTotalCategory();
    public Long getTotalAcceptedBookings();
    public Long getTotalPendingBookings();
    public Long getTotalReviews();
    public OverviewResponse getOverviews();
}
