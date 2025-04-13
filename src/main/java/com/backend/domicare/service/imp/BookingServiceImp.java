package com.backend.domicare.service.imp;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

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
public class BookingServiceImp implements BookingService {
    private final BookingsRepository bookingRepository;
    private final UsersRepository userRepository;
    private final ProductService productService;

    @Override
    public BookingDTO addBooking(BookingRequest request) {
        Long userId = request.getUserId();
        List<Long> productIds = request.getProductIds();
        Booking bookingEntity = BookingMapper.INSTANCE.convertToBooking(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("Không tìm thấy người dùng với ID: " + userId));
    
        List<Product> products = productService.findAllByIdIn(productIds);

        if (products.isEmpty()) {
            throw new NotFoundProductException("");
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
        return BookingMapper.INSTANCE.convertToBookingDTO(savedBooking);
    }

    @Override
    public BookingDTO fetchBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundBookingException("Booking not found with ID: " + id));
        return BookingMapper.INSTANCE.convertToBookingDTO(booking);
    }

    @Override
    public BookingDTO deleteBooking(Long id) {
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundBookingException("Booking not found with ID: " + id));

        //Validate status
        if (booking.getBookingStatus() == BookingStatus.ACCEPTED) {
            throw new BookingStatusException("Cannot delete booking with status: " + booking.getBookingStatus());
        }
        booking.setBookingStatus(BookingStatus.CANCELLED);
        return BookingMapper.INSTANCE.convertToBookingDTO(bookingRepository.save(booking));
    }

    @Override
    public BookingDTO updateBooking(UpdateBookingRequest request){
        Long id = request.getBookingId();
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundBookingException("Booking not found with ID: " + id));
        // Validate status
        if (booking.getBookingStatus() == BookingStatus.ACCEPTED) {
            throw new BookingStatusException("Cannot update booking with status: " + booking.getBookingStatus());
        }
        // Update booking details
        booking.setBookingDate(Instant.now());
        if( request.getAddress() != null) {
            booking.setAddress(request.getAddress());
        }
        if( request.getNote() != null) {
            booking.setNote(request.getNote());
        }
        if( request.getTotalHours() != null) {
            booking.setTotalHours(request.getTotalHours());
        }
        
        // Save updated booking
        Booking updatedBooking = bookingRepository.save(booking);
        return BookingMapper.INSTANCE.convertToBookingDTO(updatedBooking);
    }

    @Override
    public List<BookingDTO> getAllBookingsByUserId(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllBookingsByUserId'");
    }

    @Override
    public Double calculateTotalPrice(List<Double> finalPrices, Double totalHours){
        Double totalPrice = 0.0;
        for (Double price : finalPrices) {
            totalPrice += price;
        }
        return totalPrice * totalHours;
    }

    @Override
    public BookingDTO updateBookingStatus(UpdateBookingStatusRequest request) {
        Long id = request.getBookingId();
        String status = request.getStatus();
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundBookingException("Booking not found with ID: " + id));
        // Validate status
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BookingStatusException("Cannot update booking with status: " + booking.getBookingStatus());
        }
        booking.setBookingStatus(BookingStatus.valueOf(status));
        return BookingMapper.INSTANCE.convertToBookingDTO(bookingRepository.save(booking));
    }
}
