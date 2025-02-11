package main.router;

import main.models.Category;
import main.models.Product;
import main.models.Review;
import main.services.ProductsServices;
import main.services.interfaces.CategoryService;
import main.services.interfaces.ProductService;
import main.services.interfaces.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class Router {
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductsServices productServices;
    @GetMapping(value = "/")
    public String index () {
        return "index";
    }

    @GetMapping("/products")
    public String getProductList(Model model) {
        List<Product> products = productService.getAllProducts();
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "Products/products";
    }
    @PostMapping("/products/add")
    public String addProduct(@RequestParam String productId,
                             @RequestParam String name,
                             @RequestParam String description,
                             @RequestParam double price,
                             @RequestParam String category,
                             @RequestParam String imageUrl,
                             Model model) {
        Product newProduct = new Product();
        newProduct.setProductId(productId);
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setPrice(price);
        newProduct.setCategory(category);
        newProduct.setImageUrl(imageUrl);
        System.out.println("Adding new product");
        productServices.addProduct(newProduct);
        model.addAttribute("products", productServices.getAllProducts());
        return "redirect:/products"; // Redirect to the product catalog page to see the updated list
    }
    @PostMapping("/products/update")
    public String updateProduct(@RequestParam String productId,
                                @RequestParam String name,
                                @RequestParam String description,
                                @RequestParam Double price,
                                @RequestParam String category,
                                @RequestParam String imageUrl,
                                Model model) {
        Product existingProduct = productService.getProductById(productId);
        if (existingProduct != null) {
            existingProduct.setName(name);
            existingProduct.setDescription(description);
            existingProduct.setPrice(price);
            existingProduct.setCategory(category);
            existingProduct.setImageUrl(imageUrl);
            productService.updateProduct(existingProduct);
            model.addAttribute("products", productService.getAllProducts());
            return "redirect:/products";
        } else {
            return "error";
        }
    }

    @DeleteMapping("/api/products/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }


    @GetMapping(value = "/reviews")
    public String getReviewList (Model model) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        return "Reviews/reviews";
    }
    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String categoryId,
                              @RequestParam String name,
                              @RequestParam String description,
                              Model model) {
        System.out.println("Adding new cat ");
        Category newCategory = new Category();
        newCategory.setCategoryId(categoryId);
        newCategory.setName(name);
        newCategory.setDescription(description);
        categoryService.addCategory(newCategory);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/categories";
    }
    @PostMapping("/categories/update")
    public String updateCategory(@RequestParam String categoryId,
                                 @RequestParam String name,
                                 @RequestParam String description,
                                 Model model) {
        System.out.println("Updated id "  + categoryId);
        Category existingCategory = categoryService.getCategoryById(categoryId);
        if (existingCategory != null) {
            existingCategory.setName(name);
            existingCategory.setDescription(description);
            categoryService.updateCategory(existingCategory);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "redirect:/categories";
        } else {
            return "error";
        }
    }

    @DeleteMapping("/api/reviews/delete/{reviewText}")
    public ResponseEntity<String> deleteReview(@PathVariable String reviewText) {
        reviewService.deleteReview(reviewText);
        return ResponseEntity.ok("Review deleted successfully");
    }
    @GetMapping(value = "/categories")
    public String categories (Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "Categories/categories";
    }
    @DeleteMapping("/api/categories/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
