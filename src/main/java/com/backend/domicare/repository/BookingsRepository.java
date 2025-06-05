package com.backend.domicare.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Booking;
import com.backend.domicare.model.BookingStatus;
import com.backend.domicare.model.Product;

@Repository
public interface BookingsRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    List<Booking> findByUserId(@Param("userId") Long userId);

    @Query("DELETE FROM Booking b WHERE b.id IN :ids")
    void softDeleteByIds(@Param("ids") List<Long> ids);

    @Query("select b from Booking b join fetch b.user join fetch b.products where b.id = :id")
    Optional<Booking> findByIdWithUserAndProducts(@Param("id") Long id);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingStatus = :status")
    Long countBookingsByStatus(@Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.updateBy = :updateBy AND b.bookingStatus = :status")
    List<Booking> findByUpdateByAndStatus(@Param("updateBy") String updateBy, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.createBy = :createBy AND b.bookingStatus = :status")
    List<Booking> findByCreateByAndStatus(@Param("createBy") String createBy, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.updateBy = :updateBy AND b.bookingStatus IN :status")
    List<Booking> findByUpdateByAndStatusIn(@Param("updateBy") String updateBy,
            @Param("status") List<BookingStatus> status);

    // count total success booking in startdate and enddate
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingStatus = :status AND b.createAt BETWEEN :startDate AND :endDate")
    Long countTotalSuccessBooking(@Param("status") BookingStatus status, @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);
    // Example : Select b from Booking b where b.createAt between '2023-01-01' and
    // '2023-12-31'

    // count total revenue in startdate and enddate //
    @Query("SELECT SUM(b.totalPrice) FROM Booking b WHERE b.bookingStatus = :status AND b.createAt BETWEEN :startDate AND :endDate")
    Long countTotalRevenue(@Param("status") BookingStatus status, @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);
    // Example : Select SUM(b.totalPrice) from Booking b where b.createAt between
    // '2023-01-01' and '2023-12-31'

    // countBookingsByUserIdAndCreatedAtAfter
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.createAt >= :createdAt")
    Long countBookingsByUserIdAndCreatedAtAfter(@Param("userId") Long userId, @Param("createdAt") Instant createdAt);

    @Query("SELECT COUNT(b) > 0 FROM Booking b JOIN b.products p " +
            "WHERE b.user.id = :userId AND p.id = :productId AND b.bookingStatus = :status")
    boolean existsByUserIdAndProductIdAndStatus(@Param("userId") Long userId,
            @Param("productId") Long productId,
            @Param("status") BookingStatus status);

    boolean existsByUserIdAndProductsAndBookingStatusAndCreateAtAfter(
            Long userId,
            Product products,
            BookingStatus bookingStatus,
            Instant createAt);
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingStatus = :status AND b.createAt BETWEEN :startDate AND :endDate")
    Long countBookingsByStatusAndCreatedAtBetween(@Param("status") BookingStatus status,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingStatus <> :status AND b.createAt BETWEEN :startDate AND :endDate")
    Long countTotalBookingWithNotStatus(@Param("status") BookingStatus status,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);
    //findFirstByUserIdAndProductIdAndStatus(userId, productId, BookingStatus.PENDING)
    Optional<Booking> findFirstByUserIdAndProductsIdAndBookingStatusOrderByCreateAtDesc(Long userId, Long productId, BookingStatus status);

    //List<TopSaleResponse> topSales = bookingRepository.findTopSales(startDateStr, endDateStr);
    @Query("SELECT b.updateBy AS email, SUM(b.totalPrice) AS totalRevenue " +
       "FROM Booking b " +
       "WHERE b.createAt BETWEEN :startDate AND :endDate AND b.bookingStatus = 'SUCCESS' " +
       "GROUP BY b.updateBy " +
       "ORDER BY totalRevenue DESC")
    List<Object[]> findTopRevenueSales(@Param("startDate") Instant startDate,
                                   @Param("endDate") Instant endDate);
}   
