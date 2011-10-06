package audr.text.summarizer.document;

public class ChineseDocument extends Document {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void measureTF() {
		// TODO Auto-generated method stub
		String termArray[]=getSortedTermArray();
		tfEachSentences=new int[sentences.size()][terms.size()];
		tfOverall=new int[terms.size()];
		for(int i=0;i<sentences.size();++i){
			String s=sentences.get(i);
			for(int j=0;j<termArray.length;++j){
				String term=termArray[j];
				int num=0;
				int index=0;
				while(index>=0){
					index=s.indexOf(term,index);
					if(index>=0){
						index++;
						num++;
					}
				}
				tfEachSentences[i][j]+=num;
				tfOverall[j]+=num;
			}
		}
	}
}
