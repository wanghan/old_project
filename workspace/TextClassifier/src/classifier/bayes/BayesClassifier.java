package classifier.bayes;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import org.apache.lucene.index.CorruptIndexException;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import evaluate.EvaluateUnit;
import evaluate.Evaluator;
import feature.CHIAvgFeatureLearner;
import feature.CHIMaxFeatureLearner;
import feature.DFFeatureLearner;
import feature.FeatureLearnerBase;
import feature.IGFeatureLearner;
import feature.MIAvgFeatureLearner;
import feature.MIMaxFeatureLearner;

import utils.Constants;

/**
 * @author wanghan
 *
 */
public class BayesClassifier {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BayesClassifier cc=new BayesClassifier();
			Vector<EvaluateUnit> units=new Vector<EvaluateUnit>();
			int[] CategoryTextCount=new int[Constants.CATEGORYS.length]; 
			for(int i=0;i<Constants.CATEGORYS.length;++i){
				File root=new File(Constants.TRAINSETPATHS[i]+"_test"); 
				File[] files=root.listFiles();
				long before=0,after=0,total=0;
//				int totalFile=0;
				for (File file : files) {
					String filepath=file.getAbsolutePath();
					before=System.currentTimeMillis();
					ArrayList<ClassifyResult> results=cc.classify(filepath);
					after=System.currentTimeMillis();
					Collections.sort(results);
					
					
					EvaluateUnit unit=new EvaluateUnit(filepath, i, results.get(0).getClassification());
					units.add(unit);
//					total+=(after-before);
//					totalFile++;
					CategoryTextCount[i]++;
				}
			}
			Evaluator evaluator=new Evaluator(units, CategoryTextCount);
			System.out.println("Macro:\t"+evaluator.getMacroF1());
			System.out.println("Micro:\t"+evaluator.getMicroF1());
//			System.out.println("Classify time:"+total/totalFile);
//			System.out.println(1.0*i/totalFile);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	private FeatureLearnerBase features=null;
	private TrainingDataManager trainData;
	public BayesClassifier() throws IOException, ClassNotFoundException {
		// TODO Auto-generated constructor stub
		long before=System.currentTimeMillis();
		trainData=TrainingDataManager.loadData();
		long after=System.currentTimeMillis();
		try {
			features=new IGFeatureLearner();
			features.loadData();
			features.optimize();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("Prepare time:"+(after-before));
	}
	
	public BayesClassifier(int featureSelector) throws IOException, ClassNotFoundException {
		// TODO Auto-generated constructor stub
		long before=System.currentTimeMillis();
		trainData=TrainingDataManager.loadData();
		long after=System.currentTimeMillis();
		try {
			if(featureSelector==Constants.FEATURE_SELECTOR_DF){
				features=new DFFeatureLearner();
			}
			else if(featureSelector==Constants.FEATURE_SELECTOR_IG){
				features=new IGFeatureLearner();
			}
			else if(featureSelector==Constants.FEATURE_SELECTOR_MI_AVG){
				features=new MIAvgFeatureLearner();
			}
			else if(featureSelector==Constants.FEATURE_SELECTOR_CHI_AVG){
				features=new CHIAvgFeatureLearner();
			}
			else if(featureSelector==Constants.FEATURE_SELECTOR_CHI_MAX){
				features=new CHIMaxFeatureLearner();
			}
			else if(featureSelector==Constants.FEATURE_SELECTOR_MI_MAX){
				features=new MIMaxFeatureLearner();
			}
			System.out.println("Learner feature:"+features.getClass().getName());
			features.loadData();
			features.optimize();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("Prepare time:"+(after-before));
	}
	
	public ArrayList<ClassifyResult> classify(String filePath) throws IOException{
		
		ArrayList<ClassifyResult> results=new ArrayList<ClassifyResult>();
		for(int i=0;i<Constants.CATEGORYS.length;++i){
			results.add(new ClassifyResult());
			results.get(i).setClassification(i);
			results.get(i).setProbility(trainData.getCategoryProbabilities()[i]);
		}
		File file=new File(filePath);
		FileReader reader=new FileReader(file);
		IKSegmentation segmentation=new IKSegmentation(reader);
		while(true){
			Lexeme ll=segmentation.next();
			if(ll!=null){
				if(ll.getLexemeType()==Lexeme.TYPE_CJK_NORMAL){
					String token=ll.getLexemeText();
					if(features.isFeature(token)){
						
						for(int i=0;i<Constants.CATEGORYS.length;++i){
							float p=trainData.getTokenProbability(token, i);
							double newP=results.get(i).getProbility()+p;
				//				System.out.println(results.get(i).getProbility());
							results.get(i).setProbility(newP);
						}
						
					}
				}
			}
			else{
				break;
			}
		}
		return results;
	}
	public TrainingDataManager getTrainData() {
		return trainData;
	}
	
}
