package domsearch;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

public class DomSearch {

	public static String searchIdNode(Node currentNode, String targetId) {
	
		if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
			NamedNodeMap attributes = currentNode.getAttributes();
			
			if (attributes != null) {
				Node attrNode = attributes.getNamedItem("id");
				
				if (attrNode != null && targetId.equals(attrNode.getNodeValue())) {
					return currentNode.getNodeName() + "#" + targetId + " / 1개";
				}
			}
		}
		
		NodeList childNodes = currentNode.getChildNodes();
		
		for (int i=0; i<childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			
			String result = searchIdNode(childNode, targetId);
			
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	public static int countNodeName(Node currentNode, String targetNodeName) {
		int count = 0;
		
		if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
			if (currentNode.getNodeName().equalsIgnoreCase(targetNodeName)) {
				count++;
			}
		}
		
		NodeList childNodes = currentNode.getChildNodes();
		
		for (int i=0; i<childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			count += countNodeName(childNode, targetNodeName);
		}
		return count;
	}
	
	public static int countAttrName(Node currentNode, String targetAttrName) {
		int count = 0;
		NodeList childNodes;
		Node childNode;
		NamedNodeMap attributes;
		Node attrNode;
		int i;
		int j;
		
		if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
			attributes = currentNode.getAttributes();
			
			for (j=0; j<attributes.getLength(); j++) {
				attrNode = attributes.item(j);
				
				if (attrNode.getNodeName().equalsIgnoreCase(targetAttrName)) {
					count++;
				}
			}
		}
		
		childNodes = currentNode.getChildNodes();
		
		for (i=0; i<childNodes.getLength(); i++) {
			childNode = childNodes.item(i);
			count += countAttrName(childNode, targetAttrName);
		}
		return count;
	}
	
	public static int countAttrValue(Node currentNode, String targetAttrValue) {
		int count = 0;

		if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
			NamedNodeMap attributes = currentNode.getAttributes();

			for (int i=0; i<attributes.getLength(); i++) {
				Node attrNode = attributes.item(i);

				if (targetAttrValue.equals(attrNode.getNodeValue())) {
					count++;
				}
			}
		}

		NodeList childNodes = currentNode.getChildNodes();

		for (int i=0; i<childNodes.getLength(); i++) {
			count += countAttrValue(childNodes.item(i), targetAttrValue);
		}

		return count;
	}
}
