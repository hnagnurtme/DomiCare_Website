package com.backend.domicare.service.imp;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.domicare.dto.BookingDTO;
import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.request.BookingRequest;
import com.backend.domicare.dto.request.UpdateBookingRequest;
import com.backend.domicare.dto.request.UpdateBookingStatusRequest;
import com.backend.domicare.exception.AlreadyRegisterUserException;
import com.backend.domicare.exception.BookingStatusException;
import com.backend.domicare.exception.NotFoundBookingException;
import com.backend.domicare.exception.NotFoundUserException;
import com.backend.domicare.mapper.BookingMapper;
import com.backend.domicare.model.Booking;
import com.backend.domicare.model.BookingStatus;
import com.backend.domicare.model.Product;
import com.backend.domicare.model.User;
import com.backend.domicare.repository.BookingsRepository;
import com.backend.domicare.repository.UsersRepository;
import com.backend.domicare.security.jwt.JwtTokenManager;
import com.backend.domicare.service.BookingService;
import com.backend.domicare.service.EmailSendingService;
import com.backend.domicare.service.ProductService;
import com.backend.domicare.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImp implements BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImp.class);

    private final BookingsRepository bookingRepository;
    private final UsersRepository userRepository;
    private final ProductService productService;
    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;
    private final EmailSendingService emailSendingService;

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
        if (booking.getBookingStatus() == BookingStatus.ACCEPTED
                || booking.getBookingStatus() == BookingStatus.CANCELLED) {
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
    public BookingDTO updateBooking(UpdateBookingRequest request) {
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

        // Recalculate price if total hours changed

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
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            logger.warn("Cannot update booking with ID: {} due to status: {}", id, booking.getBookingStatus());
            throw new BookingStatusException("Cannot update booking with status: " + booking.getBookingStatus());
        }

        booking.setBookingStatus(newStatus);
        if(newStatus == BookingStatus.REJECTED) {
            String formattedStartTime = booking.getStartTime().toString(); // Convert Instant to String
            emailSendingService.sendRejectToUser(booking.getUser().getEmail(), booking.getProducts().get(0).getName(), formattedStartTime, "Hệ thống không thể đáp ứng yêu cầu của bạn", booking.getUser().getName());
        }
        Booking updatedBooking = bookingRepository.save(booking);

        logger.info("Booking status updated successfully for ID: {}", id);
        return BookingMapper.INSTANCE.convertToBookingDTO(updatedBooking);
    }

    @Override
    @Transactional
    public BookingDTO addBooking(BookingRequest request) {

        User user;

        String guestEmail = request.getGuestEmail();

        if (guestEmail != null && !guestEmail.isEmpty()) {

            User existingUser = userRepository.findByEmail(guestEmail);
            if (existingUser != null) {
                if (existingUser.isActive()) {
                    throw new AlreadyRegisterUserException("Email đã được đăng ký");
                } else {
                    
                    user = existingUser;
                }
            } else {
                // Không tồn tại user cũ → Tạo mới ở đây luôn
                String randomPassword = jwtTokenManager.generateRandomPassword();
                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(guestEmail);
                userDTO.setName(request.getName());
                userDTO.setPhone(request.getPhone());
                userDTO.setAddress(request.getAddress());
                userDTO.setPassword(randomPassword);
                userDTO.setIsActive(false);
                userDTO.setEmailConfirmed(true);

                UserDTO newUserDTO = userService.saveUser(userDTO);
                user = userRepository.findByEmail(newUserDTO.getEmail());

                emailSendingService.sendPasswordToUser(guestEmail, newUserDTO.getName(), randomPassword);
            }

        } else {
            Optional<String> currentUserLogin = JwtTokenManager.getCurrentUserLogin();
            if (currentUserLogin.isPresent()) {
                user = userRepository.findByEmail(currentUserLogin.get());
            } else {
                throw new NotFoundUserException("Không tìm thấy người dùng");
            }
        }
        Booking booking = BookingMapper.INSTANCE.convertToBooking(request);
        booking.setUser(user);

        List<Long> productIds = request.getProductIds();
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product IDs cannot be null or empty");
        }
        // Set products
        List<Product> products = productService.findAllByIdIn(productIds);
        booking.setProducts(products);
        Double totalPrice = 0.0;
        for (Product product : products) {
            totalPrice += product.getPrice();
        }
        booking.setTotalPrice(totalPrice);
        booking.setBookingStatus(BookingStatus.PENDING);

        Booking bookingEntity = bookingRepository.save(booking);
        logger.info("Booking created successfully with ID: {}", booking.getId());
        return BookingMapper.INSTANCE.convertToBookingDTO(bookingEntity);

    }
}
