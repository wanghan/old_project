package document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Document {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public ArrayList<String> sentences;
	public HashSet<String> terms;
	
	protected int [][] tfEachSentences;
	protected int [] tfOverall;
	public Document() {
		// TODO Auto-generated constructor stub
		this.sentences=new ArrayList<String>();
		this.terms=new HashSet<String>();
		tfEachSentences=null;
		tfOverall=null;
	}
	public String[] getSortedTermArray(){
		String termArray[]=terms.toArray(new String[0]);
		Arrays.sort(termArray);
		return termArray;
	}
	protected void measureTF(){
		String termArray[]=getSortedTermArray();
		tfEachSentences=new int[sentences.size()][terms.size()];
		tfOverall=new int[terms.size()];
		for(int i=0;i<sentences.size();++i){
			String s=sentences.get(i);
			StringTokenizer st=new StringTokenizer(s," ,.");
			while(st.hasMoreTokens()){
				String temp=st.nextToken().trim().toLowerCase();
				int index=Arrays.binarySearch(termArray, temp);
				if(index>=0){
					tfEachSentences[i][index]++;
					tfOverall[index]++;
				}
			}
		}
	}
	public int [][] getTFForEachSentence(){
		if(tfEachSentences==null)
			measureTF();
			
		return tfEachSentences;
	}
	
	public int [] getTFOverall(){
		if(tfOverall==null){
			measureTF();
		}
		return tfOverall;
	}
}
