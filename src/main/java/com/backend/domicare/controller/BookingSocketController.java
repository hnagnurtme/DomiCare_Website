package com.backend.domicare.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.backend.domicare.dto.BookingDTO;

@Controller
public class BookingSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public BookingSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNewBooking(BookingDTO bookingDTO) {
        messagingTemplate.convertAndSend("/topic/bookings/new", bookingDTO);
    }
}
