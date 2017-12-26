package com.creatingdocs.simple;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;




public class Main {

	public static void main(String[] args)throws Exception {
		
		try {
		   ReaderXML data = new ReaderXML();
		   ArrayList<Map> info =  data.reader("./data.xml");
		   WordExtractor generator =  new WordExtractor();
		   
		   for(Map<String, String> object : info) {
			   //System.out.println(object);
			   generator.setFileTemplateName("Bonus");
			   generator.createDocsFromTemlate(object);
			   System.out.println("document create");
		   }
		   
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	  
	}

}
