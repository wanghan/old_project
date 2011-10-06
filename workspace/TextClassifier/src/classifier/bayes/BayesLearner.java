/**
 * 
 */
package classifier.bayes;

import feature.CHIAvgFeatureLearner;
import feature.CHIMaxFeatureLearner;
import feature.DFFeatureLearner;
import feature.FeatureLearnerBase;
import feature.IGFeatureLearner;
import feature.MIAvgFeatureLearner;
import feature.MIMaxFeatureLearner;
import index.IndexFields;
import index.Indexer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.lucene.IKQueryParser;

import utils.Constants;

/**
 * @author wanghan
 *
 */
public class BayesLearner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BayesLearner learner=new BayesLearner();
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
		
		
		
	}
	
	private FeatureLearnerBase features=null;
	private TrainingDataManager trainingData;
	public BayesLearner() {
		// TODO Auto-generated constructor stub
		trainingData=new TrainingDataManager();
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
		
	}
	public BayesLearner(int featureSelector) {
		// TODO Auto-generated constructor stub
		trainingData=new TrainingDataManager();
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
		
	}
	
	public void learn() throws CorruptIndexException, LockObtainFailedException, IOException{
		
		//step 1: index
		
		
//		for(int i=8;i<Constants.CATEGORYS.length;++i){
//			String indexDir=Indexer.getIndexRootForCategory(i);
//			new Indexer().index(indexDir, Constants.TRAINSETPATHS[i]);
//		}
//		
		//step2 : count text number for each category
		
		int totalTextNumber=0;
		for(int i=0;i<Constants.TRAINSETPATHS.length;++i){
			String curPath=Constants.TRAINSETPATHS[i];
			File curDir=new File(curPath);
			File curFiles[]=curDir.listFiles();
			totalTextNumber+=curFiles.length;
			trainingData.setCategoryTextNumber(i, curFiles.length);
		}
		trainingData.setTotalTextNumber(totalTextNumber);
		for(int i=0;i<Constants.TRAINSETPATHS.length;++i){
			String curPath=Constants.TRAINSETPATHS[i];
			File curDir=new File(curPath);
			File curFiles[]=curDir.listFiles();
			trainingData.setCategoryProbability(i, (float)1.0*curFiles.length/totalTextNumber);
		}
		
		//step3: measure ClassConditionalProbability

		for(int i=0;i<Constants.TRAINSETPATHS.length;++i){
			String indexDir=Indexer.getIndexRootForCategory(i);
			IndexSearcher search = new IndexSearcher(FSDirectory.open(new File(indexDir)),true);
			String curPath=Constants.TRAINSETPATHS[i];
			File curDir=new File(curPath);
			File curFiles[]=curDir.listFiles();
			for (File file : curFiles) {
				if(!file.isDirectory()){
//					System.out.println(i+" "+file.getAbsolutePath());
					analyzeFile(file,i,search);
				}
			}
			System.out.println(Constants.TRAINSETPATHS[i]);
		}
		TrainingDataManager.storeData(trainingData);
		System.out.println("Learn Done");
	}
	
	private void analyzeFile(File file,int category,IndexSearcher search) throws IOException{
		
		FileReader reader=new FileReader(file);
		IKSegmentation segmentation=new IKSegmentation(reader);
		while(true){
			Lexeme ll=segmentation.next();
			if(ll!=null){
				if(ll.getLexemeType()==Lexeme.TYPE_CJK_NORMAL){
					String token=ll.getLexemeText();
					if(features.isFeature(token)){
						float pxc=calculatePxc(token, category,search);
						this.trainingData.addTokenProbability(token, pxc, category);
					}
				}
			}
			else{
				break;
			}
		}
	}
	/**
	 * 计算先验概率，并返回
	 * @param x term
	 * @param category
	 * @param search
	 * @return
	 * @throws IOException
	 */
	private float calculatePxc(String x, int category,IndexSearcher search) throws IOException 
	{
		float ret = 0;
		final float M = 0F;
		float Nxc = getCountContainKeyOfClassification(search,category,x);
		float Nc = trainingData.getCategoryProbabilities()[category];
		float V = trainingData.getCategoryTextCount()[category];
		ret = (Nxc + 1) / (Nc + M + V);
		return ret;
	}
	/**
	 * 返回某一类别文档中包含term x 的文档数目
	 * @param searcher
	 * @param category
	 * @param x
	 * @return
	 * @throws IOException
	 */
	private int getCountContainKeyOfClassification(IndexSearcher searcher,int category, String x) throws IOException{
		Query query=IKQueryParser.parse(IndexFields.CONTENTS, x);
		
		
		TreeSet<Term> term=new TreeSet<Term>();
		query.extractTerms(term);
		int docNumber=searcher.docFreq(term.pollFirst());
		return docNumber;
		
	}
}
