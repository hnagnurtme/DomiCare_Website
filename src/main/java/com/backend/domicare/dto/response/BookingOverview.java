package com.backend.domicare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingOverview {
    private Long totalBookings;
    private Long totalSuccessBookings;
    private Long totalFailedBookings;
    private Long totalAcceptedBookings;
    private Long totalRejectedBookings;
    private Long totalRevenueBookings;
    private Long totalPendingBookings;
}
