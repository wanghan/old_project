/**
 * 
 */
package classifier.bayes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;

import utils.Constants;

import evaluate.EvaluateUnit;
import evaluate.Evaluator;
import feature.CHIAvgFeatureLearner;
import feature.DFFeatureLearner;
import feature.IGFeatureLearner;
import feature.MIAvgFeatureLearner;

/**
 * @author t-hawa
 *
 */
public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Filter:" +Constants.FEATURE_FILTER);
		for(int k=3;k<7;k=k+1){
			BayesLearner learner=new BayesLearner(k);
			try {
				learner.learn();
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LockObtainFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				BayesClassifier cc=new BayesClassifier(k);
				Vector<EvaluateUnit> units=new Vector<EvaluateUnit>();
				int[] CategoryTextCount=new int[Constants.CATEGORYS.length]; 
				for(int i=0;i<Constants.CATEGORYS.length;++i){
					File root=new File(Constants.TRAINSETPATHS[i]+"_test"); 
					File[] files=root.listFiles();
					long before=0,after=0,total=0;
//					int totalFile=0;
					for (File file : files) {
						String filepath=file.getAbsolutePath();
						before=System.currentTimeMillis();
						ArrayList<ClassifyResult> results=cc.classify(filepath);
						after=System.currentTimeMillis();
						Collections.sort(results);
						
						
						EvaluateUnit unit=new EvaluateUnit(filepath, i, results.get(0).getClassification());
						units.add(unit);
//						total+=(after-before);
//						totalFile++;
						CategoryTextCount[i]++;
					}
				}
				Evaluator evaluator=new Evaluator(units, CategoryTextCount);
				System.out.println("Macro:\t"+evaluator.getMacroF1());
				System.out.println("Micro:\t"+evaluator.getMicroF1());
//				System.out.println("Classify time:"+total/totalFile);
//				System.out.println(1.0*i/totalFile);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	public static void relearnFeatures(){
		try {
			CHIAvgFeatureLearner featureLearner=new CHIAvgFeatureLearner();
			System.out.println(featureLearner.getStorePath());
			featureLearner.Learn();
			featureLearner.optimize();
			featureLearner.storeData();
			
			DFFeatureLearner featureLearner1=new DFFeatureLearner();
			System.out.println(featureLearner1.getStorePath());
			featureLearner1.Learn();
			featureLearner1.storeData();
			
			MIAvgFeatureLearner featureLearner2=new MIAvgFeatureLearner();
			System.out.println(featureLearner2.getStorePath());
			featureLearner2.Learn();
			featureLearner2.storeData();
			
			IGFeatureLearner featureLearner3=new IGFeatureLearner();
			System.out.println(featureLearner3.getStorePath());
			featureLearner3.Learn();
			featureLearner3.storeData();
			

			
			
			int a=0;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}	
