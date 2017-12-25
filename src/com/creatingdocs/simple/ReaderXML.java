package com.creatingdocs.simple;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class ReaderXML {
	
	public ArrayList<Map> reader(String fileName){
		
		//Map<String, String> placeholders = new HashMap<String, String>();
		
		ArrayList<Map> info = new ArrayList();
		
		try {
			
			File fXmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
	
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
	
			Node root = doc.getDocumentElement();
			
			NodeList children = root.getChildNodes();
			
			HashSet<String> baseNodes = new HashSet<String>();
			
			for (int temp = 0, len = children.getLength(); temp < len; temp++) {
				Node nNode = children.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					baseNodes.add(nNode.getNodeName());				
					//System.out.println("\nCurrent Element :" + nNode.getNodeName());
				}
			}
			
			//System.out.print(baseNodes);
			
			//general tags
			for(String tagName : baseNodes){
				
				
				NodeList nList = doc.getElementsByTagName(tagName);
				
				for (int temp = 0, len = nList.getLength(); temp < len; temp++) {
					
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						
						Map<String, String> object = new HashMap<String, String>();
						
						NodeList childBase = nNode.getChildNodes();
						for (int i = 0, leng = childBase.getLength(); i < leng; i++) {
							if (childBase.item(i).getNodeType() == Node.ELEMENT_NODE) {
								object.put(childBase.item(i).getNodeName(), childBase.item(i).getTextContent());
							}
						}
						 
						//System.out.println(object);
						
						if(!object.isEmpty()) {
							info.add(object);
						}
						
					}
				}
				
				
			}
			

			
	
	    } catch (Exception e) {
		e.printStackTrace();
	    }
		
		
		
		return info;
	}
}
