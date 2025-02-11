package main.utils;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.springframework.stereotype.Component;

@Component
public class XMLDataAppender {

    public boolean appendElement(String filePath, Element newElement) {
        try {
            File xmlFile = new File(filePath);
            System.out.println(xmlFile.getAbsolutePath());
            if (!xmlFile.exists()) {
                System.err.println("XML file not found: " + filePath);
                return false;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            Element root = document.getDocumentElement();
            Node importedNode = document.importNode(newElement, true);
            root.appendChild(importedNode);
            saveDocument(document, filePath);
            return true;
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveDocument(Document document, String filePath) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }

    public boolean appendProductElement(String filePath, Element newElement, String elementname) {
        try {
            File xmlFile = new File(filePath);
            if (!xmlFile.exists()) {
                System.err.println("XML file not found: " + filePath);
                return false;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            Element root = document.getDocumentElement();
            if (root.getNodeName().equals(elementname)) {
                Node importedNode = document.importNode(newElement, true);
                root.appendChild(importedNode);
                saveDocument(document, filePath);
                return true;
            } else {
                System.err.println("Root element is not " + elementname +  ", aborting append.");
                return false;
            }
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
            return false;
        }
    }

}
