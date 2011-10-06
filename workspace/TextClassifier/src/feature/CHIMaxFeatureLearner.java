/**
 * 
 */
package feature;

import index.IndexFields;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;


import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.wltea.analyzer.lucene.IKQueryParser;

import utils.Constants;

/**
 * @author wanghan
 *
 */
public class CHIMaxFeatureLearner extends FeatureLearnerBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			CHIMaxFeatureLearner featureLearner=new CHIMaxFeatureLearner();
			System.out.println(featureLearner.getStorePath());
			featureLearner.Learn();
			featureLearner.optimize();
			featureLearner.storeData();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	private Vector<FeatureWeight> maxFeatureWeight;
	private float[] CategoryProbabilities;  //每一个分类的概率
	private int[] CategoryTextCount;        //每一个分类包含文档的数目
	private int totalTextNumber=0;
	
	protected HashMap<String, FeatureWeight> maxSelectedFeatures;
	
	public CHIMaxFeatureLearner() throws CorruptIndexException, IOException {
		// TODO Auto-generated constructor stub
		super();
		this.maxFeatureWeight=new Vector<FeatureWeight>();
		CategoryProbabilities=new float[Constants.CATEGORYS.length];
		CategoryTextCount=new int[Constants.CATEGORYS.length];
		maxSelectedFeatures=new HashMap<String, FeatureWeight>();
	}
	@Override
	public double getWeight(String token, Searcher searcher, int cateGory)
			throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void Learn() throws CorruptIndexException, IOException {
		// TODO Auto-generated method stub
		//count text number for each category
		for(int i=0;i<Constants.TRAINSETPATHS.length;++i){
			String curPath=Constants.TRAINSETPATHS[i];
			File curDir=new File(curPath);
			File curFiles[]=curDir.listFiles();
			CategoryTextCount[i]=curFiles.length;
			totalTextNumber+=curFiles.length;
		}
		for(int i=0;i<Constants.TRAINSETPATHS.length;++i){
			String curPath=Constants.TRAINSETPATHS[i];
			File curDir=new File(curPath);
			File curFiles[]=curDir.listFiles();
			CategoryProbabilities[i]=(float)1.0*curFiles.length/totalTextNumber;
		}
		
		super.Learn();
	}
	@Override
	public void hooker(String token, Searcher searcher, int cateGory, int dfOfToken)
			throws IOException {
		// TODO Auto-generated method stub
		Query query=IKQueryParser.parse(IndexFields.CONTENTS, token);
		TreeSet<Term> terms=new TreeSet<Term>();
		query.extractTerms(terms);

		double max=Double.MIN_VALUE;
		double avg=0;
		int df=searcher.docFreq(terms.first());
		for(int i=0;i<Constants.CATEGORYS.length;++i){
			int tInC=searchers[i].docFreq(terms.first());
			int NotTInC=CategoryTextCount[i]-tInC;
			int tInNotC=df-tInC;
			int notTInNotC=totalTextNumber-tInC-tInNotC-NotTInC;
			
			//measure chi 
			double ad_cb=tInC*notTInNotC-NotTInC*tInNotC;
			double chi=1.0*totalTextNumber*ad_cb*ad_cb/(tInC+tInNotC)/(tInC+NotTInC)/(notTInNotC+tInNotC)/(notTInNotC+NotTInC);
			
			if(chi>max){
				max=chi;
			}
			avg+=chi*CategoryProbabilities[i];
		}
		maxFeatureWeight.add(new FeatureWeight(token,max,dfOfToken));
		
	}
	@Override
	public void optimize() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Collections.sort(maxFeatureWeight);
		int remain=(int)(maxFeatureWeight.size()*Constants.FEATURE_FILTER);
		for(int i=0;i<remain;++i){
			maxFeatureWeight.get(i).setIndex(i+1);
			this.maxSelectedFeatures.put(maxFeatureWeight.get(i).getFeature(), maxFeatureWeight.get(i));
		}
	}
	@Override
	public void storeData() throws IOException {
		// TODO Auto-generated method stub
		FileOutputStream fos=new FileOutputStream(this.getStorePath());
		ObjectOutputStream oos=new ObjectOutputStream(fos);

		oos.writeObject(this.maxFeatureWeight);
		oos.flush();
		oos.close();
	}
	
	@Override
	public void loadData() throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		FileInputStream fos=new FileInputStream(this.getStorePath());
		ObjectInputStream oos=new ObjectInputStream(fos);
		Vector<FeatureWeight> maxfeatures=(Vector<FeatureWeight>)oos.readObject();
		this.maxFeatureWeight=maxfeatures;
	}
	@Override
	public boolean isFeature(String token) {
		// TODO Auto-generated method stub
		Object o=maxSelectedFeatures.get(token);
		if(o==null)
			return false;
		else{
			return true;
		}
	}
	public FeatureWeight getFeature(String token){
		Object o=maxSelectedFeatures.get(token);
		if(o==null)
			return null;
		else{
			return (FeatureWeight)o;
		}
	}
}
