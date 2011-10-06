/**
 * 
 */
package audr.text.feature;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.TreeSet;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.wltea.analyzer.lucene.IKQueryParser;

import audr.text.feature.index.IndexFields;
import audr.text.feature.utils.Constants;
/**
 * @author wanghan
 *
 */
public class IGFeatureLearner extends FeatureLearnerBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4548974313110935515L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			IGFeatureLearner featureLearner=new IGFeatureLearner();
			System.out.println(featureLearner.getStorePath());
			featureLearner.Learn();
			featureLearner.optimize();
			featureLearner.storeData();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private float[] CategoryProbabilities;  //每一个分类的概率
	private int[] CategoryTextCount;        //每一个分类包含文档的数目
	private int totalTextNumber=0;
	private double entropyS=0;
	
	public IGFeatureLearner() throws CorruptIndexException, IOException {
		// TODO Auto-generated constructor stub
		super();
		CategoryProbabilities=new float[Constants.CATEGORYS.length];
		CategoryTextCount=new int[Constants.CATEGORYS.length];
		
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
			//measure entropyS
			entropyS-=CategoryProbabilities[i]*log2n(CategoryProbabilities[i]);
		}
		
		super.Learn();
	}
	
	
	@Override
	public double getWeight(String token, Searcher searcher, int cateGory)
			throws IOException {
		// TODO Auto-generated method stub
		double result=entropyS;
		
		Query query=IKQueryParser.parse(IndexFields.CONTENTS, token);
		TreeSet<Term> terms=new TreeSet<Term>();
		query.extractTerms(terms);
		double df=searcher.docFreq(terms.first());
		double pt=df/totalTextNumber;
		double entropyT=0;
		double entropyNotT=0;
		for(int i=0;i<Constants.CATEGORYS.length;++i){
			
			double dfInc=searchers[i].docFreq(terms.first());
			
			double pcIFt=dfInc/totalTextNumber/pt;
			double pcIFNott=(CategoryTextCount[i]-dfInc)/totalTextNumber/(1-pt);
			entropyT-=pcIFt*log2n(pcIFt);
			entropyNotT-=(pcIFNott)*log2n(pcIFNott);
		}
		result=result-pt*entropyT-(1-pt)*entropyNotT;
		return result;
	}
	@Override
	public void optimize() {
		// TODO Auto-generated method stub
		Collections.sort(features);
		int remain=(int)(features.size()*Constants.FEATURE_FILTER);
		for(int i=0;i<remain;++i){
			features.get(i).setIndex(i+1);
			this.selectedFeatures.put(features.get(i).getFeature(), features.get(i));
		}
	}
	@Override
	public void hooker(String token, Searcher searcher, int cateGory,int df) {
		// TODO Auto-generated method stub
		//do nothing
	}
	
	
}
