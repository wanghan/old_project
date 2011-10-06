package document;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import filter.StopWordFilter;

public class EnglishDocumentHandler extends DocumentHandler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file=new File("./e_sample.txt");
		Document doc=new EnglishDocumentHandler(file).getDocument();
		System.out.println(1);
	}
	
	public EnglishDocumentHandler(File file) {
		// TODO Auto-generated constructor stub
		super(file);
	}
	
	@Override
	public Document getDocument() {
		// TODO Auto-generated method stub
		
		Document doc=new Document();
		
		try {
			Scanner reader=new Scanner(file);
			while(reader.hasNext()){
				String line=reader.nextLine();
				StringTokenizer st=new StringTokenizer(line,".");
				while(st.hasMoreTokens()){
					String sentence=st.nextToken();
					doc.sentences.add(sentence);
					doc.terms.addAll(getTermsInSentence(sentence));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return doc;
	}
	private ArrayList<String> getTermsInSentence(String sen){
		StopWordFilter filter=StopWordFilter.getInstance();
		ArrayList<String> terms=new ArrayList<String>();
		StringTokenizer st=new StringTokenizer(sen," ,");
		while(st.hasMoreTokens()){
			String term=st.nextToken().trim().toLowerCase();
			if(!filter.isStopWord(term)){
				terms.add(term);
			}
		}
		return terms;
	}
}
