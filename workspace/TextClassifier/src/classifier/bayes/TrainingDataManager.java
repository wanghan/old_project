/**
 * 
 */
package classifier.bayes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import utils.Constants;

/**
 * @author wanghan
 *
 */
public class TrainingDataManager implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1899003846152896454L;
	private float[] CategoryProbabilities;  //每一个分类的概率
	private int[] CategoryTextCount;        //每一个分类包含文档的数目
	private HashMap<String,TokenProbability> tokenProbabilities; //关键词的先验概率
	
	private int totalTextNumber=0;     //总文本数目
	
	public TrainingDataManager() {
		// TODO Auto-generated constructor stub
		CategoryProbabilities=new float[Constants.CATEGORYS.length];
		tokenProbabilities=new HashMap<String, TokenProbability>();
		CategoryTextCount=new int[Constants.CATEGORYS.length];
	}

	public float[] getCategoryProbabilities() {
		return CategoryProbabilities;
	}

	public void setCategoryProbabilities(float[] categoryProbabilities) {
		CategoryProbabilities = categoryProbabilities;
	}

	public void addTokenProbability(String token, float p,int category){
		TokenProbability tp=this.tokenProbabilities.get(token);
		if(tp==null){
			tp=new TokenProbability(token);
			tp.setProbability(category, p);
			this.tokenProbabilities.put(token, tp);
		}
		else{
			tp.setProbability(category, p);
		}
	}
	public float getTokenProbability(String token, int category){
		TokenProbability tp=this.tokenProbabilities.get(token);
		if(tp==null){
			return 0;
		}
		else{
			return tp.getProbabilities()[category];
		}
	}
	public void setCategoryProbability(int index, float p){
		this.CategoryProbabilities[index]=p;
	}
	public void setCategoryTextNumber(int index, int n){
		this.CategoryTextCount[index]=n;
	}

	public int getTotalTextNumber() {
		return totalTextNumber;
	}

	public void setTotalTextNumber(int totalTextNumber) {
		this.totalTextNumber = totalTextNumber;
	}

	public int[] getCategoryTextCount() {
		return CategoryTextCount;
	}

	public void setCategoryTextCount(int[] categoryTextCount) {
		CategoryTextCount = categoryTextCount;
	}
	public static void storeData(TrainingDataManager data) throws IOException{
		FileOutputStream fos=new FileOutputStream(Constants.TRAINING_RESULT);
		ObjectOutputStream oos=new ObjectOutputStream(fos);

		oos.writeObject(data);
		
		oos.flush();
		oos.close();
	}
	public static TrainingDataManager loadData() throws IOException, ClassNotFoundException{
		FileInputStream fos=new FileInputStream(Constants.TRAINING_RESULT);
		ObjectInputStream oos=new ObjectInputStream(fos);
		return (TrainingDataManager)oos.readObject();
	}
	
}
