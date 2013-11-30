package com.google.glassware;

import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SearchBioPortal {
	private static final Logger LOG = Logger.getLogger(SearchBioPortal.class.getSimpleName());
	//Following http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/ 
	public static String parseXMLFile (String uri) {
		String preferredName = null;
		String Id = null;
		String definition = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(uri);

			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
			LOG.warning("Root element: " + doc.getDocumentElement().getNodeName()); // Root is ""

			NodeList nList = doc.getElementsByTagName("class");
			int numRecords = nList.getLength();

			if (numRecords == 0) {
				LOG.warning("No anatomy terms found");
			}
			else {
				Integer maxResultsToDisplay = 1;  //Hard code number of results to display 
				//for (int first = 0; temp < nList.getLength(); temp++) {
				for (int count = 0; count <= maxResultsToDisplay; count++) {
					Node nNode = nList.item(count);
					LOG.warning("\nCurrent Element:" + nNode.getNodeName());
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						//TODO: May limit search results to X number of hits
						LOG.warning("Term Name: " + eElement.getElementsByTagName("label").item(0).getTextContent());
						LOG.warning("Term ID " + eElement.getElementsByTagName("name").item(0).getTextContent().toLowerCase());
						//LOG.warning("Term Definition: " + eElement.getElementsByTagName("comments/comment").item(0).getTextContent());		
					
						preferredName = eElement.getElementsByTagName("label").item(0).getTextContent();
						Id = eElement.getElementsByTagName("name").item(0).getTextContent();
						//definition = eElement.getElementsByTagName("comments/comment").item(0).getTextContent();
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			LOG.warning("Error with Term search service");
		}
	return Id;
	}
}
