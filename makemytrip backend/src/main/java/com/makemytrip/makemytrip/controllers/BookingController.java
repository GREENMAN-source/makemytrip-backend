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

    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable String userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        
        if (bookings != null) {
            for (Booking booking : bookings) {
                // Use the alias method getTargetName() which points to serviceId
                String fullName = booking.getTargetName(); 
                
                if (fullName != null && fullName.contains(" to ")) {
                    // UNIVERSAL STRIPPER: 
                    // Splits "AnyCity to DestinationCity" and takes only "DestinationCity"
                    String[] parts = fullName.split("(?i) to "); // (?i) makes it case-insensitive
                    String destination = parts[parts.length - 1].trim();
                    
                    // Update the object with ONLY the destination
                    booking.setTargetName(destination);
                }

                // AI ASSET MAPPING:
                // Ensure unknown cities map to your frontend images/assets
                String destinationLower = booking.getTargetName().toLowerCase();
                if (destinationLower.contains("colcatta") || destinationLower.contains("kolkata")) {
                    booking.setTargetName("Kolkata delhi"); // Maps to Delhi assets
                } else if (destinationLower.contains("kerala") && !destinationLower.contains("kochi")) {
                    booking.setTargetName("Kerala kochi"); // Maps to Kochi assets
                }
            }
        }
        return bookings;
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        booking.setCreatedAt(String.valueOf(System.currentTimeMillis()));
        booking.setRefundStatus("ACTIVE"); 
        return bookingRepository.save(booking);
    }

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
