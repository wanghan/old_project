/**
 * 
 */
package feature;

import java.io.Serializable;

/**
 * @author wanghan
 *
 */
public class FeatureWeight implements Comparable<FeatureWeight> ,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4982950175830629364L;
	private String feature;
	private double weight;
	private int df;
	private int index;
	private int tf;
	
	private int category;
	
	public FeatureWeight() {
		// TODO Auto-generated constructor stub
		tf=0;
	}
	public FeatureWeight(String feature, double weight,int df){
		super();
		this.feature=feature;
		this.weight=weight;
		category=-1;
		this.df=df;
		
	}
	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	@Override
	public int compareTo(FeatureWeight o) {
		// TODO Auto-generated method stub
		if(this.weight>o.weight){
			return -1;
		}
		else{
			return 1;
		}
	}
	public int getDf() {
		return df;
	}
	public void setDf(int df) {
		this.df = df;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getTf() {
		return tf;
	}
	public void setTf(int tf) {
		this.tf = tf;
	}
	
}
