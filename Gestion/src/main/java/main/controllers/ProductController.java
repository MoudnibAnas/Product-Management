package main.controllers;

import main.models.Product;
import main.models.Review;
import main.services.interfaces.ProductService;
import main.services.ProductsServices;
import main.services.interfaces.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping(value = "/generate-xslt-file/{productId}")
    public ResponseEntity<String> generateXSLT(@PathVariable String productId) {
        Product product = productService.getProductById(productId);
        List<Review> reviews = reviewService.getReviewsByProductId(productId);

        if (product != null) {
            StringBuilder xsltTemplateBuilder = new StringBuilder();
            xsltTemplateBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                    .append("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">")
                    .append("  <xsl:template match=\"/\"> ")
                    .append("    <html>")
                    .append("      <head>")
                    .append("        <title>Product Details</title>")
                    .append("      </head>")
                    .append("      <body>")
                    .append("        <h1>Product: ")
                    .append(product.getName())
                    .append("</h1>")
                    .append("        <p>Description: ")
                    .append(product.getDescription())
                    .append("</p>")
                    .append("        <p>Price: ")
                    .append(product.getPrice())
                    .append("</p>")
                    .append("        <h2>Reviews:</h2>")
                    .append("        <ul>");

            for (Review review : reviews) {
                xsltTemplateBuilder.append("          <li>Rating: ")
                        .append(review.getRating())
                        .append("</li>")
                        .append("          <li>Customer: ")
                        .append(review.getCustomerId())
                        .append("</li>")
                        .append("          <li>Text: ")
                        .append(review.getReviewText())
                        .append("</li>");
            }

            xsltTemplateBuilder.append("        </ul>")
                    .append("      </body>")
                    .append("    </html>")
                    .append("  </xsl:template>")
                    .append("</xsl:stylesheet>");

            String xsltTemplate = xsltTemplateBuilder.toString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_XML);
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename("product_" + productId + ".xsl")
                            .build());

            return new ResponseEntity<>(xsltTemplate, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
