package com.backend.domicare.dto.response;

import java.util.Map;

import com.backend.domicare.dto.DashboardSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OverviewResponse {
    Map<String, DashboardSummaryDTO> dashboardSummary;
    BookingOverview bookingOverview;
    Map<String, Long> totalRevenue12Months;
}
