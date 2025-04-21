package com.backend.domicare.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.backend.domicare.dto.BookingDTO;
import com.backend.domicare.dto.request.BookingRequest;
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
                .startTime(booking.getStartTime())
                .isPeriodic(booking.getIsPeriodic())
                .startTime(booking.getStartTime())
                .bookingStatus(booking.getBookingStatus())
                .userDTO(UserMapper.INSTANCE.convertToUserDTO(booking.getUser()))
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
}
