package com.backend.domicare.service.imp;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.backend.domicare.dto.BookingDTO;
import com.backend.domicare.dto.request.BookingRequest;
import com.backend.domicare.dto.request.UpdateBookingRequest;
import com.backend.domicare.dto.request.UpdateBookingStatusRequest;
import com.backend.domicare.exception.BookingStatusException;
import com.backend.domicare.exception.NotFoundBookingException;
import com.backend.domicare.exception.NotFoundProductException;
import com.backend.domicare.exception.NotFoundUserException;
import com.backend.domicare.mapper.BookingMapper;
import com.backend.domicare.model.Booking;
import com.backend.domicare.model.BookingStatus;
import com.backend.domicare.model.Product;
import com.backend.domicare.model.User;
import com.backend.domicare.repository.BookingsRepository;
import com.backend.domicare.repository.UsersRepository;
import com.backend.domicare.service.BookingService;
import com.backend.domicare.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImp implements BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImp.class);
    
    private final BookingsRepository bookingRepository;
    private final UsersRepository userRepository;
    private final ProductService productService;

    @Override
    @Transactional
    public BookingDTO addBooking(BookingRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Booking request cannot be null");
        }
        
        Long userId = request.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        List<Long> productIds = request.getProductIds();
        if (CollectionUtils.isEmpty(productIds)) {
            throw new IllegalArgumentException("Product IDs cannot be empty");
        }
        
        logger.info("Creating new booking for user ID: {}", userId);
        Booking bookingEntity = BookingMapper.INSTANCE.convertToBooking(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("User not found with ID: " + userId));
    
        List<Product> products = productService.findAllByIdIn(productIds);

        if (products.isEmpty()) {
            throw new NotFoundProductException("No products found with provided IDs");
        }

        List<Double> finalPrices = products.stream()
                .map(Product::getPriceAfterDiscount)
                .toList();

        Double totalPrice = this.calculateTotalPrice(finalPrices, request.getTotalHours());
        BookingStatus status = BookingStatus.PENDING;
        bookingEntity.setBookingDate(Instant.now());
        bookingEntity.setTotalPrice(totalPrice);
        bookingEntity.setUser(user);
        bookingEntity.setBookingStatus(status);
        bookingEntity.setProducts(products);

        Booking savedBooking = bookingRepository.save(bookingEntity);
        logger.info("Booking created successfully with ID: {}", savedBooking.getId());
        return BookingMapper.INSTANCE.convertToBookingDTO(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDTO fetchBookingById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        
        logger.debug("Fetching booking with ID: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundBookingException("Booking not found with ID: " + id));
        return BookingMapper.INSTANCE.convertToBookingDTO(booking);
    }

    @Override
    @Transactional
    public BookingDTO deleteBooking(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        
        logger.info("Attempting to delete booking with ID: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundBookingException("Booking not found with ID: " + id));

        // Validate status
        if (booking.getBookingStatus() == BookingStatus.ACCEPTED) {
            logger.warn("Cannot delete booking with ID: {} due to status: {}", id, booking.getBookingStatus());
            throw new BookingStatusException("Cannot delete booking with status: " + booking.getBookingStatus());
        }
        booking.setBookingStatus(BookingStatus.CANCELLED);
        Booking savedBooking = bookingRepository.save(booking);
        logger.info("Booking with ID: {} has been marked as CANCELLED", id);
        return BookingMapper.INSTANCE.convertToBookingDTO(savedBooking);
    }

    @Override
    @Transactional
    public BookingDTO updateBooking(UpdateBookingRequest request){
        if (request == null || request.getBookingId() == null) {
            throw new IllegalArgumentException("Update request or booking ID cannot be null");
        }
        
        Long id = request.getBookingId();
        logger.info("Updating booking with ID: {}", id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundBookingException("Booking not found with ID: " + id));
                
        // Validate status
        if (booking.getBookingStatus() == BookingStatus.ACCEPTED) {
            logger.warn("Cannot update booking with ID: {} due to status: {}", id, booking.getBookingStatus());
            throw new BookingStatusException("Cannot update booking with status: " + booking.getBookingStatus());
        }
        
        // Update booking details
        booking.setBookingDate(Instant.now());
        if (request.getAddress() != null) {
            booking.setAddress(request.getAddress());
        }
        if (request.getNote() != null) {
            booking.setNote(request.getNote());
        }
        
        // Recalculate price if total hours changed
        Double oldTotalHours = booking.getTotalHours();
        if (request.getTotalHours() != null && !Objects.equals(oldTotalHours, request.getTotalHours())) {
            booking.setTotalHours(request.getTotalHours());
            
            // Recalculate total price
            List<Double> finalPrices = booking.getProducts().stream()
                    .map(Product::getPriceAfterDiscount)
                    .toList();
            Double newTotalPrice = calculateTotalPrice(finalPrices, request.getTotalHours());
            booking.setTotalPrice(newTotalPrice);
            logger.info("Recalculated price for booking ID: {} from {} to {}", id, 
                    (oldTotalHours * booking.getTotalPrice() / request.getTotalHours()), newTotalPrice);
        }
        
        // Save updated booking
        Booking updatedBooking = bookingRepository.save(booking);
        logger.info("Booking updated successfully with ID: {}", id);
        return BookingMapper.INSTANCE.convertToBookingDTO(updatedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getAllBookingsByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        logger.debug("Fetching all bookings for user ID: {}", userId);
        
        // Check if user exists
        if (!userRepository.existsById(userId)) {
            throw new NotFoundUserException("User not found with ID: " + userId);
        }
        
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        if (bookings.isEmpty()) {
            logger.debug("No bookings found for user ID: {}", userId);
            return Collections.emptyList();
        }
        
        return bookings.stream()
                .map(BookingMapper.INSTANCE::convertToBookingDTO)
                .toList();
    }

    @Override
    public Double calculateTotalPrice(List<Double> finalPrices, Double totalHours) {
        if (CollectionUtils.isEmpty(finalPrices) || totalHours == null || totalHours <= 0) {
            throw new IllegalArgumentException("Invalid price list or total hours");
        }
        
        Double totalPrice = 0.0;
        for (Double price : finalPrices) {
            if (price != null) {
                totalPrice += price;
            }
        }
        return totalPrice * totalHours;
    }

    @Override
    @Transactional
    public BookingDTO updateBookingStatus(UpdateBookingStatusRequest request) {
        if (request == null || request.getBookingId() == null || request.getStatus() == null) {
            throw new IllegalArgumentException("Update request, booking ID, or status cannot be null");
        }
        
        Long id = request.getBookingId();
        String statusStr = request.getStatus();
        
        logger.info("Updating booking status for ID: {} to {}", id, statusStr);
        
        // Validate status string format
        BookingStatus newStatus;
        try {
            newStatus = BookingStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid booking status: " + statusStr);
        }
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundBookingException("Booking not found with ID: " + id));
        
        // Validate status transition
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            logger.warn("Cannot update booking with ID: {} due to status: {}", id, booking.getBookingStatus());
            throw new BookingStatusException("Cannot update booking with status: " + booking.getBookingStatus());
        }
        
        booking.setBookingStatus(newStatus);
        Booking updatedBooking = bookingRepository.save(booking);
        logger.info("Booking status updated successfully for ID: {}", id);
        return BookingMapper.INSTANCE.convertToBookingDTO(updatedBooking);
    }
}
