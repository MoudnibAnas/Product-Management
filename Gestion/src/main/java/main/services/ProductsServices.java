package main.services;

import main.models.Product;
import main.services.interfaces.ProductService;
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
public class ProductsServices implements ProductService {

    @Value("classpath:xml/products.xml")
    private Resource xmlFileResource;

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            XMLNode[] productNodes = rootNode.children("product");
            for (XMLNode productNode : productNodes) {
                Product product = new Product();
                product.setProductId(productNode.child("productId").getValue());
                product.setName(productNode.child("name").getValue());
                product.setDescription(productNode.child("description").getValue());
                product.setPrice(Double.parseDouble(productNode.child("price").getValue()));
                product.setCategory(productNode.child("category").getValue());
                product.setImageUrl(productNode.child("imageUrl").getValue());
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public Product getProductById(String id) {
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            XMLNode[] productNodes = rootNode.children("product");
            for (XMLNode productNode : productNodes) {
                if (productNode.child("productId").getValue().equals(id)) {
                    Product product = new Product();
                    product.setProductId(productNode.child("productId").getValue());
                    product.setName(productNode.child("name").getValue());
                    product.setDescription(productNode.child("description").getValue());
                    product.setPrice(Double.parseDouble(productNode.child("price").getValue()));
                    product.setCategory(productNode.child("category").getValue());
                    product.setImageUrl(productNode.child("imageUrl").getValue());
                    return product;
                }
            }

        } catch (IOException e) {
            System.out.println("failed to check for id ");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Product addProduct(Product product) {
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            if (rootNode.getDocument() == null)  rootNode.setDocument();
            Document doc = rootNode.getDocument();
            Element newElement = doc.createElement("product");
            XMLNode newProductNode = new XMLNode(newElement, doc);
            newProductNode.addChild("productId", product.getProductId());
            newProductNode.addChild("name", product.getName());
            newProductNode.addChild("description", product.getDescription());
            newProductNode.addChild("price", String.valueOf(product.getPrice()));
            newProductNode.addChild("category", product.getCategory());
            newProductNode.addChild("imageUrl", product.getImageUrl());
            rootNode.addChild(newProductNode);
            rootNode.saveToFile(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return product;
    }




    @Override
    public Product updateProduct(Product product) {
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            XMLNode[] productNodes = rootNode.children("product");
            for (XMLNode productNode : productNodes) {
                String currentProductId = productNode.child("productId").getValue();
                if (currentProductId.equals(product.getProductId())) {
                    productNode.child("name").setValue(product.getName());
                    productNode.child("description").setValue(product.getDescription());
                    productNode.child("price").setValue(String.valueOf(product.getPrice()));
                    productNode.child("category").setValue(product.getCategory());
                    productNode.child("imageUrl").setValue(product.getImageUrl());
                    rootNode.saveToFile(file.getAbsolutePath());
                    return product;
                }
            }
            System.out.println("Product with ID " + product.getProductId() + " not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void deleteProduct(String id) {
        try {
            File file = xmlFileResource.getFile();
            XMLNode rootNode = new XMLNode(file.getAbsolutePath());
            XMLNode[] productNodes = rootNode.children("product");
            for (XMLNode productNode : productNodes) {
                if (productNode.child("productId").getValue().equals(id)) {
                    System.out.println("Found product with ID: " + id + ". Deleting...");
                    rootNode.removeChild(productNode);
                    System.out.println("Product removed from XMLNode. Saving changes...");
                    rootNode.saveToFile(file.getAbsolutePath());
                    XMLNode testRoot = new XMLNode(file.getAbsolutePath());
                    XMLNode[] updatedNodes = testRoot.children("product");
                    System.out.println("Products remaining after deletion: " + updatedNodes.length);
                    return;
                }
            }
            System.out.println("Product ID " + id + " not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
