package com.makemytrip.makemytrip.services;

import java.time.Instant;
import java.util.List;
import com.makemytrip.makemytrip.models.Booking;
import com.makemytrip.makemytrip.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public Booking saveBooking(Booking booking) {
        if (booking.getCreatedAt() == null || booking.getCreatedAt().isEmpty()) {
            booking.setCreatedAt(Instant.now().toString());
        }
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUserId(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public void deleteBooking(String id) {
        bookingRepository.deleteById(id);
    }
}