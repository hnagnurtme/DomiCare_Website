package com.backend.domicare.service;

import java.util.List;

import com.backend.domicare.dto.BookingDTO;
import com.backend.domicare.dto.request.BookingRequest;
import com.backend.domicare.dto.request.UpdateBookingRequest;
import com.backend.domicare.dto.request.UpdateBookingStatusRequest;

public interface  BookingService {
    public BookingDTO addBooking(BookingRequest request);
    public BookingDTO fetchBookingById(Long id);
    public BookingDTO deleteBooking(Long id);
    public BookingDTO updateBooking(UpdateBookingRequest request);
    public List<BookingDTO> getAllBookingsByUserId(Long userId);
    public BookingDTO updateBookingStatus(UpdateBookingStatusRequest request);
}
