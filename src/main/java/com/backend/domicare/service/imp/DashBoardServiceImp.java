package com.backend.domicare.service.imp;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.backend.domicare.dto.DashboardSummaryDTO;
import com.backend.domicare.dto.request.LocalDateRequest;
import com.backend.domicare.dto.response.BookingOverview;
import com.backend.domicare.dto.response.ChartResponse;
import com.backend.domicare.dto.response.OverviewResponse;
import com.backend.domicare.dto.response.TopSaleResponse;
import com.backend.domicare.exception.InvalidDateException;
import com.backend.domicare.model.BookingStatus;
import com.backend.domicare.service.BookingService;
import com.backend.domicare.service.DashBoardService;
import com.backend.domicare.service.ReviewService;
import com.backend.domicare.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashBoardServiceImp implements DashBoardService {
    private final UserService userService;
    private final BookingService bookingService;
    private final ReviewService reviewService;

    @Override
    public OverviewResponse getDashboardSummary(LocalDateRequest localDateRequest) {
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
        bookings.setValue(bookingService.getTotalSuccessBooking(startDate, endDate).toString());
        bookings.setChange(calculateChange(bookingService.getTotalSuccessBooking(startDate, endDate),
                bookingService.getTotalSuccessBooking(prevStart, prevEnd)).toString());
        summaryDTO.put("bookings", bookings);

        DashboardSummaryDTO totalRevenue = new DashboardSummaryDTO();
        totalRevenue.setValue(bookingService.getTotalRevenue(startDate, endDate).toString());
        totalRevenue.setChange(calculateChange(bookingService.getTotalRevenue(startDate, endDate),
                bookingService.getTotalRevenue(prevStart, prevEnd)).toString());
        summaryDTO.put("totalRevenue", totalRevenue);

        DashboardSummaryDTO totalusers = new DashboardSummaryDTO();
        totalusers.setValue(userService.countNewUserBetween(startDate, endDate).toString());
        totalusers.setChange(calculateChange(userService.countNewUserBetween(startDate, endDate),
                userService.countNewUserBetween(prevStart, prevEnd)).toString());
        summaryDTO.put("totalUsers", totalusers);

        DashboardSummaryDTO reviews = new DashboardSummaryDTO();
        reviews.setValue(reviewService.countTotalReviews(startDate, endDate).toString());
        reviews.setChange(calculateChange(reviewService.countTotalReviews(startDate, endDate),
                reviewService.countTotalReviews(prevStart, prevEnd)).toString());
        summaryDTO.put("reviews", reviews);

        OverviewResponse result = new OverviewResponse();
        result.setDashboardSummary(summaryDTO);
        result.setBookingOverview(this.getBookingOverview(localDateRequest));
        return result;

    }

    private Float calculateChange(Long current, Long previous) {
        if (previous == null || previous == 0) {
            return current > 0 ? 100f : 0f;
        }
        float change = ((current - previous) * 100f) / previous;
        return Math.round(change * 100f) / 100f;
    }

    public Long getTotalBooking(LocalDateRequest localDateRequest) {
        LocalDate startDate = localDateRequest.getStartDate();
        LocalDate endDate = localDateRequest.getEndDate();
        if (startDate == null || endDate == null) {
            throw new InvalidDateException("Start date and end date must not be null.");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateException("Start date must be before or equal to end date.");
        }
        return bookingService.getTotalSuccessBooking(startDate, endDate);
    }

    @Override
    public ChartResponse getTotalRevenue12Months() {
        ChartResponse chartResponse = new ChartResponse();
        Map<String, Long> revenueMap = new LinkedHashMap<>();

        // Khởi tạo map với định dạng "Th 01" → "Th 12"
        for (int i = 1; i <= 12; i++) {
            revenueMap.put(String.format("Th %02d", i), 0L);
        }

        LocalDate currentDate = LocalDate.now();

        for (int i = 11; i >= 0; i--) {
            LocalDate monthStart = currentDate.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

            Long totalRevenue = bookingService.getTotalRevenue(monthStart, monthEnd);
            if (totalRevenue == null)
                totalRevenue = 0L;

            String monthKey = String.format("Th %02d", monthStart.getMonthValue());
            revenueMap.put(monthKey, totalRevenue);
        }

        chartResponse.setTotalRevenue(revenueMap);

        // Tính tăng trưởng tháng này so với tháng trước
        String currentMonthKey = String.format("Th %02d", currentDate.getMonthValue());
        String previousMonthKey = String.format("Th %02d", currentDate.minusMonths(1).getMonthValue());

        Long currentMonthRevenue = revenueMap.getOrDefault(currentMonthKey, 0L);
        Long previousMonthRevenue = revenueMap.getOrDefault(previousMonthKey, 0L);

        float growthRate = 0f;
        if (previousMonthRevenue > 0) {
            growthRate = ((float) (currentMonthRevenue - previousMonthRevenue) / previousMonthRevenue) * 100;
            growthRate = Math.round(growthRate * 100f) / 100f; // Làm tròn 2 chữ số thập phân
        }

        chartResponse.setGrowthRate(growthRate);
        return chartResponse;
    }

    @Override
    public BookingOverview getBookingOverview(LocalDateRequest localDateRequest) {
        LocalDate startDate = localDateRequest.getStartDate();
        LocalDate endDate = localDateRequest.getEndDate();
        if (startDate == null || endDate == null) {
            throw new InvalidDateException("Start date and end date must not be null.");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateException("Start date must be before or equal to end date.");
        }
        BookingOverview bookingOverview = new BookingOverview();
        bookingOverview.setTotalBookings(bookingService.countTotalBooking(localDateRequest));
        bookingOverview.setTotalAcceptedBookings(
                bookingService.countTotalBookingByStatus(BookingStatus.ACCEPTED, localDateRequest));
        bookingOverview.setTotalRejectedBookings(
                bookingService.countTotalBookingByStatus(BookingStatus.REJECTED, localDateRequest));
        bookingOverview.setTotalFailedBookings(
                bookingService.countTotalBookingByStatus(BookingStatus.FAILED, localDateRequest));
        bookingOverview.setTotalSuccessBookings(
                bookingService.countTotalBookingByStatus(BookingStatus.SUCCESS, localDateRequest));
        bookingOverview.setTotalPendingBookings(
                bookingService.countTotalBookingByStatus(BookingStatus.PENDING, localDateRequest));
        bookingOverview.setTotalRevenueBookings(bookingService.getTotalRevenue(startDate, endDate));
        return bookingOverview;
    }


    @Override
    public  List<TopSaleResponse> getTopSale(LocalDateRequest localDateRequest){
        LocalDate startDate = localDateRequest.getStartDate();
        LocalDate endDate = localDateRequest.getEndDate();
        if (startDate == null || endDate == null) {
            throw new InvalidDateException("Start date and end date must not be null.");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateException("Start date must be before or equal to end date.");
        }
        return bookingService.getFiveTopSale(localDateRequest);
    }

}
