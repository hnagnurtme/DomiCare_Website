package com.backend.domicare.service.imp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backend.domicare.dto.BookingDTO;
import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.request.BookingRequest;
import com.backend.domicare.dto.request.LocalDateRequest;
import com.backend.domicare.dto.request.UpdateBookingRequest;
import com.backend.domicare.dto.request.UpdateBookingStatusRequest;
import com.backend.domicare.dto.response.MiniBookingResponse;
import com.backend.domicare.dto.response.TopSaleResponse;
import com.backend.domicare.exception.AlreadyPendingBooking;
import com.backend.domicare.exception.AlreadyRegisterUserException;
import com.backend.domicare.exception.AlreadySaleHandle;
import com.backend.domicare.exception.BookingStatusException;
import com.backend.domicare.exception.InvalidDateException;
import com.backend.domicare.exception.NotFoundBookingException;
import com.backend.domicare.exception.NotFoundUserException;
import com.backend.domicare.exception.TooMuchBookingException;
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


import com.backend.domicare.dto.paging.ResultPagingDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public BookingServiceImp(
            BookingsRepository bookingRepository,
            UsersRepository userRepository,
            ProductService productService,
            @Lazy JwtTokenManager jwtTokenManager,
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
    public MiniBookingResponse fetchBookingById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }

        logger.debug("[Booking] Fetching booking with ID: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundBookingException("Booking not found with ID: " + id));
        return BookingMapper.INSTANCE.convertToMiniBookingResponse(booking);
    }

    @Override
    @Transactional
    public MiniBookingResponse deleteBooking(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }

        logger.info("[Booking] Attempting to delete booking with ID: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundBookingException("Booking not found with ID: " + id));
        if (booking.getBookingStatus() == BookingStatus.ACCEPTED
                || booking.getBookingStatus() == BookingStatus.CANCELLED) {
            logger.warn("[Booking] Cannot delete booking with ID: {} due to status: {}", id,
                    booking.getBookingStatus());
            throw new BookingStatusException("Cannot delete booking with status: " + booking.getBookingStatus());
        }
        booking.setBookingStatus(BookingStatus.CANCELLED);
        Booking savedBooking = bookingRepository.save(booking);
        logger.info("[Booking] Booking with ID: {} has been marked as CANCELLED", id);
        messagingTemplate.convertAndSend("/topic/bookings/delete",
                BookingMapper.INSTANCE.convertToMiniBookingResponse(savedBooking));

        logger.info("[Booking] Booking deletion notification sent for booking ID: {}", id);
        return BookingMapper.INSTANCE.convertToMiniBookingResponse(savedBooking);
    }

    @Override
    @Transactional
    public MiniBookingResponse updateBooking(UpdateBookingRequest request) {
        if (request == null || request.getBookingId() == null) {
            throw new IllegalArgumentException("Update request or booking ID cannot be null");
        }

        Long id = request.getBookingId();
        logger.info("[Booking] Updating booking with ID: {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundBookingException("Booking not found with ID: " + id));
        

        if (booking.getBookingStatus() != BookingStatus.PENDING && booking.getBookingStatus() != BookingStatus.ACCEPTED ) {
            logger.warn("[Booking] Cannot update booking with ID: {} due to status: {}", id,
                    booking.getBookingStatus());
            throw new BookingStatusException("Cannot update booking with status: " + booking.getBookingStatus());
        }
        validateStartTime(request.getStartTime());
        // update productId
        //create list productIds from request
        Long productId = request.getProductId();
        if (productId != null) {
            List<Product> products = productService.findAllByIdIn(List.of(productId));
            if (products.isEmpty()) {
                throw new NotFoundBookingException("Product not found with ID: " + productId);
            }
            booking.setProducts(products);
            // set total price
            double totalPrice = products.stream()
                    .mapToDouble(product -> {
                        Double price = product.getPrice();
                        if (price == null) {
                            throw new IllegalArgumentException(
                                    "Product price cannot be null (Product ID: " + product.getId() + ")");
                        }
                        double discount_percent = product.getDiscount() != null ? product.getDiscount() : 0.0;
                        return price * (100 - discount_percent) / 100;
                    })
                    .sum();
            booking.setTotalPrice(totalPrice);
        }
        if (request.getAddress() != null) {
            booking.setAddress(request.getAddress().trim());
        }
        if (request.getNote() != null) {
            booking.setNote(request.getNote().trim());
        }
        if (request.getPhone() != null) {
            booking.setPhone(request.getPhone().trim());
        }
        if (request.getStartTime() != null) {
            booking.setStartTime(request.getStartTime());
        }
        if (request.getIsPeriodic() != null) {
            booking.setIsPeriodic(request.getIsPeriodic());
        }
        if( request.getName() != null) {
            User user = booking.getUser();
            if (user == null) {
                throw new NotFoundUserException("User not found for booking ID: " + id);
            }
            user.setName(request.getName().trim());
            userRepository.save(user);
        }
        bookingRepository.save(booking);
        logger.info("[Booking] Booking updated successfully with ID: {}", id);

        MiniBookingResponse changeStatus = this.updateBookingStatus(
                new UpdateBookingStatusRequest(id, request.getStatus()));
        messagingTemplate.convertAndSend("/topic/bookings/update", changeStatus);
        logger.info("[Booking] Booking update notification sent for booking ID: {}", id);
        return changeStatus;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getAllBookingsByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        logger.debug("[Booking] Fetching all bookings for user ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new NotFoundUserException("User not found with ID: " + userId);
        }

        List<Booking> bookings = bookingRepository.findByUserId(userId);
        if (bookings.isEmpty()) {
            logger.debug("[Booking] No bookings found for user ID: {}", userId);
            return Collections.emptyList();
        }

        return bookings.stream()
                .map(BookingMapper.INSTANCE::convertToBookingDTO)
                .toList();
    }

    @Override
    @Transactional
    public MiniBookingResponse updateBookingStatus(UpdateBookingStatusRequest request) {
        if (request == null || request.getBookingId() == null || request.getStatus() == null) {
            throw new IllegalArgumentException("Update request, booking ID, or status cannot be null");
        }

        Long id = request.getBookingId();
        String statusStr = request.getStatus();

        logger.info("[Booking] Updating booking status for ID: {} to {}", id, statusStr);

        String emailSale = JwtTokenManager.getCurrentUserLogin()
                .orElseThrow(() -> new NotFoundUserException("User not found"));
        User saleuser = userRepository.findByEmail(emailSale);
        if (saleuser == null) {
            throw new NotFoundUserException("User not found with email: " + emailSale);
        }

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
                    break;
                case REJECTED:
                    booking.setBookingStatus(BookingStatus.REJECTED);
                    this.emailSendingService.sendRejectToUser(
                            booking.getUser().getEmail(),
                            booking.getProducts().get(0).getName(),
                            booking.getCreateAt().toString(),
                            booking.getUser().getName());
                    booking.setSaleUser(saleuser);
                    break;
                case CANCELLED:
                    booking.setBookingStatus(BookingStatus.CANCELLED);
                    logger.info("[Booking] Booking with ID: {} has been cancelled", id);
                    break;
                case PENDING:
                    logger.info("[Booking] Booking with ID: {} is already in PENDING status", id);
                    break;
                default:
                    throw new BookingStatusException("Cannot update booking to status: " + newStatus);
            }
        } else {
            if (booking.getBookingStatus() == BookingStatus.ACCEPTED) {
                // validate saleid
                if (booking.getSaleUser() == null || !booking.getSaleUser().getId().equals(saleuser.getId())) {
                    throw new AlreadySaleHandle("This booking has already been handled by another sale user.");
                }
                switch (newStatus) {
                    case FAILED:
                        booking.setBookingStatus(BookingStatus.FAILED);
                        customer.setUserTotalFailedBookings(customer.getUserTotalFailedBookings() + 1);
                        calculateSuccessPercentage(saleuser);
                        break;

                    case SUCCESS:
                        booking.setBookingStatus(BookingStatus.SUCCESS);
                        saleuser.setUserTotalSuccessBookings(saleuser.getUserTotalSuccessBookings() + 1);
                        customer.setUserTotalSuccessBookings(customer.getUserTotalSuccessBookings() + 1);
                        calculateSuccessPercentage(saleuser);
                        break;
                    case ACCEPTED:
                        logger.info("[Booking] Booking with ID: {} is already in ACCEPTED status", id);
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
        MiniBookingResponse updated = BookingMapper.INSTANCE.convertToMiniBookingResponse(updatedBooking);
        messagingTemplate.convertAndSend("/topic/bookings/update", updated);

        logger.info("[Booking] Booking status updated successfully for ID: {}", id);
        return BookingMapper.INSTANCE.convertToMiniBookingResponse(updatedBooking);
    }

    @Override
    @Transactional
    public MiniBookingResponse addBooking(BookingRequest request) {

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
        validateTooMuchBookingPerHour(user.getId());
        validateAlreadyBookedAndPending(user.getId(), request.getProductIds().get(0), request);
        validateStartTime(request.getStartTime());

        Booking booking = BookingMapper.INSTANCE.convertToBooking(request);
        booking.setUser(user);
        booking.setCreateBy(user.getEmail());

        List<Long> productIds = request.getProductIds();
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product IDs cannot be null or empty");
        }

        List<Product> products = productService.findAllByIdIn(productIds);
        booking.setProducts(products);

        double totalPrice = products.stream()
                .mapToDouble(product -> {
                    Double price = product.getPrice();
                    if (price == null) {
                        throw new IllegalArgumentException(
                                "Product price cannot be null (Product ID: " + product.getId() + ")");
                    }

                    double discount_percent = product.getDiscount() != null ? product.getDiscount() : 0.0;
                    return price * (100 - discount_percent) / 100;
                })
                .sum();

        booking.setTotalPrice(totalPrice);
        booking.setBookingStatus(BookingStatus.PENDING);

        Booking bookingEntity = bookingRepository.save(booking);
        logger.info("[Booking] Booking created successfully with ID: {}", bookingEntity.getId());

        logger.info("[Booking] User with email: {} has been associated with booking ID: {}", user.getEmail(),
                bookingEntity.getId());
        MiniBookingResponse bookingDTO = BookingMapper.INSTANCE.convertToMiniBookingResponse(bookingEntity);
        messagingTemplate.convertAndSend("/topic/bookings/new", bookingDTO);

        logger.info("[Booking] New booking notification sent for booking ID: {}", bookingEntity.getId());
        return BookingMapper.INSTANCE.convertToMiniBookingResponse(bookingEntity);
    }

    private void calculateSuccessPercentage(User saleUser) {
        if (saleUser.getSaleTotalBookings() == null || saleUser.getSaleTotalBookings() == 0) {
            saleUser.setSaleSuccessPercent(0.0);
            return;
        }

        if (saleUser.getUserTotalSuccessBookings() == null) {
            saleUser.setUserTotalSuccessBookings(0L);
        }

        Double successPercentage = (double) saleUser.getUserTotalSuccessBookings() / saleUser.getSaleTotalBookings()
                * 100;
        saleUser.setSaleSuccessPercent(successPercentage);
    }

    public Long getTotalBooking(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        Instant startDateStr = startDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
        Instant endDateStr = endDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant(); // include
                                                                                                             // entire
                                                                                                             // day
        logger.debug("[Booking] Calculating total revenue from {} to {}", startDateStr, endDateStr);
        Long totalRevenue = bookingRepository.countTotalSuccessBooking(BookingStatus.SUCCESS, startDateStr, endDateStr);
        if (totalRevenue == null) {
            totalRevenue = 0L;
        }

        logger.debug("[Booking] Total revenue from {} to {}: {}", startDate, endDate, totalRevenue);
        return totalRevenue;
    }

    @Override
    public Long getTotalRevenue(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        Instant startDateStr = startDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
        Instant endDateStr = endDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant(); // include
                                                                                                             // entire
                                                                                                             // day
        logger.debug("[Booking] Calculating total revenue from {} to {}", startDateStr, endDateStr);
        Long totalRevenue = bookingRepository.countTotalRevenue(BookingStatus.SUCCESS, startDateStr, endDateStr);
        if (totalRevenue == null) {
            totalRevenue = 0L;
        }

        logger.debug("[Booking] Total revenue from {} to {}: {}", startDate, endDate, totalRevenue);
        return totalRevenue;
    }

    @Override
    public ResultPagingDTO getAllBooking(Specification<Booking> spec, Pageable pageable) {
        Page<Booking> bookings = bookingRepository.findAll(spec, pageable);
        ResultPagingDTO resultPagingDTO = new ResultPagingDTO();
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();

        meta.setPage(bookings.getNumber() + 1);
        meta.setSize(bookings.getSize());
        meta.setTotal(bookings.getTotalElements());
        meta.setTotalPages(bookings.getTotalPages());

        resultPagingDTO.setMeta(meta);

        List<MiniBookingResponse> bookingDTOs = bookings.getContent().stream()
                .map(BookingMapper.INSTANCE::convertToMiniBookingResponse)
                .collect(Collectors.toList());

        resultPagingDTO.setData(bookingDTOs);

        return resultPagingDTO;
    }

    private boolean validateTooMuchBookingPerHour(Long userId) {
        Instant oneHourAgo = Instant.now().minusSeconds(3600);
        long bookingCount = bookingRepository
                .countBookingsByUserIdAndCreatedAtAfter(userId, oneHourAgo);

        if (bookingCount >= 5) {
            logger.warn("[Booking] User with ID: {} has too many bookings in the last hour: {}", userId, bookingCount);
            throw new TooMuchBookingException("Bạn đã đặt quá 5 đơn hàng trong 1 giờ qua. Vui lòng thử lại sau.");
        }
        return true;
    }

    private void validateAlreadyBookedAndPending(Long userId, Long productId, BookingRequest request) {
        
        Optional<Booking> existingBooking = bookingRepository
                .findFirstByUserIdAndProductsIdAndBookingStatusOrderByCreateAtDesc(userId, productId, BookingStatus.PENDING);  
        
        if (existingBooking.isPresent()) {
            Booking booking = existingBooking.get();
            String bookingAddress = booking.getAddress();
            Instant bookingDate = booking.getStartTime();
            String requestAddress = request.getAddress();
            Instant requestDate = request.getStartTime();
            if (bookingAddress.equals(requestAddress) || bookingDate.equals(requestDate)) {
                logger.warn("[Booking] User with ID: {} already has a pending booking for product ID: {}", userId,
                        productId);
                throw new AlreadyPendingBooking("Bạn đã có một đơn hàng đang chờ xử lý cho sản phẩm này.");
            }
            
        }
    }
    private void validateStartTime(Instant startTime) {
        if (startTime == null) {
            throw new InvalidDateException("Start time cannot be null");
        }
        Instant now = Instant.now();
        if (startTime.isBefore(now)) {
            throw new InvalidDateException("Start time cannot be in the past");
        }
    }

    @Override
    public Long countTotalBookingByStatus(BookingStatus status, LocalDateRequest localDateRequest) {
        LocalDate startDate = localDateRequest.getStartDate();
        LocalDate endDate = localDateRequest.getEndDate();
        if (startDate == null || endDate == null) {
            throw new InvalidDateException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateException("Start date must be before or equal to end date");
        }
        Instant startDateStr = startDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
        Instant endDateStr = endDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
        logger.debug("[Booking] Counting total bookings with status {} from {} to {}", status, startDateStr,
                endDateStr);
        Long totalBookings = bookingRepository.countBookingsByStatusAndCreatedAtBetween(status, startDateStr,
                endDateStr);
        if (totalBookings == null) {
            totalBookings = 0L;
        }
        logger.debug("[Booking] Total bookings with status {} from {} to {}: {}", status, startDate, endDate,
                totalBookings);
        return totalBookings;
    }

    @Override
    public Long countTotalBooking(LocalDateRequest localDateRequest) {
        LocalDate startDate = localDateRequest.getStartDate();
        LocalDate endDate = localDateRequest.getEndDate();
        if (startDate == null || endDate == null) {
            throw new InvalidDateException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateException("Start date must be before or equal to end date");
        }
        Instant startDateStr = startDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
        Instant endDateStr = endDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
        logger.debug("[Booking] Counting total bookings from {} to {}", startDateStr, endDateStr);
        Long totalBookings = bookingRepository.countTotalBookingWithNotStatus(BookingStatus.CANCELLED, startDateStr,
                endDateStr);
        if (totalBookings == null) {
            totalBookings = 0L;
        }
        logger.debug("[Booking] Total bookings from {} to {}: {}", startDate, endDate, totalBookings);
        return totalBookings;
    }

    @Override
    public Long getTotalSuccessBooking(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidDateException("Start date and end date cannot be null");

        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateException("Start date must be before or equal to end date");
        }
        Instant startDateStr = startDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
        Instant endDateStr = endDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
        logger.debug("[Booking] Counting total bookings from {} to {}", startDateStr, endDateStr);
        Long totalBookings = bookingRepository.countBookingsByStatusAndCreatedAtBetween(BookingStatus.SUCCESS, startDateStr,
                endDateStr);
        if (totalBookings == null) {
            totalBookings = 0L;
        }
        logger.debug("[Booking] Total bookings from {} to {}: {}", startDate, endDate, totalBookings);
        return totalBookings;
    }

   @Override
