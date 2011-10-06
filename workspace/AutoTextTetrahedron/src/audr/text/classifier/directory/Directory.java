package audr.text.classifier.directory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.apache.lucene.index.CorruptIndexException;

import audr.text.feature.FeatureLearnerBase;
import audr.text.feature.FeatureWeight;
import audr.text.feature.IGFeatureLearner;
import audr.text.feature.utils.Constants;

/**
 * 
 * @author wanghan
 *
 */
public class Directory {

	private static Directory instance=null;
	
	private HashMap<String, Term> Vocabulary;
	
	public static Directory getInstance(){
		if(instance==null){
			instance=new Directory();
		}
		return instance;
	}
	
	public Directory() {
		// TODO Auto-generated constructor stub
		this.Vocabulary=new HashMap<String, Term>();
	}
	
	public void Learn() throws CorruptIndexException, IOException{
		FeatureLearnerBase features=new IGFeatureLearner();
		features.Learn();
		features.optimize();
		
		int index=1;
		
		for (String term : features.getSelectedFeatures().keySet()) {
			FeatureWeight fw=features.getSelectedFeatures().get(term);
			Term word=new Term();
			word.setIndex(index++);
			word.setWeight(fw.getWeight());
			word.setWord(term);
			this.Vocabulary.put(term, word);
		}
		this.storeData();
	}
	
	public void load() throws IOException, ClassNotFoundException{
		this.loadData();
	}
	
	private void loadData() throws IOException, ClassNotFoundException{
		FileInputStream fos=new FileInputStream(this.getStorePath());
		ObjectInputStream oos=new ObjectInputStream(fos);
		this.Vocabulary=(HashMap<String, Term>)oos.readObject();
	}
	/**
	 *  Deserialized data
	 * @throws IOException
	 */
	private void storeData() throws IOException{
		
		FileOutputStream fos=new FileOutputStream(this.getStorePath());
		ObjectOutputStream oos=new ObjectOutputStream(fos);

		oos.writeObject(this.Vocabulary);
		
		oos.flush();
		oos.close();
	}
	
	private String getStorePath(){
		String filePath=Constants.MODEL_DATA_ROOT+"Directory.model";
		return filePath;
	}
}	
