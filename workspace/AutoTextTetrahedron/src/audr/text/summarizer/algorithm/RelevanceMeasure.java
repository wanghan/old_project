package audr.text.summarizer.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;


import audr.text.summarizer.document.ChineseDocumentHandler;
import audr.text.summarizer.document.Document;
import audr.text.summarizer.utils.Constants;

public class RelevanceMeasure {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File file=new File("./c_sample.txt");
		Document doc=new ChineseDocumentHandler(file).getDocument();
		ArrayList<String> s=new RelevanceMeasure().getSummary(doc);
		for (String string : s) {
			System.out.println(string);
		}
	}
	
	
	
	public ArrayList<String> getSummary(Document doc){
		
		int returnNum=getExtractSentenceNum(doc.sentences.size());
		if(returnNum==doc.sentences.size()){
			return doc.sentences;
		}
		
		ArrayList<String> result=new ArrayList<String>();	
		int [][] tfEachSentences=doc.getTFForEachSentence();
		int [] tfOverall=doc.getTFOverall();
		
		//measure score
		RelevanceScore scores[]=new RelevanceScore[doc.sentences.size()];
		for(int i=0;i<doc.sentences.size();++i){
			scores[i]=new RelevanceScore();
			scores[i].index=i;
			scores[i].score=getInnerProduct(tfEachSentences[i], tfOverall);
		}
		Arrays.sort(scores);
	
		for(int i=0;i<returnNum;++i){
			result.add(doc.sentences.get(scores[i].index));
		}
		
		return result;
	}
	private int getInnerProduct(int []a, int []b){
		int len=a.length;
		int sum=0;
		for(int i=0;i<len;++i){
			sum+=a[i]*b[i];
		}
		return sum;
	}
	private int getExtractSentenceNum(int senNum){
		int a=(int)Math.floor(1.0*senNum*Constants.ExtractPercentage);
		if(a==1)
			return 2;
		else if(a==0){
			
			return senNum;
		}
		else{
			return a;
		}
	}
}
