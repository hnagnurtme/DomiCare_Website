package com.backend.domicare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.dto.BookingDTO;
import com.backend.domicare.dto.request.BookingRequest;
import com.backend.domicare.dto.request.UpdateBookingRequest;
import com.backend.domicare.dto.request.UpdateBookingStatusRequest;
import com.backend.domicare.service.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // Add your endpoint methods here
    @PostMapping("/bookings")
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
     
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.addBooking(bookingRequest));
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        BookingDTO booking = bookingService.fetchBookingById(id);
        return ResponseEntity.status(HttpStatus.OK).body(booking);
    }
    

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/bookings")
    public ResponseEntity<BookingDTO> updateBooking(
        @Valid @RequestBody UpdateBookingRequest bookingRequest) {
        BookingDTO updatedBooking = bookingService.updateBooking(bookingRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBooking);
    }


    @PutMapping("/bookings/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(
        @RequestBody UpdateBookingStatusRequest bookingRequest) {
        BookingDTO updatedBooking = bookingService.updateBookingStatus(bookingRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBooking);
    }
}
