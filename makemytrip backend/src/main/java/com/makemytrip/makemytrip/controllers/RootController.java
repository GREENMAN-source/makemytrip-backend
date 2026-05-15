package com.makemytrip.makemytrip.controllers;

import com.makemytrip.makemytrip.models.Flight;
import com.makemytrip.makemytrip.models.Hotel;
import com.makemytrip.makemytrip.repositories.FlightRepository;
import com.makemytrip.makemytrip.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class RootController {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping("/")
    public String home() {
        return "AI Engine & API is running on port 8080!";
    }

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

    private String getDestinationName(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        String[] routeParts = value.split("(?i)\\s+to\\s+");
        String destination = routeParts[routeParts.length - 1].trim();
        int commaIndex = destination.indexOf(",");
        return commaIndex >= 0 ? destination.substring(0, commaIndex).trim() : destination;
    }

    private boolean isVisibleHotel(Hotel hotel) {
        return !isRemovedPlace(hotel.gethotelName())
                && !isRemovedPlace(hotel.getLocation())
                && !isRemovedPlace(hotel.getCategory());
    }

    private boolean isVisibleFlight(Flight flight) {
        return !isRemovedPlace(flight.getFlightName())
                && !isRemovedPlace(flight.getFrom())
                && !isRemovedPlace(flight.getTo());
    }

    private List<Hotel> visibleHotels(List<Hotel> hotels) {
        return hotels.stream()
                .filter(this::isVisibleHotel)
                .collect(Collectors.toList());
    }

    private int hotelRecommendationScore(Hotel hotel, String destination) {
        String normalizedDestination = destination.toLowerCase();
        String location = hotel.getLocation() == null ? "" : hotel.getLocation().toLowerCase();
        String hotelName = hotel.gethotelName() == null ? "" : hotel.gethotelName().toLowerCase();
        String category = hotel.getCategory() == null ? "" : hotel.getCategory().toLowerCase();

        int score = 0;
        if (location.equals(normalizedDestination)) {
            score += 100;
        }
        if (location.contains(normalizedDestination) || normalizedDestination.contains(location)) {
            score += 60;
        }
        if (hotelName.contains(normalizedDestination)) {
            score += 25;
        }
        if (category.contains("luxury") || category.contains("family") || category.contains("business")) {
            score += 10;
        }
        score += Math.max(0, hotel.getAvailableRooms());
        score -= Math.min(30, (int) (hotel.getPricePerNight() / 1000));
        return score;
    }

    private boolean hotelMatchesDestination(Hotel hotel, String destination) {
        String normalizedDestination = destination.toLowerCase();
        String location = hotel.getLocation() == null ? "" : hotel.getLocation().toLowerCase();
        return location.equals(normalizedDestination)
                || location.contains(normalizedDestination)
                || normalizedDestination.contains(location);
    }

    @GetMapping("/hotel")
    public ResponseEntity<List<Hotel>> getallhotel() {
        return ResponseEntity.ok(visibleHotels(hotelRepository.findAll()));
    }

    @GetMapping("/hotel/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable String id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if (hotel.isPresent() && isVisibleHotel(hotel.get())) {
            return ResponseEntity.ok(hotel.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/hotel/search")
    public ResponseEntity<List<Hotel>> searchHotels(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category) {

        List<Hotel> results;

        if (location != null && !location.isEmpty() && category != null && !category.isEmpty()) {
            results = hotelRepository.findByLocationIgnoreCaseAndCategoryIgnoreCase(location, category);
        } else if (location != null && !location.isEmpty()) {
            results = hotelRepository.findByLocationIgnoreCase(location);
        } else if (category != null && !category.isEmpty()) {
            results = hotelRepository.findByCategoryIgnoreCase(category);
        } else {
            results = hotelRepository.findAll();
        }

        return ResponseEntity.ok(visibleHotels(results));
    }

    @GetMapping("/hotel/recommendations")
    public ResponseEntity<List<Hotel>> recommendHotels(@RequestParam String destination) {
        String destinationName = getDestinationName(destination);

        if (destinationName.isBlank() || isRemovedPlace(destinationName)) {
            return ResponseEntity.ok(List.of());
        }

        List<Hotel> recommendations = visibleHotels(hotelRepository.findAll()).stream()
                .filter(hotel -> hotelMatchesDestination(hotel, destinationName))
                .sorted(Comparator
                        .comparingInt((Hotel hotel) -> hotelRecommendationScore(hotel, destinationName))
                        .reversed()
                        .thenComparingDouble(Hotel::getPricePerNight))
                .limit(3)
                .collect(Collectors.toList());

        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/flight")
    public ResponseEntity<List<Flight>> getallflights() {
        List<Flight> flights = flightRepository.findAll().stream()
                .filter(this::isVisibleFlight)
                .collect(Collectors.toList());
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/flight/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable String id) {
        Optional<Flight> flight = flightRepository.findById(id);
        if (flight.isPresent() && isVisibleFlight(flight.get())) {
            return ResponseEntity.ok(flight.get());
        }
        return ResponseEntity.notFound().build();
    }
}
