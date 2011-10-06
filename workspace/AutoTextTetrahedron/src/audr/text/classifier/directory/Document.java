package audr.text.classifier.directory;

import java.util.HashMap;

public class Document {
	private String filepath;
	private int category;
	private HashMap<Integer, Integer> termFrequency;
	
	
	
	public Document() {
		// TODO Auto-generated constructor stub
		this.termFrequency=new HashMap<Integer, Integer>();
	}
}
