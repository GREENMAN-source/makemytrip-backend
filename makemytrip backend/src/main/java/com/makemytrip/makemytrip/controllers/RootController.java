package com.makemytrip.makemytrip.controllers;

import com.makemytrip.makemytrip.models.Flight;
import com.makemytrip.makemytrip.models.Hotel;
import com.makemytrip.makemytrip.repositories.FlightRepository;
import com.makemytrip.makemytrip.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*") // Allows your React frontend to communicate
public class RootController {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping("/")
    public String home() {
        return "✅ AI Engine & API is running on port 8080!";
    }

    // --- HOTEL ENDPOINTS ---

    // 1. Get all hotels (Default fallback)
    @GetMapping("/hotel")
    public ResponseEntity<List<Hotel>> getallhotel() {
        List<Hotel> hotels = hotelRepository.findAll();
        return ResponseEntity.ok(hotels);
    }

    // 2. Get a single hotel by ID
    @GetMapping("/hotel/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable String id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if (hotel.isPresent()) {
            return ResponseEntity.ok(hotel.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. THE FIX: Dynamic Search Engine for Location & Category
    @GetMapping("/hotel/search")
    public ResponseEntity<List<Hotel>> searchHotels(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category) {
        
        List<Hotel> results;

        // Both Location and Category are provided
        if (location != null && !location.isEmpty() && category != null && !category.isEmpty()) {
            results = hotelRepository.findByLocationIgnoreCaseAndCategoryIgnoreCase(location, category);
        } 
        // Only Location is provided
        else if (location != null && !location.isEmpty()) {
            results = hotelRepository.findByLocationIgnoreCase(location);
        } 
        // Only Category is provided
        else if (category != null && !category.isEmpty()) {
            results = hotelRepository.findByCategoryIgnoreCase(category);
        } 
        // If nothing is provided, return all
        else {
            results = hotelRepository.findAll();
        }

        return ResponseEntity.ok(results);
    }

    // --- FLIGHT ENDPOINTS ---

    @GetMapping("/flight")
    public ResponseEntity<List<Flight>> getallflights() {
        List<Flight> flights = flightRepository.findAll();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/flight/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable String id) {
        Optional<Flight> flight = flightRepository.findById(id);
        if (flight.isPresent()) {
            return ResponseEntity.ok(flight.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
