package com.makemytrip.makemytrip.controllers;

import com.makemytrip.makemytrip.models.Review;
import com.makemytrip.makemytrip.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    // Fetch reviews for a specific hotel/flight
    @GetMapping("/{targetId}")
    public List<Review> getReviews(@PathVariable String targetId) {
        return reviewRepository.findByTargetId(targetId);
    }

    // Submit a new review
    @PostMapping
    public Review createReview(@RequestBody Review review) {
        review.setCreatedAt(System.currentTimeMillis());
        // Ensure rating is between 1 and 5
        if(review.getRating() < 1) review.setRating(1);
        if(review.getRating() > 5) review.setRating(5);
        return reviewRepository.save(review);
    }
}