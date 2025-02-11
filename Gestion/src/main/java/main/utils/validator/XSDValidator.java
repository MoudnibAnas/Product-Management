package main.utils.validator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;

@Component
public class XSDValidator {

    public boolean validateXml(InputStream xmlInputStream, String schemaType) {
        try {
            String xsdPath = "xsd/" + (schemaType.equalsIgnoreCase("reviews") ? "reviews.xsd" : "products.xsd");
            InputStream xsdStream = new ClassPathResource(xsdPath).getInputStream();
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Source xsdSource = new StreamSource(xsdStream);
            Schema schema = factory.newSchema(xsdSource);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlInputStream));
            return true;
        } catch (SAXException | IOException e) {
            System.out.println("Validation failed: " + e.getMessage());
            return false;
        }
    }
}
