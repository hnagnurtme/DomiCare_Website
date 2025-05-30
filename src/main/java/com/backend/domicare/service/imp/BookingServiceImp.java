package com.backend.domicare.service.imp;

import java.util.ArrayList;
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

@Service
@Transactional
public class BookingServiceImp implements BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImp.class);

    private final BookingsRepository bookingRepository;
    private final UsersRepository userRepository;
    private final ProductService productService;
    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;
    private final EmailSendingService emailSendingService;

    public BookingServiceImp(
            BookingsRepository bookingRepository,
            UsersRepository userRepository,
            ProductService productService,
            @org.springframework.context.annotation.Lazy JwtTokenManager jwtTokenManager,
            UserService userService,
            EmailSendingService emailSendingService) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.productService = productService;
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
        this.emailSendingService = emailSendingService;
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

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            logger.warn("Cannot update booking with ID: {} due to status: {}", id, booking.getBookingStatus());
            throw new BookingStatusException("Cannot update booking with status: " + booking.getBookingStatus());
        }
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


        String emailSale = JwtTokenManager.getCurrentUserLogin()
                .orElseThrow(() -> new NotFoundUserException("User not found"));
        User saleuser = userRepository.findByEmail(emailSale);
        if (saleuser == null) {
            throw new NotFoundUserException("User not found with email: " + emailSale);
        }
        
        // Initialize metrics fields if they are null
        if (saleuser.getSaleTotalBookings() == null) {
            saleuser.setSaleTotalBookings(0L);
        }
        if (saleuser.getUserTotalSuccessBookings() == null) {
            saleuser.setUserTotalSuccessBookings(0L);
        }
        if (saleuser.getUserTotalFailedBookings() == null) {
            saleuser.setUserTotalFailedBookings(0L);
        }
        if (saleuser.getSaleSuccessPercent() == null) {
            saleuser.setSaleSuccessPercent(0.0);
        }

        BookingStatus newStatus;
        try {
            newStatus = BookingStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid booking status: " + statusStr);
        }

        Booking booking = bookingRepository.findByIdWithUserAndProducts(id)
                .orElseThrow(() -> new NotFoundBookingException("Booking not found with ID: " + id));

        User customer = booking.getUser();
        if (customer == null) {
            throw new NotFoundUserException("Customer not found for booking ID: " + id);
        }
        
        // Initialize customer metrics fields if they are null
        if (customer.getUserTotalSuccessBookings() == null) {
            customer.setUserTotalSuccessBookings(0L);
        }
        if (customer.getUserTotalFailedBookings() == null) {
            customer.setUserTotalFailedBookings(0L);
        }

        if (booking.getBookingStatus() == BookingStatus.PENDING) {
            switch (newStatus) {
                case ACCEPTED:
                    booking.setBookingStatus(BookingStatus.ACCEPTED);
                    this.emailSendingService.sendAcceptedToUser(
                            booking.getUser().getEmail(),
                            booking.getProducts().get(0).getName(),
                            booking.getCreateAt().toString(),
                            booking.getUser().getName());
                    booking.setSaleUser(saleuser);
                    saleuser.setSaleTotalBookings(saleuser.getSaleTotalBookings() + 1);
                    // Don't increment success booking counter yet, as the booking is only accepted, not completed
                    break;
                case REJECTED:
                    booking.setBookingStatus(BookingStatus.REJECTED);
                    this.emailSendingService.sendRejectToUser(
                            booking.getUser().getEmail(),
                            booking.getProducts().get(0).getName(),
                            booking.getCreateAt().toString(),
                            booking.getUser().getName());
                    booking.setSaleUser(saleuser);
                    // No need to update metrics for rejected bookings as they never reached the accepted state
                    break;
                default:
                    throw new BookingStatusException("Cannot update booking to status: " + newStatus);
            }
        } else {
            if (booking.getBookingStatus() == BookingStatus.ACCEPTED) {
                switch (newStatus) {
                    case FAILED:
                        booking.setBookingStatus(BookingStatus.FAILED);
                        // Increment failed bookings counter for customer
                        customer.setUserTotalFailedBookings(customer.getUserTotalFailedBookings() + 1);

                        // Calculate success percentage - avoid division by zero
                        calculateSuccessPercentage(saleuser);
                        break;

                    case SUCCESS:
                        booking.setBookingStatus(BookingStatus.SUCCESS);
                        // Increment success bookings counter for both sale user and customer
                        saleuser.setUserTotalSuccessBookings(saleuser.getUserTotalSuccessBookings() + 1);
                        customer.setUserTotalSuccessBookings(customer.getUserTotalSuccessBookings() + 1);
                        
                        // Calculate success percentage - avoid division by zero
                        calculateSuccessPercentage(saleuser);
                        break;
                    default:
                        throw new BookingStatusException("Cannot update booking to status: " + newStatus);
                }
            } else {
                throw new BookingStatusException("Cannot update booking to status: " + newStatus);
            }

        }
        

        List<Booking> bookings = saleuser.getBookings();
        if (bookings == null) {
            bookings = new ArrayList<>();
            bookings.add(booking);
        } else {
            bookings.add(booking);
        }
        saleuser.setBookings(bookings);
        userRepository.save(saleuser);

        booking.setUpdateBy(saleuser.getEmail());


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
        booking.setCreateBy(user.getEmail());

        List<Long> productIds = request.getProductIds();
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product IDs cannot be null or empty");
        }

        List<Product> products = productService.findAllByIdIn(productIds);
        booking.setProducts(products);
        Double totalPrice = 0.0;
        for (Product product : products) {
            totalPrice += product.getPrice();
        }
        booking.setTotalPrice(totalPrice);
        booking.setBookingStatus(BookingStatus.PENDING);

        Booking bookingEntity = bookingRepository.save(booking);
        logger.info("Booking created successfully with ID: {}", bookingEntity.getId());

        logger.info("User with email: {} has been associated with booking ID: {}", user.getEmail(),
                bookingEntity.getId());
        return BookingMapper.INSTANCE.convertToBookingDTO(bookingEntity);
    }
    
   
    private void calculateSuccessPercentage(User saleUser) {
        if (saleUser.getSaleTotalBookings() == null || saleUser.getSaleTotalBookings() == 0) {
            saleUser.setSaleSuccessPercent(0.0);
            return;
        }

        if (saleUser.getUserTotalSuccessBookings() == null) {
            saleUser.setUserTotalSuccessBookings(0L);
        }

        Double successPercentage = (double) saleUser.getUserTotalSuccessBookings() / saleUser.getSaleTotalBookings() * 100;
        saleUser.setSaleSuccessPercent(successPercentage);
    }
}
