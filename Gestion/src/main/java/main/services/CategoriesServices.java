package main.services;

import main.models.Category;
import main.services.interfaces.CategoryService;
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
public class CategoriesServices implements CategoryService {
    @Value("classpath:xml/categories.xml")
    private Resource xmlFileResource;

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            XMLNode[] categoryNodes = rootNode.children("category");
            for (XMLNode categoryNode : categoryNodes) {
                Category category = new Category();
                category.setCategoryId(categoryNode.child("categoryId").getValue());
                category.setName(categoryNode.child("name").getValue());
                category.setDescription(categoryNode.child("description").getValue());
                categories.add(category);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category getCategoryById(String categoryId) {
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            XMLNode[] categoryNodes = rootNode.children("category");
            for (XMLNode categoryNode : categoryNodes) {
                if (categoryNode.child("categoryId").getValue().equals(categoryId)) {
                    Category category = new Category();
                    category.setCategoryId(categoryNode.child("categoryId").getValue());
                    category.setName(categoryNode.child("name").getValue());
                    category.setDescription(categoryNode.child("description").getValue());
                    return category;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Category updateCategory(Category updatedCategory) {
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            XMLNode[] categoryNodes = rootNode.children("category");
            for (XMLNode categoryNode : categoryNodes) {
                if (categoryNode.child("categoryId").getValue().equals(updatedCategory.getCategoryId())) {
                    categoryNode.child("name").setValue(updatedCategory.getName());
                    categoryNode.child("description").setValue(updatedCategory.getDescription());
                    rootNode.saveToFile(file.getAbsolutePath());
                    return updatedCategory;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Category createCategory(Category category) {
        return null;
    }

    @Override
    public void deleteCategory(String id) {
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            XMLNode[] categoryNodes = rootNode.children("category");
            for (XMLNode categoryNode : categoryNodes) {
                if (categoryNode.child("categoryId").getValue().equals(String.valueOf(id))) {
                    System.out.println("Found category with ID: " + id + ". Deleting...");
                    rootNode.removeChild(categoryNode);
                    System.out.println("Category removed from XMLNode. Saving changes...");
                    rootNode.saveToFile(file.getAbsolutePath());
                    XMLNode testRoot = new XMLNode(file.getAbsolutePath());
                    XMLNode[] updatedNodes = testRoot.children("category");
                    System.out.println("Categories remaining after deletion: " + updatedNodes.length);
                    return;
                }
            }
            System.out.println("Category ID " + id + " not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Category addCategory(Category category) {
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            System.out.println("Categories before adding: " + rootNode.child("category"));
            if (rootNode.getDocument() == null) {
                rootNode.setDocument();
            }
            Document doc = rootNode.getDocument();
            Element newElement = doc.createElement("category");
            XMLNode newCategoryNode = new XMLNode(newElement, doc);
            newCategoryNode.addChild("categoryId", category.getCategoryId());
            newCategoryNode.addChild("name", category.getName());
            newCategoryNode.addChild("description", category.getDescription());
            rootNode.addChild(newCategoryNode);
            rootNode.saveToFile(file.getAbsolutePath());
            XMLNode[] updatedNodes = rootNode.children("category");
            System.out.println("Categories remaining after adding: " + updatedNodes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return category;
    }

}
