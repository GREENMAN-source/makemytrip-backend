package com.makemytrip.makemytrip.controllers;

import com.makemytrip.makemytrip.models.Review;
import com.makemytrip.makemytrip.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    // Fetch reviews for a specific hotel/flight
    @GetMapping("/{targetId}")
    public List<Review> getReviews(
            @PathVariable String targetId,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(required = false) Integer rating) {
        List<Review> reviews = new ArrayList<>(reviewRepository.findByTargetId(targetId));

        if (rating != null) {
            reviews.removeIf(review -> review.getRating() != rating);
        }

        if ("highest".equalsIgnoreCase(sort)) {
            reviews.sort(Comparator.comparingInt(Review::getRating).reversed());
        } else if ("helpful".equalsIgnoreCase(sort)) {
            reviews.sort(Comparator.comparingInt(Review::getHelpfulVotes).reversed());
        } else {
            reviews.sort(Comparator.comparingLong(Review::getCreatedAt).reversed());
        }

        return reviews;
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

    @PostMapping("/{reviewId}/reply")
    public Review replyToReview(@PathVariable String reviewId, @RequestBody String reply) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            return null;
        }
        List<String> replies = review.getReplies();
        if (replies == null) {
            replies = new ArrayList<>();
        }
        replies.add(reply.replace("\"", ""));
        review.setReplies(replies);
        return reviewRepository.save(review);
    }

    @PostMapping("/{reviewId}/flag")
    public Review flagReview(@PathVariable String reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            return null;
        }
        review.setFlagged(true);
        return reviewRepository.save(review);
    }

    @PostMapping("/{reviewId}/helpful")
    public Review markHelpful(@PathVariable String reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            return null;
        }
        review.setHelpfulVotes(review.getHelpfulVotes() + 1);
        return reviewRepository.save(review);
    }
}
