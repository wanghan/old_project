

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * @author wanghan
 *
 */
public class Word implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1632656504457797231L;
	public int Index;
	public String Word;
	
	//add word size 2011/5/5
	private int size;
	
	public Word(int index, String w) {
		// TODO Auto-generated constructor stub
		this.Index=index;
		this.Word=w;
		StringTokenizer st=new StringTokenizer(w);
		int i=0;
		while(st.hasMoreTokens()){
			i++;
			st.nextToken();
		}
		this.size=i;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Word;
	}
	public int getSize() {
		return size;
	}
	
	 
}
