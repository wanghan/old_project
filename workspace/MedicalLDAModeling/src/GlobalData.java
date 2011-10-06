

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * @author wanghan
 *
 */
public class GlobalData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3475196420479731031L;
	public Vocabulary wordSet;
	
	public GlobalData() {
		// TODO Auto-generated constructor stub
		this.wordSet=new Vocabulary();
	}
	public GlobalData(Vocabulary v) {
		this.wordSet=v;
	}
	public Word GetWord(int index){
		return wordSet.GetWord(index);
	}
	public Word GetWordWithInsert(String s){
		return wordSet.GetWordWithInsert(s);
	}
	
	public int getWordCount(){
		return wordSet.GetWordCount();
	}

	
	public void serialize(String filepath) throws FileNotFoundException, IOException{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				filepath, false));

		oos.writeObject(this.wordSet);
		oos.flush();

		oos.close();
	}
	
	public static GlobalData deserialize(String filepath) throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				filepath));
		
		Vocabulary words=(Vocabulary) ois.readObject();
		GlobalData result=new GlobalData(words);
		return result;
	}
	
}
