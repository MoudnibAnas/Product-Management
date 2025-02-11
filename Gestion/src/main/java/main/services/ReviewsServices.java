package main.services;

import main.models.Product;
import main.models.Review;
import main.services.interfaces.ReviewService;
import main.utils.parser.XMLNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewsServices implements ReviewService {
    @Value("classpath:xml/reviews.xml")
    private Resource xmlFileResource;

    @Override
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            XMLNode[] reviewNodes = rootNode.children("review");
            for (XMLNode reviewNode : reviewNodes) {
                Review review = new Review();
                review.setProductId(reviewNode.child("productId").getValue());
                review.setCustomerId(reviewNode.child("customerId").getValue());
                review.setRating(Integer.parseInt(reviewNode.child("rating").getValue()));
                review.setReviewText(reviewNode.child("reviewText").getValue());
                reviews.add(review);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public List<Review> getReviewsByProductId(String productId) {
        List<Review> reviews = new ArrayList<>();
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            XMLNode[] reviewNodes = rootNode.children("review");
            for (XMLNode reviewNode : reviewNodes) {
                String reviewedProductId = reviewNode.child("productId").getValue();
                if(productId.equals(reviewedProductId)) {
                    Review review = new Review();
                    review.setProductId(reviewedProductId);
                    review.setCustomerId(reviewNode.child("customerId").getValue());
                    review.setRating(Integer.parseInt(reviewNode.child("rating").getValue()));
                    review.setReviewText(reviewNode.child("reviewText").getValue());
                    reviews.add(review);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public Review addReview(Review review) {
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            if (rootNode.getDocument() == null)  rootNode.setDocument();
            Document doc = rootNode.getDocument();
            Element newElement = doc.createElement("review");
            XMLNode newReviewNode = new XMLNode(newElement, doc);
            newReviewNode.addChild("productId", review.getProductId());
            newReviewNode.addChild("customerId", review.getCustomerId());
            newReviewNode.addChild("rating", String.valueOf(review.getRating()));
            newReviewNode.addChild("reviewText", review.getReviewText());
            rootNode.addChild(newReviewNode);
            rootNode.saveToFile(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return review;
    }
    @Override
    public Review updateReview(Review review) {
        return null;
    }

    @Override
    public void deleteReview (String reviewText) {
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            XMLNode[] reviewNodes = rootNode.children("review");
            System.out.println("the node : " + reviewNodes.length);
            for (XMLNode reviewNode : reviewNodes) {
                if (reviewText.equals(reviewNode.child("reviewText").getValue())) {
                    rootNode.removeChild(reviewNode);
                    rootNode.saveToFile(file.getAbsolutePath());
                    XMLNode testRoot = new XMLNode(file.getAbsolutePath());
                    XMLNode[] updatedNodes = testRoot.children("review");
                    System.out.println("Reviews remaining after deletion: " + updatedNodes.length);
                    return;
                }
            }
            System.out.println("Review not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Review getReviewById(int id) {
        return null;
    }

    @Override
    public List<Review> getReviewsByUser(int userId) {
        return List.of();
    }

    @Override
    public List<Review> getReviews() {
        return List.of();
    }
}
