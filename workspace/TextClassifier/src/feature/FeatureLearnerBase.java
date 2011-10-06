/**
 * 
 */
package feature;

import index.IndexFields;
import index.Indexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.lucene.IKQueryParser;

import utils.Constants;

/**
 * @author wanghan
 *
 */
public abstract class FeatureLearnerBase implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3831403587815333332L;
	protected Vector<FeatureWeight> features;
	protected HashSet<String> table=new HashSet<String>();
	protected IndexSearcher searchers[]=new IndexSearcher[Constants.CATEGORYS.length];
	protected boolean isCut=true;
	protected HashMap<String, FeatureWeight> selectedFeatures;
	
	public FeatureLearnerBase() throws CorruptIndexException, IOException {
		// TODO Auto-generated constructor stub
		this.features=new Vector<FeatureWeight>();
		for(int i=0;i<Constants.CATEGORYS.length;++i){
			searchers[i]=new IndexSearcher(FSDirectory.open(new File(Indexer.getIndexRootForCategory(i))));
		}
		selectedFeatures=new HashMap<String, FeatureWeight>();
	}
	
	public String getStorePath(){
		String filePath=Constants.DATA_ROOT+this.getClass().getName();
		return filePath;
	}
	
	public void Learn() throws CorruptIndexException, IOException{

		MultiSearcher searcher=new MultiSearcher(searchers);
		for(int i=0;i<Constants.TRAINSETPATHS.length;++i){
			String curPath=Constants.TRAINSETPATHS[i];
			File curDir=new File(curPath);
			File curFiles[]=curDir.listFiles();
			for (File file : curFiles) {
				if(!file.isDirectory()){
					System.out.println(i+" "+file.getAbsolutePath());
					analyzeFile(searcher,file,i);
				}
			}
			System.out.println(Constants.TRAINSETPATHS[i]);
		}
	}
	private void analyzeFile(Searcher searcher,File file,int cateGory) throws IOException{
		
		FileReader reader=new FileReader(file);
		IKSegmentation segmentation=new IKSegmentation(reader);
		while(true){
			Lexeme ll=segmentation.next();
			if(ll!=null){
				if(ll.getLexemeType()==Lexeme.TYPE_CJK_NORMAL){
					String token=ll.getLexemeText().trim();
					boolean flag=table.contains(token);
					
					if(!flag){
						int df=getDF(token, searcher);
						if(isCut){
							
							if(df<Constants.MIN_DF){
								continue;
							}
						}
						table.add(token);
						double weight=getWeight(token, searcher,cateGory);
						FeatureWeight cr=new FeatureWeight(token,weight,df);
						features.add(cr);
						//measure others paras
						hooker(token,searcher,cateGory,df);
					}
					else{
						;
					}
				}
			}
			else{
				break;
			}
		}
	}
	private int getDF(String token,Searcher searcher) throws IOException{
		Query query=IKQueryParser.parse(IndexFields.CONTENTS, token);
		TreeSet<Term> terms=new TreeSet<Term>();
		query.extractTerms(terms);
		int df=searcher.docFreq(terms.pollFirst());
		return df;
	}
	public abstract void hooker(String token, Searcher searcher, int cateGory, int dfOfToken) throws IOException ;
	public abstract double getWeight(String token,Searcher searcher,int cateGory) throws IOException;
	public abstract void optimize();
	
	public void loadData() throws IOException, ClassNotFoundException{
		FileInputStream fos=new FileInputStream(this.getStorePath());
		ObjectInputStream oos=new ObjectInputStream(fos);
		Vector<FeatureWeight> features=(Vector<FeatureWeight>)oos.readObject();
		this.features=features;
	}
	public void storeData() throws IOException{
		
		FileOutputStream fos=new FileOutputStream(this.getStorePath());
		ObjectOutputStream oos=new ObjectOutputStream(fos);

		oos.writeObject(this.features);
		
		oos.flush();
		oos.close();
	}
	protected double log2n(double n){
		if(n==0)
			return 0;
		return Math.log(n)/Math.log(2);
		
	}
	
	public boolean isFeature(String token){
		Object o=selectedFeatures.get(token);
		if(o==null)
			return false;
		else{
			return true;
		}
	}
	
	public FeatureWeight getFeature(String token){
		Object o=selectedFeatures.get(token);
		if(o==null)
			return null;
		else{
			return (FeatureWeight)o;
		}
	}
	
	public boolean isCut() {
		return isCut;
	}

	public void setCut(boolean isCut) {
		this.isCut = isCut;
	}
	
}
