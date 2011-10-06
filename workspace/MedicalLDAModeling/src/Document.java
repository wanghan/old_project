

import java.io.Serializable;
import java.util.Vector;


/**
 * @author wanghan
 *
 */
public class Document implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2821873368132169777L;

	private int index;
	private String id;
	private String content;
	private Vector<Word> bagOfWords;
	private Vector<Integer> bagOfWordGloIndexes;
	
	public Document(int index,String id) {
		// TODO Auto-generated constructor stub
		this.bagOfWords=new Vector<Word>();
		this.bagOfWordGloIndexes=new Vector<Integer>();
		this.index=index;
		this.id=id;
	}

	
	public void InsertWord(Word w,int gloIndex){
		bagOfWords.add(w);
		bagOfWordGloIndexes.add(gloIndex);
	}
	
	public int getBagOfWordSize(){
		return bagOfWords.size();
	}
	

	public Vector<Word> getBagOfWords() {
		return bagOfWords;
	}


	public String getId() {
		return id;
	}


	public String getContent() {
		return content;
	}


	public int getIndex() {
		return index;
	}


	public Vector<Integer> getBagOfWordGloIndexes() {
		return bagOfWordGloIndexes;
	}
}
