package com.backend.domicare.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.backend.domicare.dto.BookingDTO;
import com.backend.domicare.dto.request.BookingRequest;
import com.backend.domicare.dto.response.MiniBookingResponse;
import com.backend.domicare.model.Booking;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface  BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    Booking convertToBooking(BookingDTO bookingDTO);

    default BookingDTO convertToBookingDTO(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingDTO.builder()
                .id(booking.getId())
                .createAt(booking.getCreateAt())
                .createBy(booking.getCreateBy())
                .updateAt(booking.getUpdateAt())
                .updateBy(booking.getUpdateBy())
                .address(booking.getAddress())
                .note(booking.getNote())
                .totalPrice(booking.getTotalPrice())
                .isPeriodic(booking.getIsPeriodic())
                .startTime(booking.getStartTime())
                .bookingStatus(booking.getBookingStatus())
                .userDTO(booking.getUser() != null ? UserMapper.INSTANCE.convertToUserDTO(booking.getUser()) : null)
                .products(booking.getProducts() != null ? ProductMapper.INSTANCE.convertToProductDTOs(booking.getProducts()) : null)
                .build();
    }

    Booking convertToBooking(BookingRequest bookingRequest);
    default Booking convertToBookingByRequest(BookingRequest bookingRequest) {
        if (bookingRequest == null) {
            return null;
        }
        return Booking.builder()
                .address(bookingRequest.getAddress())
                .note(bookingRequest.getNote())
                .isPeriodic(bookingRequest.getIsPeriodic())
                .startTime(bookingRequest.getStartTime())
                .note(bookingRequest.getNote())
                .build();
    }

    default MiniBookingResponse convertToMiniBookingResponse(Booking booking) {
        if (booking == null) {
            return null;
        }
        return MiniBookingResponse.builder()
                .id(booking.getId())
                .address(booking.getAddress())
                .totalPrice(booking.getTotalPrice())
                .note(booking.getNote())
                .startTime(booking.getStartTime())
                .products(booking.getProducts() != null ? ProductMapper.INSTANCE.convertToProductMinis(booking.getProducts()) : null)
                .userDTO(booking.getUser() != null ? UserMapper.INSTANCE.convertToUserMini(booking.getUser()) : null)
                .isPeriodic(booking.getIsPeriodic())
                .bookingStatus(booking.getBookingStatus())
                .createBy(booking.getCreateBy())
                .updateBy(booking.getUpdateBy())
                .createAt(booking.getCreateAt())
                .updateAt(booking.getUpdateAt())
                .build();
    }


}
