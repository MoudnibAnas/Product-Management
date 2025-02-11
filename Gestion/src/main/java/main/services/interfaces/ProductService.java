package main.services.interfaces;

import main.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(String id);
    Product addProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(String id);
}
