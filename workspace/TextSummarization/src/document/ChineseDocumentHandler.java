package document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import filter.StopWordFilter;

public class ChineseDocumentHandler extends DocumentHandler{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file=new File("./c_sample.txt");
		Document doc=new ChineseDocumentHandler(file).getDocument();
		System.out.println(1);
	}
	public ChineseDocumentHandler(File file) {
		// TODO Auto-generated constructor stub
		super(file);
	}
	@Override
	public ChineseDocument getDocument() {
		// TODO Auto-generated method stub
		ChineseDocument doc=new ChineseDocument();
		
		try {
			Scanner reader=new Scanner(file);
			while(reader.hasNext()){
				String line=reader.nextLine();
				StringTokenizer st=new StringTokenizer(line,"¡£ ");
				while(st.hasMoreTokens()){
					String sentence=st.nextToken().trim();
					if(sentence.length()>18){
						doc.sentences.add(sentence);
						doc.terms.addAll(getTermsInSentence(sentence));
					}
					
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
		IKSegmentation segmentation=new IKSegmentation(new StringReader(sen));
		while(true){
			try {
				Lexeme ll=segmentation.next();
				if(ll==null){
					break;
				}
				else{
					if(ll.getLexemeType()==Lexeme.TYPE_CJK_NORMAL){
						String temp=ll.getLexemeText();
						if(!filter.isStopWord(temp)){
							terms.add(temp);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return terms;
	}
}
