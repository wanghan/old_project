/**
 * 
 */
package feature;

import index.IndexFields;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

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
public class DFFeatureLearner extends FeatureLearnerBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1030635449946124399L;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			DFFeatureLearner featureLearner=new DFFeatureLearner();
			System.out.println(featureLearner.getStorePath());
			featureLearner.Learn();
			featureLearner.optimize();
			featureLearner.storeData();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	public DFFeatureLearner() throws CorruptIndexException, IOException {
		// TODO Auto-generated constructor stub
		super();
	}
	
	
	public double getWeight(String token,Searcher searcher,int category) throws IOException{
		Query query=IKQueryParser.parse(IndexFields.CONTENTS, token);
		TreeSet<Term> terms=new TreeSet<Term>();
		query.extractTerms(terms);
		double weight=searcher.docFreq(terms.pollFirst());
		return weight;
		
	}
	public void optimize(){
		Collections.sort(this.features,new Comparator<FeatureWeight>() {
			@Override
			public int compare(FeatureWeight o1, FeatureWeight o2) {
				// TODO Auto-generated method stub
				if(o1.getWeight()>o2.getWeight()){
					return 1;
				}
				else{
					return -1;
				}
			}
		});
		int remain=(int)(features.size()*Constants.FEATURE_FILTER);
		for(int i=0;i<remain;++i){
			features.get(i).setIndex(i+1);
			this.selectedFeatures.put(features.get(i).getFeature(), features.get(i));
		}
	}
	@Override
	public void hooker(String token, Searcher searcher, int cateGory,int df) {
		// TODO Auto-generated method stub
		
	}
	
}
