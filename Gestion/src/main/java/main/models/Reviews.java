package main.models;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "reviews")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "review"
})
public class Reviews {

    @XmlElement(name = "review")
    private List<Review> review;
    public Reviews() {
        review = new ArrayList<Review>();
    }
    public List<Review> getReviews() {
        return review;
    }
    public void setProducts(List<Review> reviews) {
        this.review = reviews;
    }
    public void addProduct(Review review) {
        this.review.add(review);
    }
    public void removeProduct(Review review) {
        this.review.remove(review);
    }

}
