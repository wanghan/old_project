/**
 * 
 */
package sys;

import java.util.HashMap;
import java.util.Map;

import audr.text.lucene.distributed.searcher.TextQueryResult;

/**
 * @author wanghan
 *
 */
public class HQ {
	

	 public static String receiveInsertInfo(String []url, String []basicfeature, String semanticfeature, String [] ifPath,String type){
		 return "OK"; 
		 
	 }
	 
	 public static TextQueryResult[] Querybyall(String keywords, String[] bf, String sf[]){
		 TextQueryResult[] result=new TextQueryResult[2];
		 result[0]=new TextQueryResult();
		 result[1]=new TextQueryResult();
		 result[0].id="0";
		 result[1].id="1";
		 result[0].originalFileName="2010031817075212689032721401.xml";
		 result[1].originalFileName="2010031817075212689032721872.xml";
		 result[0].originalFilePath="D:\\Data\\20\\2010031817075212689032721401.xml";
		 result[1].originalFilePath="D:\\Data\\20\\2010031817075212689032721872.xml";
		 result[0].score=100;
		 result[1].score=50;
		 result[0].snippet="aa";
		 result[1].snippet="bb";
		 return result;
	 }
	 
	 public static String queryBF(String input){
		 return "1";
	 }
	 public static String querySF(String input){
		 return "2";
	 }
	 public static String[] querySFs(String input[]){
		 return input;
	 }
	 public static String[] queryBFs(String input[]){
		 return input;
	 }
	 
	 public static Map<String,String> getFeature(String tableName,String LireID){
		 Map<String, String> result=new HashMap<String, String>();
		 for(int i=0;i<10;++i){
			 result.put(String.valueOf(i), String.valueOf(i+100));
		 }
		 return result;
	 }
}
