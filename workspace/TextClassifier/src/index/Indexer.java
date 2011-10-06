package index;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.wltea.analyzer.lucene.IKAnalyzer;

import utils.Constants;

public class Indexer {
	
	
	public Indexer(){
		
	}
	
	public int index(String indexRoot, String fileRoot) throws CorruptIndexException, LockObtainFailedException, IOException{
		
		int docCount=0;
		
		IndexWriter indexWriter=new IndexWriter(FSDirectory.open(new File(indexRoot)),
				new IKAnalyzer(),
				true,
				MaxFieldLength.UNLIMITED);
		
		File root=new File(fileRoot);
		for (File file : root.listFiles()) {
			if(!file.isDirectory()){
				docCount++;
				Document doc=new Document();
				doc.add(new Field(IndexFields.FILENAME, file.getName(), Store.YES, Index.NOT_ANALYZED));
				doc.add(new Field(IndexFields.CONTENTS,new FileReader(file)));
				System.out.println("Adding doc:"+file.getPath());
				indexWriter.addDocument(doc);
			}
		}
		
		indexWriter.optimize();
		indexWriter.close();
		
		return docCount;
	}
	
	public static String getIndexRootForCategory(int category){
		String indexRoot=Constants.INDEX_ROOT;
		String indexDir=indexRoot+Constants.CATEGORYS[category];
		return indexDir;
	}
}
