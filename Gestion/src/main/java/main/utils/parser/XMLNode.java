package main.utils.parser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Vector;


public class XMLNode {
	
	private Node node;
	private Document document;

	public XMLNode(Node node) {
		super();
		this.node = node;
		if (this.document == null) {
			setDocument();
		}
	}
	public XMLNode(Node node,Document document) {
		super();
		this.node = node;
		this.document = document;
	}
	public void setDocument() {
		if (this.document == null) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				this.document = builder.newDocument();
			} catch (ParserConfigurationException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public XMLNode(String source) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(source);
			node = document.getFirstChild();
			while (node.getNodeType() != Node.ELEMENT_NODE) {
				node = node.getNextSibling();
			}
			if (document == null) {
				setDocument();
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	public XMLNode[] children() {
		List<XMLNode> list = new Vector<XMLNode>();
		NodeList n1 = node.getChildNodes();
		int n = n1.getLength();
		for (int i = 0; i < n ;i++) {
			Node child = n1.item(i);
			if(child.getNodeType() == Node.ELEMENT_NODE) {
				list.add(new XMLNode(child));
			}
		}
		XMLNode t[]=new XMLNode[list.size()];
		list.toArray(t);
		return t;
	}
	public XMLNode[] children (String name) {
		List<XMLNode> list = new Vector<XMLNode>();
		NodeList n1 = node.getChildNodes();
		int n = n1.getLength();
		for (int i = 0; i < n ;i++) {
			Node child = n1.item(i);
			if(child.getNodeType() == Node.ELEMENT_NODE && name.equals(child.getNodeName())) {
				list.add(new XMLNode(child));
			}
		}
		XMLNode t[]=new XMLNode[list.size()];
		list.toArray(t);
		return t;
	}
	public XMLNode child(String name) {
		NodeList n1 = node.getChildNodes();
		int n = n1.getLength();
		for (int i = 0; i < n ;i++) {
			Node child = n1.item(i);
			if(child.getNodeName().equals(name)){
				return new XMLNode(child);
			}
		}
		return null;
	}
	public String getName() {
		return node.getNodeName();
	}
//??	Un nom n'a de fils que si il na pas de valeur et que sa valeur est le nom de son fils textuel
	public String getValue() {
		NodeList list = node.getChildNodes();
		if(list.getLength() == 1 && list.item(0).getNodeType() == Node.TEXT_NODE) {
			return list.item(0).getNodeValue();			
		}
		return null;
	}
	public String attribute(String name) {
		NamedNodeMap atts = node.getAttributes();
//		System.out.println("The attribute : " + atts);
		return atts.getNamedItem(name) != null ? atts.getNamedItem(name).getNodeValue() : null;
	}
	public int intAttribute(String name) {
		String att = attribute(name);
		try {
			return Integer.parseInt(att);
		} catch (Exception e) {
			return -1;
		}
	}

	public void removeChild(XMLNode childNode) {
		if (node != null && childNode.node != null) {
			node.removeChild(childNode.node);
			System.out.println("Node removed successfully.");
		} else {
			System.out.println("Failed to remove node: Parent or child is null.");
		}
	}

	public void saveToFile(String filePath) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(filePath));

			transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			System.out.println("XML file updated successfully: " + filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addChild(String tagName, String value) {
		if (this.document == null) {
			setDocument();
		}
		Element newElement = document.createElement(tagName);
		newElement.appendChild(document.createTextNode(value));
		node.appendChild(newElement);
	}

	// Method to add a child node that itself contains sub-nodes
	public void addChild(XMLNode childNode) {
		node.appendChild(childNode.getNode());
	}
	private Node getNode() {
		return this.node;
	}
	public void setValue(String value) {
		NodeList list = node.getChildNodes();
		if (list.getLength() == 1 && list.item(0).getNodeType() == Node.TEXT_NODE) {
			list.item(0).setNodeValue(value);
		} else {
			node.setTextContent(value);
		}
	}

	public Document getDocument() {
		return document;
	}
}
