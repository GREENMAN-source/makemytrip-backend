package com.makemytrip.makemytrip.repositories;

import com.makemytrip.makemytrip.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    // Find all reviews for a specific hotel or flight
    List<Review> findByTargetId(String targetId);
}