public List<TopSaleResponse> getFiveTopSale(LocalDateRequest localDateRequest) {
    LocalDate startDate = localDateRequest.getStartDate();
    LocalDate endDate = localDateRequest.getEndDate();

    if (startDate == null || endDate == null) {
        throw new InvalidDateException("Start date and end date cannot be null");
    }
    if (startDate.isAfter(endDate)) {
        throw new InvalidDateException("Start date must be before or equal to end date");
    }

    Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    Instant endInstant = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

    logger.debug("[Booking] Fetching top revenue sales from {} to {}", startInstant, endInstant);

    List<Object[]> topSalesData = bookingRepository.findTopRevenueSales(startInstant, endInstant);
    if (topSalesData.isEmpty()) {
        logger.debug("[Booking] No top sales found for the given date range");
        return Collections.emptyList();
    }

    List<TopSaleResponse> topSales = new ArrayList<>();

    for (Object[] data : topSalesData) {
        String email = (String) data[0];
        Number revenueNumber = (Number) data[1];
        Double totalRevenue = revenueNumber != null ? revenueNumber.doubleValue() : 0.0;

        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("[Booking] User not found for email: {}", email);
            continue; 
        }

        TopSaleResponse response = new TopSaleResponse();
        response.setId(user.getId());
        response.setName(user.getName() != null ? user.getName() : "Unknown User");
        response.setEmail(email);
        response.setAvatar(user.getAvatar()); 
        response.setTotalSalePrice(totalRevenue);
        response.setTotalSuccessBookingPercent(user.getSaleSuccessPercent() != null ? user.getSaleSuccessPercent().floatValue() : 0.0f);
        logger.info("[Booking] Top sale: {} - ID: {}, Total: {}, Success: {}%", 
                    response.getName(), response.getId(), totalRevenue, response.getTotalSuccessBookingPercent());

        topSales.add(response);
    }

    return topSales.size() > 5 ? topSales.subList(0, 5) : topSales;
}
}
