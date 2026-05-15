package com.makemytrip.makemytrip.repositories;

import com.makemytrip.makemytrip.models.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HotelRepository extends MongoRepository<Hotel, String> {
    
    // Finds hotels by exact location (ignores uppercase/lowercase so "goa" == "Goa")
    List<Hotel> findByLocationIgnoreCase(String location);
    
    // Finds hotels by category (e.g., "Beach", "Luxury")
    List<Hotel> findByCategoryIgnoreCase(String category);
    
    // Finds hotels by BOTH location and category (e.g., "Goa" AND "Beach")
    List<Hotel> findByLocationIgnoreCaseAndCategoryIgnoreCase(String location, String category);
}
