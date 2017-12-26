package com.creatingdocs.simple;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;



public class WordExtractor {
	private String fileTemplateName;
	private static final String TEMPLATEPATH ;
	private static final String RESULTPATH ;
	private static final String CONFIGTPATH = "./config.xml";
	
	static {
		 ReaderXML data = new ReaderXML();
			try {
				data.setFileName(CONFIGTPATH);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		 Map<String, String>  config =  data.simpleReader();
		 TEMPLATEPATH = config.get("templatePath");
		 RESULTPATH = config.get("resultPath");
		 
	}
	
	public String getFileTemplateName() {
		return fileTemplateName;
	}

	public void setFileTemplateName(String fileTemplateName) throws Exception {
		if(fileTemplateName.trim().length() == 0)
			throw new Exception("No template name specified");
		
		this.fileTemplateName = fileTemplateName;
	}


	public void createDocsFromTemlate(Map<String, String> base) throws Exception {
        
	    try {
	    	SimpleDateFormat  folderDateFormat = new SimpleDateFormat("dd_MM_yyyy");
	    	String folderName = getFileTemplateName()+"_"+folderDateFormat.format(new Date());
	    	File folder = new File(RESULTPATH +"/"+ folderName);
			if (!folder.exists()) {
				folder.mkdir();
			}

	    	String person = "";
	    	String[] fullName = new String[3];

	    	String template = TEMPLATEPATH+"/"+ getFileTemplateName()+".docx";
	    	if ((new File(template)).exists()) {
					XWPFDocument doc = new XWPFDocument(OPCPackage.open(template));
					
					for (XWPFParagraph p : doc.getParagraphs()) {
					    List<XWPFRun> runs = p.getRuns();
					    if (runs != null) {
					        for (XWPFRun r : runs) {
					            String text = r.getText(0);
					            
					            for (Map.Entry<String, String> entry : base.entrySet()) {
					                ///System.out.println(entry.getKey() + "  " + entry.getValue());  
					            	if(entry.getKey().equals("surName")) {
					            		fullName[0] = entry.getValue().trim();
					            	}else if(entry.getKey().equals("firstName")) {
					            		fullName[1] = entry.getValue().trim();
					            	}
					            	else if(entry.getKey().equals("patronymic")) {
					            		fullName[2] = entry.getValue().trim();
					            	}
		
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
					            
					            if (text != null && text.contains("{{fullName}}")) {
					            	String fullNameStr = fullName[0]+" "+ fullName[1]+" "+fullName[2]+" ";
					                text = text.replace("{{fullName}}", fullNameStr);
					                r.setText(text, 0);
					            }
					            
					            if (text != null && text.contains("{{shirtName}}")) {
					            	String shirtNameStr = fullName[0]+" "+ fullName[1].charAt(0)+"."+fullName[2].charAt(0)+".";
					                text = text.replace("{{shirtName}}", shirtNameStr);
					                r.setText(text, 0);
					            }
					        }  
					    }
					    person = fullName[0]+ fullName[1].toUpperCase().charAt(0)+fullName[2].toUpperCase().charAt(0);
					}

					//checkin if file exists(in two or more person have simple name)  
					String result = getResultPath(person, folderName);
				 
					doc.write(new FileOutputStream(result));
					
					//check that all placeholders have been replaced
					if(!isAllReplaced(result)) {
						File file = new File(result);
					    file.delete();
						throw new Exception("the document was not formed due to a lack of input data");
					}
					
					
					
	    	} else {
	    		throw new Exception("File not exists");
	    	}
	     }finally {
	    	
	     }
	}
	
	
	private String  getResultPath(String person, String folderName) {
		
		String result = RESULTPATH+"/"+folderName+"/"+getFileTemplateName()+"_"+person+".docx";
		
		File dir = new File(RESULTPATH+"/"+folderName); 
		File[] arrFiles = dir.listFiles();
		List<File> lst = Arrays.asList(arrFiles);
		int kol = -1;
		String pat = ".+"+person+".+";
		for(File s : lst) {
			Pattern p = Pattern.compile(pat);  
			Matcher m = p.matcher(s.getName());  
			if(m.matches()) {
			   kol++;
			}
		}
		
		if ( kol!=-1 ) {
			   result = RESULTPATH+"/"+folderName+"/"+getFileTemplateName()+"_"+person+kol+".docx";
		}
		
		return result;
	}
	
	
	
	private Boolean isAllReplaced(String result) throws FileNotFoundException, IOException {
		
		XWPFDocument docx = new XWPFDocument(new FileInputStream(result));
	      //using XWPFWordExtractor Class
	      XWPFWordExtractor we = new XWPFWordExtractor(docx);
	      String  temp = we.getText();
	      Pattern p = Pattern.compile("\\{\\{(.*)?\\}\\}"); 

			Matcher m = p.matcher(temp);  
			if(m.find()) {
			   return false;
			}
	      return true;
	}
}



