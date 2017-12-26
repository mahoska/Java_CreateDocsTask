package com.creatingdocs.simple;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args)throws Exception {

		try {
			
			Scanner in = new Scanner(System.in);
		    String buffer = new String();
		    System.out.print("Enter template name: ");  
		    //Bonus - norm file; 
		    //Bonus_test - There is not enough data to replace
		    buffer=in.nextLine();

		   ReaderXML data = new ReaderXML();
		   data.setFileName("./data.xml");
			
		   ArrayList<Map> info =  data.reader();
		   WordExtractor generator =  new WordExtractor();
		   for(Map<String, String> object : info) {
			   generator.setFileTemplateName(buffer);
			   generator.createDocsFromTemlate(object);
			   System.out.println("document create");
		   } 
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	  
	}

}
