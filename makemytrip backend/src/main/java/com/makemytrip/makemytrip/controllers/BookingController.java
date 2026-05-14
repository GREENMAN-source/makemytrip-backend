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
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        
        // --- AI NORMALIZATION ENGINE ---
        // This ensures the frontend AI always matches Kerala to Kochi assets
        if (bookings != null) {
            for (Booking booking : bookings) {
                if (booking.getTargetName() != null) {
                    String nameLower = booking.getTargetName().toLowerCase();
                    
                    // If user booked "Kerala", we append "kochi" so the frontend AI recognizes it
                    if (nameLower.contains("kerala") && !nameLower.contains("kochi")) {
                        booking.setTargetName(booking.getTargetName() + " kochi");
                    }
                }
            }
        }
        return bookings;
    }

    // Save a new trip (from the Interactive Selection modal)
    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        booking.setCreatedAt(String.valueOf(System.currentTimeMillis()));
        booking.setRefundStatus("ACTIVE"); 
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
