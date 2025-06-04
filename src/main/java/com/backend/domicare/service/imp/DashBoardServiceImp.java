package com.backend.domicare.service.imp;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.backend.domicare.dto.DashboardSummaryDTO;
import com.backend.domicare.dto.request.LocalDateRequest;
import com.backend.domicare.dto.response.BookingOverview;
import com.backend.domicare.dto.response.OverviewResponse;
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

        result.setTotalRevenue12Months(this.getTotalRevenue12Months());
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
    public Map<String, Long> getTotalRevenue12Months() {
        Map<String, Long> revenueMap = new LinkedHashMap<>();

        // Khởi tạo tháng theo thứ tự cố định
        String[] monthsOrder = {
                "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"
        };
        for (String month : monthsOrder) {
            revenueMap.put(month, 0L); // gán giá trị mặc định 0
        }

        LocalDate currentDate = LocalDate.now();

        // Duyệt 12 tháng gần nhất
        for (int i = 11; i >= 0; i--) {
            LocalDate monthStart = currentDate.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
            Long totalRevenue = bookingService.getTotalRevenue(monthStart, monthEnd);
            if (totalRevenue == null)
                totalRevenue = 0L;

            // Lấy tên tháng (uppercase)
            String monthName = monthStart.getMonth().toString();

            // Cập nhật lại giá trị vào map
            revenueMap.put(monthName, totalRevenue);
        }

        return revenueMap;
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

}
