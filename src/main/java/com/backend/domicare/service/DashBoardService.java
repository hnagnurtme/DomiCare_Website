package com.backend.domicare.service;

import java.util.Map;


import com.backend.domicare.dto.request.LocalDateRequest;
import com.backend.domicare.dto.response.BookingOverview;
import com.backend.domicare.dto.response.OverviewResponse;

public interface DashBoardService {
    OverviewResponse getDashboardSummary(LocalDateRequest localDateRequest);
    BookingOverview getBookingOverview(LocalDateRequest localDateRequest);
    Map<String, Long > getTotalRevenue12Months();
}
