package com.backend.domicare.service.imp;

import org.springframework.stereotype.Service;

import com.backend.domicare.dto.response.OverviewResponse;
import com.backend.domicare.model.BookingStatus;
import com.backend.domicare.repository.BookingsRepository;
import com.backend.domicare.repository.CategoriesRepository;
import com.backend.domicare.repository.ProductsRepository;
import com.backend.domicare.repository.ReviewsRepository;
import com.backend.domicare.repository.UsersRepository;
import com.backend.domicare.service.OverviewService;
import com.backend.domicare.utils.ProjectConstants;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class OverviewServiceImp implements OverviewService {
    private final UsersRepository usersRepository;
    private final ProductsRepository productsRepository;
    private final CategoriesRepository categoriesRepository;
    private final BookingsRepository bookingsRepository;
    private final ReviewsRepository reviewsRepository;
    @Override
    public Long getTotalCustomers() {
        return usersRepository.countUsersByRoleName(ProjectConstants.ROLE_USER);
    }

    @Override
    public Long getTotalSales() {
        return usersRepository.countUsersByRoleName(ProjectConstants.ROLE_SALE);
    }
    
    @Override
    public Long getTotalOrders() {
        return bookingsRepository.count();
    }

    @Override
    public Long getTotalProducts() {
        
        return productsRepository.count();
    }

    @Override
    public Long getTotalCategory() {
        return categoriesRepository.count();
    }


    public Long getTotalPendingBookings(){
        return bookingsRepository.countBookingsByStatus(BookingStatus.PENDING);
    }

    public Long getTotalReviews(){
        return reviewsRepository.count();
    }

    @Override
    public Long getTotalAcceptedBookings() {
        return bookingsRepository.countBookingsByStatus(BookingStatus.ACCEPTED);
    }


    @Override
    public OverviewResponse getOverviews(){
        OverviewResponse overviewResponse = new OverviewResponse();
        overviewResponse.setTotalCustomers(getTotalCustomers());
        overviewResponse.setTotalSales(getTotalSales());
        overviewResponse.setTotalOrders(getTotalOrders());
        overviewResponse.setTotalProducts(getTotalProducts());
        overviewResponse.setTotalCategory(getTotalCategory());
        overviewResponse.setTotalAcceptedBookings(getTotalAcceptedBookings());
        overviewResponse.setTotalPendingBookings(getTotalPendingBookings());
        overviewResponse.setTotalReviews(getTotalReviews());
        return overviewResponse;
    }
    
}
