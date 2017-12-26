package com.creatingdocs.simple;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReaderXML {
	
	private String fileName;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) throws Exception {
		if(fileName.trim().length() == 0)
			throw new Exception("no data file found");
		this.fileName = fileName;
	}

	public ArrayList<Map> reader(){

		ArrayList<Map> info = new ArrayList();
		try {
			
			File fXmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
	
			doc.getDocumentElement().normalize();
	
			Node root = doc.getDocumentElement();
			//System.out.println("\nRoot Element :" + root.getNodeName());
			
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
	
	public Map<String, String>  simpleReader()  {
		Map<String, String> baseNodes = new HashMap<String, String>();
		
		try {
			File fXmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
	
			doc.getDocumentElement().normalize();
	
			Node root = doc.getDocumentElement();
			
			NodeList children = root.getChildNodes();
			
			for (int temp = 0, len = children.getLength(); temp < len; temp++) {
				Node nNode = children.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					baseNodes.put(nNode.getNodeName(), nNode.getTextContent());				
				}
			}

	    } catch (Exception e) {
		e.printStackTrace();
	    }
		
		return baseNodes;
	}
	
	
}
