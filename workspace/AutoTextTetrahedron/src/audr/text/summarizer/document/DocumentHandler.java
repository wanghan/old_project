package audr.text.summarizer.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DocumentHandler {
	
	protected String fileContent;
	public DocumentHandler(File file) throws FileNotFoundException {
		// TODO Auto-generated constructor stub
		StringBuffer sb=new StringBuffer();
		Scanner reader=new Scanner(file);
		while(reader.hasNext()){
			String line=reader.nextLine();
			sb.append(line+"\n\r");
		}
		reader.close();
		
		this.fileContent=sb.toString();
	}
	public DocumentHandler(String text) {
		// TODO Auto-generated constructor stub
		this.fileContent=text;
	}
	
	public Document getDocument(){
		return null;
	}
}
