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
@CrossOrigin(origins = "*") // This is perfect, it allows Next.js to talk to it!
public class RootController {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping("/")
    public String home() {
        return "✅ It's running on port 8080!";
    }

    // --- HOTEL ENDPOINTS ---
    
    @GetMapping("/hotel")
    public ResponseEntity<List<Hotel>> getallhotel(){
        List<Hotel> hotels=hotelRepository.findAll();
        return ResponseEntity.ok(hotels);
    }

    // NEW: Get a single hotel by its ID (Fixes the 404 Error!)
    @GetMapping("/hotel/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable String id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if (hotel.isPresent()) {
            return ResponseEntity.ok(hotel.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- FLIGHT ENDPOINTS ---

    @GetMapping("/flight")
    public ResponseEntity<List<Flight>> getallflights(){
        List<Flight> flights=flightRepository.findAll();
        return ResponseEntity.ok(flights);
    }

    // NEW: Get a single flight by its ID (For your next task!)
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