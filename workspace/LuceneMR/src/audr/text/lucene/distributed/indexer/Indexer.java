
package audr.text.lucene.distributed.indexer;

import java.io.File;

import audr.text.lucene.fields.TextCategoryFields;

/**
 * @author wanghan
 *
 */
public class Indexer {

	/**
	 * for debug
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File dir=new File("D:\\Data\\50");
		File files[]=dir.listFiles();
		
		String[] id=new String[files.length];
		String[] lfFilePath=new String[files.length];
		String[] originalFilePath=new String[files.length];
		for(int i=0;i<files.length;++i){
			lfFilePath[i]=files[i].getAbsolutePath();
			originalFilePath[i]=files[i].getAbsolutePath();
			id[i]=files[i].getName();
		}
		try {
			new Indexer().insertAll(id, lfFilePath, originalFilePath,TextCategoryFields.CONSUMPTION);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String insertAll(String[] id, String[] lfFilePath,String[] originalFilePath, String category) {
		try {
			System.out.println(category);
			return new IndexMRDriver().StartJob(id, lfFilePath, originalFilePath,category);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "Failed";
		}
	}
}
