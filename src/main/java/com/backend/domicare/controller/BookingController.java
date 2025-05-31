package com.backend.domicare.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.dto.BookingDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.dto.request.BookingRequest;
import com.backend.domicare.dto.request.UpdateBookingRequest;
import com.backend.domicare.dto.request.UpdateBookingStatusRequest;
import com.backend.domicare.model.Booking;
import com.backend.domicare.model.Category;
import com.backend.domicare.service.BookingService;
import com.backend.domicare.utils.FormatStringAccents;
import com.turkraft.springfilter.boot.Filter;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/bookings")
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.addBooking(bookingRequest));
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.fetchBookingById(id));
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/bookings")
    public ResponseEntity<BookingDTO> updateBooking(@Valid @RequestBody UpdateBookingRequest bookingRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.updateBooking(bookingRequest));
    }

    @PutMapping("/bookings/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @RequestBody UpdateBookingStatusRequest bookingRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.updateBookingStatus(bookingRequest));
    }

    @GetMapping("/bookings")
    public ResponseEntity<ResultPagingDTO> getBookings(
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Search by category name") @RequestParam(required = false) String searchName,
            @Parameter(description = "Field to sort by") @RequestParam(required = false, defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @Parameter(description = "Additional filter specification") @Filter Specification<Booking> spec, 
            Pageable pageable) {

        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 20; 

        Sort sort = Sort.by(sortBy);
        if (sortDirection.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        pageable = PageRequest.of(page - 1, size, sort);

        if (searchName != null && !searchName.trim().isEmpty()) {
            String cleanSearchName = FormatStringAccents.removeTones(searchName.toLowerCase().trim());
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("nameUnsigned")), "%" + cleanSearchName + "%"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.bookingService.getAllBooking(spec, pageable));
    }
}
