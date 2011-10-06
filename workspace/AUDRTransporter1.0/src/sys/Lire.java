/**
 * 
 */
package sys;

import java.io.File;


/**
 * @author wanghan
 *
 */
public class Lire {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
	}
	public static String insertImage(File[] files,String []existID,String imageType){

//		String lireid[]={"aaa","sss","whwh"};
//		String existid[]={"1","2","3"};
		return "aaa";
	}
	public static QueryResult searchFromLire(File file, String type,String imageType){
		File f1=new File("./Chocolate Cake.bmp");
		File f2=new File("./Water lilies.jpg");
		File f3=new File("./Da Vinci.bmp");
		
		File files[]={f1,f2,f3};
		
		String lireid[]={"aaa","sss","whwh"};
		
//		new LireTransporter().returnLireResult(URL, lireid, files);
		return new QueryResult(files, lireid);
	}
	
	public static QueryResult QuerybyLireID(String [] lireID,String[] tableName){
		File f1=new File("./Chocolate Cake.bmp");
		File f2=new File("./Water lilies.jpg");
		File f3=new File("./Da Vinci.bmp");
		
		File files[]={f1,f2,f3};
		
		String lireid[]={"aaa","sss","whwh"};
		
//		new LireTransporter().returnLireResult(URL, lireid, files);
		return new QueryResult(files, lireid);
	}
}
