package main.services.interfaces;

import main.models.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getReviews();
    Review addReview(Review review);
    Review updateReview(Review review);
    void deleteReview(String reviewText);
    Review getReviewById(int id);
    List<Review> getReviewsByUser(int userId);
    List<Review> getAllReviews();
    List<Review> getReviewsByProductId(String productId);
}
