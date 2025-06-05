package com.backend.domicare.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.domicare.dto.request.LocalDateRequest;
import com.backend.domicare.dto.response.ChartResponse;
import com.backend.domicare.dto.response.OverviewResponse;
import com.backend.domicare.dto.response.TopSaleResponse;
import com.backend.domicare.service.DashBoardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DashBoardController {
    private final DashBoardService dashboardService;

    @PostMapping("/dashboard/summary")
    public ResponseEntity<OverviewResponse> getDashboardSummary(@Valid @RequestBody LocalDateRequest localDateRequest) {
        OverviewResponse summary = dashboardService.getDashboardSummary(localDateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(summary);
    }


    @GetMapping("/dashboard/chart")
    public ResponseEntity<ChartResponse> getTotalRevenue12Months() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(dashboardService.getTotalRevenue12Months());
    }
    
    @PostMapping("/dashboard/topsale")
    public ResponseEntity<List<TopSaleResponse>> getTopSale(@Valid @RequestBody LocalDateRequest localDateRequest) {
        List<TopSaleResponse> topSales = dashboardService.getTopSale(localDateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(topSales);
    }
}
