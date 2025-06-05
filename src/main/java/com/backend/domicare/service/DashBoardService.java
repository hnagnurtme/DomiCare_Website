package com.backend.domicare.service;



import java.util.List;

import com.backend.domicare.dto.request.LocalDateRequest;
import com.backend.domicare.dto.response.BookingOverview;
import com.backend.domicare.dto.response.ChartResponse;
import com.backend.domicare.dto.response.OverviewResponse;
import com.backend.domicare.dto.response.TopSaleResponse;

public interface DashBoardService {
    OverviewResponse getDashboardSummary(LocalDateRequest localDateRequest);
    BookingOverview getBookingOverview(LocalDateRequest localDateRequest);
    ChartResponse getTotalRevenue12Months();
    List<TopSaleResponse> getTopSale(LocalDateRequest localDateRequest);
}
