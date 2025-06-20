package com.backend.domicare.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.backend.domicare.dto.BookingDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.dto.request.BookingRequest;
import com.backend.domicare.dto.request.LocalDateRequest;
import com.backend.domicare.dto.request.UpdateBookingRequest;
import com.backend.domicare.dto.request.UpdateBookingStatusRequest;
import com.backend.domicare.dto.response.MiniBookingResponse;
import com.backend.domicare.dto.response.TopSaleResponse;
import com.backend.domicare.model.Booking;
import com.backend.domicare.model.BookingStatus;


public interface  BookingService {
    public MiniBookingResponse addBooking(BookingRequest request);
    public MiniBookingResponse fetchBookingById(Long id);
    public MiniBookingResponse deleteBooking(Long id);
    public MiniBookingResponse updateBooking(UpdateBookingRequest request);
    public ResultPagingDTO getAllBooking(Specification<Booking> spec,Pageable pageable);
    public List<BookingDTO> getAllBookingsByUserId(Long userId);
    public MiniBookingResponse updateBookingStatus(UpdateBookingStatusRequest request);
    public Long getTotalSuccessBooking(LocalDate startDate, LocalDate endDate);
    public Long getTotalRevenue(LocalDate startDate, LocalDate endDate);
    public Long countTotalBookingByStatus(BookingStatus status, LocalDateRequest localDateRequest);
    public Long countTotalBooking(LocalDateRequest localDateRequest);

    public List<TopSaleResponse> getFiveTopSale(LocalDateRequest localDateRequest);
}
