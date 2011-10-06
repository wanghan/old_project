package audr.text.summarizer.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;

import audr.text.summarizer.document.ChineseDocumentHandler;
import audr.text.summarizer.document.Document;
import audr.text.summarizer.utils.Constants;


import Jama.Matrix;



public class LatentSemanticAnalysis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file=new File("./c_sample.txt");
		Document doc=null;
		try {
			doc = new ChineseDocumentHandler(file).getDocument();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> s=new LatentSemanticAnalysis().getSummary(doc);
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

		Matrix matrix=new Matrix(doc.terms.size(),doc.sentences.size());
		for(int i=0;i<doc.terms.size();++i){
			for(int j=0;j<doc.sentences.size();++j){
				matrix.set(i, j, tfEachSentences[j][i]);
			}
		}
		
		Matrix v=matrix.svd().getV();
		v=v.transpose();
		
		HashSet<Integer> hasReturned=new HashSet<Integer>();
		for(int i=0;i<v.getColumnDimension();++i){
			double max=Double.NEGATIVE_INFINITY;
			int max_index=-1;
			for(int j=0;j<v.getRowDimension();++j){
				if(max<v.get(j, i)){
					max=v.get(j, i);
					max_index=j;
				}
			}
			if(!hasReturned.contains(max_index)){
				result.add(doc.sentences.get(max_index));
				hasReturned.add(max_index);
				if(returnNum==result.size())
					break;
			}
		}

		return result;
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
