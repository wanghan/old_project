package summarizer.simgle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import utils.Constants;

import Jama.Matrix;

import document.ChineseDocumentHandler;
import document.Document;

public class LatentSemanticAnalysis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file=new File("./c_sample.txt");
		Document doc=new ChineseDocumentHandler(file).getDocument();
		ArrayList<String> s=new LatentSemanticAnalysis().getSummary(doc);
		for (String string : s) {
			System.out.println(string);
		}
	}
	public ArrayList<String> getSummary(Document doc){
		ArrayList<String> result=new ArrayList<String>();
		
		int [][] tfEachSentences=doc.getTFForEachSentence();

		Matrix matrix=new Matrix(doc.terms.size(),doc.sentences.size());
		for(int i=0;i<doc.terms.size();++i){
			for(int j=0;j<doc.sentences.size();++j){
				matrix.set(i, j, tfEachSentences[j][i]);
			}
		}
		
		Matrix v=matrix.svd().getV();
		matrix.svd().getS();
		v=v.transpose();
		int returnNum=getExtractSentenceNum(doc.sentences.size());
		HashSet<Integer> hasReturned=new HashSet<Integer>();
		for(int i=0;i<v.getColumnDimension();++i){
			double max=Double.MIN_VALUE;
			int max_index=-1;
			for(int j=0;j<v.getRowDimension();++j){
				if(v.get(j, i)>max){
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
		else{
			return a;
		}
	}
}
