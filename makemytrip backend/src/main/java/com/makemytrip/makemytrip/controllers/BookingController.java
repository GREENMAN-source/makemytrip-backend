package com.makemytrip.makemytrip.controllers;

import com.makemytrip.makemytrip.models.Booking;
import com.makemytrip.makemytrip.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    // Get trips for the dashboard
    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable String userId) {
        return bookingRepository.findByUserId(userId);
    }

    // Save a new trip (from the Interactive Selection modal)
    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        booking.setCreatedAt(String.valueOf(System.currentTimeMillis()));
        booking.setRefundStatus("ACTIVE"); // Default status for new trips
        return bookingRepository.save(booking);
    }

    // Task 1: Handle Cancellation & 50% Refund
    @PostMapping("/cancel/{id}")
    public Booking cancelBooking(@PathVariable String id, @RequestParam String reason) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            booking.setCancellationReason(reason);
            booking.setRefundStatus("PENDING_50%_REFUND");
            return bookingRepository.save(booking);
        }
        return null;
    }
}