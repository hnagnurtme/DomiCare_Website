package com.backend.domicare.controller;

import java.util.List;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Dashboard", description = "Endpoints for dashboard data and analytics")
public class DashBoardController {
    private final DashBoardService dashboardService;

    @PostMapping("/dashboard/summary")
    @Operation(summary = "Get dashboard summary data", description = "Returns summary overview data for the dashboard within a specified date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard summary retrieved successfully", 
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                {
                  "dashboardSummary": {
                    "revenue": {
                      "value": "12,500,000đ",
                      "change": "15.2"
                    },
                    "bookings": {
                      "value": "42",
                      "change": "-8.5"
                    },
                    "customers": {
                      "value": "18",
                      "change": "-5.0"
                    },
                    "products": {
                      "value": "35",
                      "change": "2.1"
                    }
                  },
                  "bookingOverview": {
                    "totalBookings": 42,
                    "totalSuccessBookings": 35,
                    "totalFailedBookings": 2,
                    "totalAcceptedBookings": 38,
                    "totalRejectedBookings": 2,
                    "totalRevenueBookings": 12500000,
                    "totalPendingBookings": 5
                  }
                }
            """))),
        @ApiResponse(responseCode = "400", description = "Invalid date range"),
        @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<OverviewResponse> getDashboardSummary(@Valid @RequestBody LocalDateRequest localDateRequest) {
        OverviewResponse summary = dashboardService.getDashboardSummary(localDateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(summary);
    }


    @GetMapping("/dashboard/chart")
    @Operation(summary = "Get revenue chart data", description = "Returns revenue data for the last 12 months with growth rate")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chart data retrieved successfully", 
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                {
                  "totalRevenue": {
                    "Th 01": 12500000,
                    "Th 02": 15000000,
                    "Th 03": 17500000,
                    "Th 04": 20000000,
                    "Th 05": 22000000,
                    "Th 06": 24000000,
                    "Th 07": 26000000,
                    "Th 08": 28000000,
                    "Th 09": 30000000,
                    "Th 10": 32000000,
                    "Th 11": 34000000,
                    "Th 12": 36000000
                  },
                  "growthRate": 10.6
                }
            """))),
        @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<ChartResponse> getTotalRevenue12Months() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(dashboardService.getTotalRevenue12Months());
    }
    
    @PostMapping("/dashboard/topsale")
    @Operation(summary = "Get top sales staff data", description = "Returns information about top performing sales staff within a specified date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Top sales data retrieved successfully", 
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                [
                  {
                    "id": 1,
                    "name": "Nguyễn Văn A",
                    "avatar": "https://example.com/avatars/user1.jpg",
                    "email": "nguyenvana@domicare.com",
                    "totalSalePrice": 5200000.0,
                    "totalSuccessBookingPercent": 92.5
                  },
                  {
                    "id": 2,
                    "name": "Trần Thị B",
                    "avatar": "https://example.com/avatars/user2.jpg",
                    "email": "tranthib@domicare.com",
                    "totalSalePrice": 4800000.0,
                    "totalSuccessBookingPercent": 88.3
                  },
                  {
                    "id": 3,
                    "name": "Lê Văn C",
                    "avatar": "https://example.com/avatars/user3.jpg",
                    "email": "levanc@domicare.com",
                    "totalSalePrice": 3900000.0,
                    "totalSuccessBookingPercent": 85.7
                  }
                ]
            """))),
        @ApiResponse(responseCode = "400", description = "Invalid date range"),
        @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<List<TopSaleResponse>> getTopSale(@Valid @RequestBody LocalDateRequest localDateRequest) {
        List<TopSaleResponse> topSales = dashboardService.getTopSale(localDateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(topSales);
    }
}
