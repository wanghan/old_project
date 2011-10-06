/**
 * 
 */
package classifier.svm;

import evaluate.EvaluateUnit;
import evaluate.Evaluator;
import feature.CHIAvgFeatureLearner;
import feature.CHIMaxFeatureLearner;
import feature.DFFeatureLearner;
import feature.FeatureLearnerBase;
import feature.FeatureWeight;
import feature.IGFeatureLearner;
import feature.MIAvgFeatureLearner;
import feature.MIMaxFeatureLearner;
import index.Indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import utils.Constants;

/**
 * @author wanghan
 *
 */
public class SVMLearner {

	/**
	 * @param args
	 */
	private String MODEL_PATH="model";
	private String OUTPUT_PATH="result.txt";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Filter:" +Constants.FEATURE_FILTER);
		for(int k=3;k<7;k=k+1){
			try {
				SVMLearner learner=new SVMLearner(k);
				learner.learn();
				Vector<EvaluateUnit> units=learner.classify();
				int[] CategoryTextCount=new int[Constants.CATEGORYS.length]; 
				for(int i=0;i<Constants.CATEGORYS.length;++i){
					File root=new File(Constants.TESTSETPATHS[i]); 
					File[] files=root.listFiles();
					CategoryTextCount[i]=files.length;
				}
				Evaluator evaluator=new Evaluator(units, CategoryTextCount);
				System.out.println("Macro:\t"+evaluator.getMacroF1());
				System.out.println("Micro:\t"+evaluator.getMicroF1());
//				System.out.println("Classify time:"+total/totalFile);
//				System.out.println(1.0*i/totalFile);
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
	}
	
	private FeatureLearnerBase features=null;
	public SVMLearner() {
		// TODO Auto-generated constructor stub
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
	public SVMLearner(int featureSelector) {
		// TODO Auto-generated constructor stub
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
		
		
//		for(int i=0;i<Constants.CATEGORYS.length;++i){
//			String indexDir=Indexer.getIndexRootForCategory(i);
//			new Indexer().index(indexDir, Constants.TRAINSETPATHS[i]);
//		}
		FileWriter writer=new FileWriter(new File(getTrainPath()));
		for(int i=0;i<Constants.TRAINSETPATHS.length;++i){
			
			String indexDir=Indexer.getIndexRootForCategory(i);
			IndexSearcher search = new IndexSearcher(FSDirectory.open(new File(indexDir)),true);
			String curPath=Constants.TRAINSETPATHS[i];
			File curDir=new File(curPath);
			File curFiles[]=curDir.listFiles();
			for (File file : curFiles) {
				if(!file.isDirectory()){
//					System.out.println(i+" "+file.getAbsolutePath());
					ArrayList<FeatureWeight> ff=analyzeFile(file,i,search);
					
					if(ff.size()>0){
						writeAInstace(writer, i, ff);
					}
				}
			}
//			System.out.println(Constants.TRAINSETPATHS[i]);
		}
		writer.close();
		
		System.out.println("Run SVM learn");
		runSVMLearnExe(getTrainPath());
		System.out.println("Learn Done");
	}
	private void writeAInstace(FileWriter writer, int index,
			ArrayList<FeatureWeight> ff) throws IOException {
		writer.write(String.valueOf(index+1));
		Collections.sort(ff,new Comparator<FeatureWeight>(){
			@Override
			public int compare(FeatureWeight o1, FeatureWeight o2) {
				// TODO Auto-generated method stub
				return o1.getIndex()-o2.getIndex();
			}
		});
		for (FeatureWeight featureWeight : ff) {
			writer.write(" "+featureWeight.getIndex()+":"+getTFIDF(featureWeight));
		}
		writer.write("\r\n");
		writer.flush();
	}
	public Vector<EvaluateUnit> classify() throws CorruptIndexException, LockObtainFailedException, IOException{
		
		//step 1: index
		
		
//		for(int i=0;i<Constants.CATEGORYS.length;++i){
//			String indexDir=Indexer.getIndexRootForCategory(i);
//			new Indexer().index(indexDir, Constants.TRAINSETPATHS[i]);
//		}
		
		HashMap<Integer, Integer> IndexClassMap=new HashMap<Integer, Integer>();
		int index=0;
		FileWriter writer=new FileWriter(new File(getTestPath()));
		for(int i=0;i<Constants.TESTSETPATHS.length;++i){
			
			String indexDir=Indexer.getIndexRootForCategory(i);
			IndexSearcher search = new IndexSearcher(FSDirectory.open(new File(indexDir)),true);
			String curPath=Constants.TESTSETPATHS[i];
			File curDir=new File(curPath);
			File curFiles[]=curDir.listFiles();
			for (File file : curFiles) {
				if(!file.isDirectory()){
//					System.out.println(i+" "+file.getAbsolutePath());
					ArrayList<FeatureWeight> ff=analyzeFile(file,i,search);
					
					if(ff.size()>0){
						writeAInstace(writer, i, ff);
						IndexClassMap.put(index++, i);
					}
				}
			}
//			System.out.println(Constants.TRAINSETPATHS[i]);
		}
		writer.close();
//		System.out.println("classify Done");
		runSVMClassifyExe(getTestPath());
		int lineNum=0;
		Vector<EvaluateUnit> result=new Vector<EvaluateUnit>();
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(new File(OUTPUT_PATH))));
		while(true){
			String line=reader.readLine();
			if(line==null)
				break;
			StringTokenizer st=new StringTokenizer(line);
			int classresult=Integer.parseInt(st.nextToken())-1;
			EvaluateUnit unit=new EvaluateUnit(null,IndexClassMap.get(lineNum),classresult);
			lineNum++;
			result.add(unit);
		}
		reader.close();
		return result;
	}
	private ArrayList<FeatureWeight> analyzeFile(File file,int category,IndexSearcher search) throws IOException{
		ArrayList<FeatureWeight> result=new ArrayList<FeatureWeight>();
		FileReader reader=new FileReader(file);
		IKSegmentation segmentation=new IKSegmentation(reader);
		while(true){
			Lexeme ll=segmentation.next();
			if(ll!=null){
				if(ll.getLexemeType()==Lexeme.TYPE_CJK_NORMAL){
					String token=ll.getLexemeText();
					FeatureWeight feature=features.getFeature(token);
					if(feature!=null){
						if(result.contains(feature)){
							feature.setTf(feature.getTf()+1);
						}
						else{
							feature.setTf(1);
							result.add(feature);
						}
					}
				}
			}
			else{
				break;
			}
		}
		return result;
	}
	private double getTFIDF(FeatureWeight feature){
		return 1.0*feature.getTf()*Math.log((feature.getDf()+1));
	}
	public void runSVMClassifyExe(String infilename){
		Runtime rn=Runtime.getRuntime();
		Process p = null;   
		try {   
			p = rn.exec("svm_multiclass_classify.exe "+infilename+" "+MODEL_PATH+features.getClass().getName()+" "+OUTPUT_PATH);

			p.waitFor();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error my exec ");   
		}  
	}
	public void runSVMLearnExe(String infilename){
		Runtime rn=Runtime.getRuntime(); 
		try {   
			rn.exec("svm_multiclass_learn.exe -c 100 "+infilename+" "+MODEL_PATH+features.getClass().getName());
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error my exec ");   
		}  
	}
	private String getTrainPath(){
		return "data/"+features.getClass().getName()+"_train_set.txt";
	}
	private String getTestPath(){
		return "data/"+features.getClass().getName()+"_test_set.txt";
	}
}
