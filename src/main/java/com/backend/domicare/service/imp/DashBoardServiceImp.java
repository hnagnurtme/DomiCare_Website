package com.backend.domicare.service.imp;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.backend.domicare.dto.DashboardSummaryDTO;
import com.backend.domicare.dto.request.LocalDateRequest;
import com.backend.domicare.exception.InvalidDateException;
import com.backend.domicare.service.BookingService;
import com.backend.domicare.service.DashboardService;
import com.backend.domicare.service.ReviewService;
import com.backend.domicare.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImp implements DashboardService {
    private final UserService userService;
    private final BookingService bookingService;
    private final ReviewService reviewService;

    @Override
    public Map<String, DashboardSummaryDTO> getDashboardSummary(LocalDateRequest localDateRequest) {
        LocalDate startDate = localDateRequest.getStartDate();
        LocalDate endDate = localDateRequest.getEndDate();
        LocalDate prevStart = startDate.minusMonths(1);
        LocalDate prevEnd = endDate.minusMonths(1);
        if (startDate == null || endDate == null) {
            throw new InvalidDateException("Start date and end date must not be null.");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateException("Start date must be before or equal to end date.");
        }
        Map<String, DashboardSummaryDTO> summaryDTO = new HashMap<>();
        DashboardSummaryDTO bookings = new DashboardSummaryDTO();
        bookings.setValue(bookingService.getTotalBooking(startDate, endDate).toString());
        bookings.setChange(calculateChange(bookingService.getTotalBooking(startDate, endDate), bookingService.getTotalBooking(prevStart, prevEnd)).toString());
        summaryDTO.put("bookings", bookings);

        DashboardSummaryDTO totalRevenue = new DashboardSummaryDTO();
        totalRevenue.setValue(bookingService.getTotalRevenue(startDate, endDate).toString());
        totalRevenue.setChange(calculateChange(bookingService.getTotalRevenue(startDate, endDate), bookingService.getTotalRevenue(prevStart, prevEnd)).toString());
        summaryDTO.put("totalRevenue", totalRevenue);


        DashboardSummaryDTO totalusers = new DashboardSummaryDTO();
        totalusers.setValue(userService.countNewUserBetween(startDate,endDate).toString());
        totalusers.setChange(calculateChange(userService.countNewUserBetween(startDate, endDate), userService.countNewUserBetween(prevStart, prevEnd)).toString());
        summaryDTO.put("totalUsers", totalusers);

        DashboardSummaryDTO reviews = new DashboardSummaryDTO();
        reviews.setValue(reviewService.countTotalReviews(startDate, endDate).toString());
        reviews.setChange(calculateChange(reviewService.countTotalReviews(startDate, endDate), reviewService.countTotalReviews(prevStart, prevEnd)).toString());
        summaryDTO.put("reviews", reviews);
        return summaryDTO;
        
    }

    private Float calculateChange(Long current, Long previous) {
        if (previous == null || previous == 0) {
            return current > 0 ? 100f : 0f;
        }
        float change = ((current - previous) * 100f) / previous;
        return Math.round(change * 100f) / 100f;
    }

}
