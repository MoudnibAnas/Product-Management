package main.utils.validator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

@Component
public class DTDValidator {

    public boolean validateXml(InputStream xmlInputStream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {
                    throw new SAXException("Warning: " + exception.getMessage());
                }
                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw new SAXException("Error: " + exception.getMessage());
                }
                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw new SAXException("Fatal Error: " + exception.getMessage());
                }
            });
            InputStream dtdStream = new ClassPathResource("dtd/categories.dtd").getInputStream();
            InputSource dtdSource = new InputSource(dtdStream);
            builder.setEntityResolver((publicId, systemId) -> dtdSource);
            builder.parse(new InputSource(xmlInputStream));
            return true;
        } catch (SAXException | ParserConfigurationException | IOException e) {
            System.out.println("Validation failed: " + e.getMessage());
            return false;
        }
    }
}
