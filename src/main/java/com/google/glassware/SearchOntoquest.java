package com.google.glassware;

import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class SearchOntoquest {
	private static final Logger LOG = Logger.getLogger(SearchOntoquest.class.getSimpleName());
	//Following http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/ 
	public static String parseXMLFile (String uri) {
		String preferredName = null;
		String Id = null;
		String definition = null;
		String displayResult = null;
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(uri);

			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
			LOG.warning("Root element: " + doc.getDocumentElement().getNodeName()); // Root is "class" from NIF Ontoquest web service
			NodeList nList = doc.getElementsByTagName("class");
			int numRecords = nList.getLength();

			if (numRecords == 0) {
				LOG.warning("No anatomy terms found");
				displayResult = "No anatomy terms found";
			}
			else {
				Integer maxResultsToDisplay = 1;  //Hard code number of results to display 
				for (int count = 0; count <= maxResultsToDisplay; count++) {
					Node nNode = nList.item(count);
					LOG.warning("\nCurrent Element:" + nNode.getNodeName());
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						preferredName = eElement.getElementsByTagName("label").item(0).getTextContent();
						Id = eElement.getElementsByTagName("name").item(0).getTextContent();
						LOG.warning("Term Name: " + preferredName);
						LOG.warning("Term ID " + Id);
						
						//Example from: http://www.coderanch.com/t/127860/XML/nested-elements-DOM
						Element commentsNode = (Element) eElement.getElementsByTagName("comments").item(0);
						if (commentsNode != null) {
						NodeList nl2 = commentsNode.getElementsByTagName("comment");  
						Element childNode = (Element) nl2.item(0);  
						Text childNodeText = (Text) childNode.getFirstChild();  
						definition = childNodeText.getNodeValue();  
						LOG.warning("Definition: " + definition);		

						//TODO: Handle correctly
						displayResult = definition+"("+Id+")";
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			LOG.warning("Error with Term search service");
		}
		return displayResult;
	}
}

