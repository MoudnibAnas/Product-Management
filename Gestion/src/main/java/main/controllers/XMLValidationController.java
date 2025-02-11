package main.controllers;

import main.utils.XMLDataAppender;
import main.utils.validator.DTDValidator;
import main.utils.validator.XSDValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class XMLValidationController {

    @Autowired
    private XSDValidator xsdValidator;

    @Autowired
    private DTDValidator dtdValidator;

    @Autowired
    private XMLDataAppender xmlDataAppender;  // Inject XMLDataAppender

    @PostMapping("/validate")
    public ResponseEntity<String> validateXmlFile(
            @RequestParam("xmlFile") MultipartFile file,
            @RequestParam("schemaType") String schemaType,
            @RequestParam("append") boolean append) {  // Add a new parameter to decide whether to append

        try {
            boolean isValid;
            String schemaTypename = "";
            String tagName = "";
            if (schemaType.equalsIgnoreCase("categories")) {
                schemaTypename = "categories";
                tagName = "category";
                isValid = dtdValidator.validateXml(file.getInputStream());
            } else if (schemaType.equalsIgnoreCase("reviews")){
                schemaTypename = "reviews";
                tagName = "review";
                isValid = xsdValidator.validateXml(file.getInputStream(), schemaType);
            } else if (schemaType.equalsIgnoreCase("products")) {
                schemaTypename = "products";
                tagName = "product";
                isValid = xsdValidator.validateXml(file.getInputStream(), schemaType);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid schema type. Use 'categories', 'reviews', or 'products'.");
            }

            if (isValid) {
                if (append) {
                    Element newElement = convertFileToElement(file);
                    Element newProduct = (Element) newElement.getElementsByTagName(tagName).item(0);
                    System.out.println(newElement);
                    if (newProduct != null) {
                        boolean isAppended = xmlDataAppender.appendProductElement("src/main/resources/xml/" + schemaTypename + ".xml", newProduct,schemaTypename);
                        if (isAppended) {
                            return ResponseEntity.ok("Valid XML file and data appended.");
                        } else {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error appending the data.");
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No product element found in the uploaded file.");
                    }
                }
                return ResponseEntity.ok("Valid XML file.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid XML file.");
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file.");
        }
    }

    private Element convertFileToElement(MultipartFile file) throws IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file.getInputStream());
            return document.getDocumentElement();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to parse the file.", e);
        }
    }
}
