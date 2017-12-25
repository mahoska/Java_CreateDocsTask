package com.creatingdocs.simple;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;



public class WordExtractor {

	public void createDocsFromTemlate(String fileTemplatePath,String fileCreatePath, Map<String, String> base) {
        
	    try { 
			XWPFDocument doc = new XWPFDocument(OPCPackage.open(fileTemplatePath));
			for (XWPFParagraph p : doc.getParagraphs()) {
				
			    List<XWPFRun> runs = p.getRuns();
			    if (runs != null) {
			        for (XWPFRun r : runs) {
			            String text = r.getText(0);
			           	
			            for (Map.Entry<String, String> entry : base.entrySet()) {
			                //System.out.println(entry.getKey() + "  " + entry.getValue());   
			                String placeholder = new String("{{"+entry.getKey()+"}}"); 
			                if (text != null && text.contains(placeholder)) {
				                text = text.replace(placeholder, entry.getValue());
				                r.setText(text, 0);
				            }
			            }
			            
			            if (text != null && text.contains("{{data}}")) {
			            	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			                text = text.replace("{{data}}", dateFormat.format(new Date()));
			                r.setText(text, 0);
			            }
			           
			        }  
			    }
			    
			    doc.write(new FileOutputStream(fileCreatePath));
			    
			}
	     }
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

}
