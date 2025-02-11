package main.services.interfaces;

import main.models.Category;
import main.models.Product;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(String id);
    Category createCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategory(String id);

    Category addCategory(Category category);
}
