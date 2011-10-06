

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;


/**
 * @author wanghan
 *
 */
public class DataSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 953317450726322564L;

	public Vector<Document> documentSet;
	public int N;//Number of words in corpus
	public HashMap<Integer, Word> GlobalIndexWordMap;
	public HashMap<Integer, Document> GlobalIndexDocMap;
	
	private int docIndex=0;
	
	public DataSet() {
		// TODO Auto-generated constructor stub
		
		this.documentSet=new Vector<Document>();
		
		this.GlobalIndexDocMap=new HashMap<Integer, Document>();
		this.GlobalIndexWordMap=new HashMap<Integer, Word>();
		this.N=0;
	}
	
	public void insert(String id, String content,GlobalData globalData){

		Document document=new Document(docIndex++,id);
		Vector<String> tokens=StringUtils.splitStringToWords(content);
		
		for (String string : tokens) {
			Word word =globalData.GetWordWithInsert(string);
			document.InsertWord(word,N);
			GlobalIndexDocMap.put(N, document);
			GlobalIndexWordMap.put(N++, word);
		}
		documentSet.add(document);
	}
}
