package com.makemytrip.makemytrip.controllers;

import com.makemytrip.makemytrip.models.Booking;
import com.makemytrip.makemytrip.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    private boolean isRemovedPlace(String value) {
        if (value == null) {
            return false;
        }

        String normalized = value.toLowerCase();
        return normalized.contains("kol" + "kata")
                || normalized.contains("cal" + "cutta")
                || normalized.contains("col" + "catta")
                || normalized.contains("cc" + "u");
    }

    private String displayTargetName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return "Unknown Destination";
        }

        String[] parts = fullName.split("(?i)\\s+to\\s+");
        String destination = parts[parts.length - 1].trim();
        return isRemovedPlace(destination) ? "Destination unavailable" : destination;
    }

    private boolean isVisibleBooking(Booking booking) {
        return !isRemovedPlace(booking.getTargetName())
                && !isRemovedPlace(booking.getServiceId())
                && !isRemovedPlace(booking.getSelectionId());
    }

    private long parseBookingTime(String createdAt) {
        if (createdAt == null || createdAt.isBlank()) {
            return System.currentTimeMillis();
        }

        try {
            return Long.parseLong(createdAt);
        } catch (NumberFormatException ignored) {
            try {
                return Instant.parse(createdAt).toEpochMilli();
            } catch (DateTimeParseException ignoredAgain) {
                return System.currentTimeMillis();
            }
        }
    }

    private void applyRefundPolicy(Booking booking, String reason) {
        long now = System.currentTimeMillis();
        long createdAt = parseBookingTime(booking.getCreatedAt());
        long hoursSinceBooking = Math.max(0, (now - createdAt) / (1000 * 60 * 60));
        double totalAmount = booking.getTotalAmount() == null ? 0 : booking.getTotalAmount();
        double refundAmount = hoursSinceBooking <= 24 ? totalAmount * 0.5 : 0;

        booking.setCancellationReason(reason);
        booking.setCanceledAt(String.valueOf(now));
        booking.setRefundAmount(refundAmount);
        booking.setRefundStatus(refundAmount > 0 ? "PENDING" : "NOT_APPLICABLE");
        booking.setExpectedRefundBy(refundAmount > 0 ? String.valueOf(now + (3L * 24 * 60 * 60 * 1000)) : null);
        booking.setRefundPolicy(hoursSinceBooking <= 24
                ? "50% refund because cancellation was requested within 24 hours."
                : "No refund because the 24-hour partial refund window has expired.");
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll().stream()
                .filter(this::isVisibleBooking)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable String userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId).stream()
                .filter(this::isVisibleBooking)
                .collect(Collectors.toList());

        for (Booking booking : bookings) {
            booking.setTargetName(displayTargetName(booking.getTargetName()));

            String destinationLower = booking.getTargetName().toLowerCase();
            if (destinationLower.contains("kerala") && !destinationLower.contains("kochi")) {
                booking.setTargetName("Kerala kochi");
            }
        }

        return bookings;
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        booking.setTargetName(displayTargetName(booking.getTargetName()));
        booking.setCreatedAt(String.valueOf(System.currentTimeMillis()));
        booking.setRefundStatus("ACTIVE");
        booking.setRefundAmount(0.0);
        return bookingRepository.save(booking);
    }

    @PostMapping("/cancel/{id}")
    public Booking cancelBooking(@PathVariable String id, @RequestParam String reason) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            applyRefundPolicy(booking, reason);
            return bookingRepository.save(booking);
        }
        return null;
    }
}
