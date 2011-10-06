/**
 * 
 */
package sys;

import java.io.File;

/**
 * @author wanghan
 *
 */
public class HQ {
	
	/**
	 * 将用户的插入操作信息传给HQ
	 * @param url
	 * @param basicfeature
	 * @param semanticfeature
	 * @return 插入结果信息
	 */
	public static String receiveInsertInfo(String[] url,String[] basicfeature, File semanticfeature,String imageType){
//		String existid[]={"1","2","3"};
		File files[]=new File[url.length];
		for(int i=0;i<url.length;++i){
			files[i]=new File(url[i]);
		}
		
		return "aa";
	}
	
	/**
	 * 将用户的 检索操作信息传给HQ
	 * @param url
	 * @param type
	 * @param basicfeature
	 * @param semanticfeature
	 * @return Lire返回的检索结果
	 */
	public static QueryResult Querybyall(String url,String type,String[] basicfeature,String[] semanticfeature,String imageType){
//		QueryResult result=null;
//		File file=new File(url);
		File f1=new File("./Chocolate Cake.bmp");
		File f2=new File("./Dragon.bmp");
		File f3=new File("./Da Vinci.bmp");
		
		File files[]={f2,f3,f1};
		String paths[]={"./Water lilies.jpg","./Blue hills.jpg","./Sunset.jpg"};
		String lireid[]={"aaa","sss","whwh"};
//		result=new HQTransporter().Querybyexist(basicfeature);
		return new QueryResult(files, lireid, paths,paths,paths);
	}
	public static QueryResult Querybylire(String url, String type,String imageType){
		File f1=new File("./Chocolate Cake.bmp");
		File f2=new File("./Dragon.bmp");
		File f3=new File("./Da Vinci.bmp");
		
		File files[]={f1,f2,f3};
		String paths[]={"./Water lilies.jpg","./Blue hills.jpg","./Sunset.jpg"};
		String lireid[]={"aaa","sss","whwh"};
//		result=new HQTransporter().Querybyexist(basicfeature);
		return new QueryResult(files, lireid, paths,paths,paths);
	}
	public static QueryResult Querybyexist(String[] bf,String[] sf,String imageType){
//		QueryResult result=null;
		File f1=new File("./Chocolate Cake.bmp");
		File f2=new File("./Dragon.bmp");
		File f3=new File("./Da Vinci.bmp");
		
		File files1[]={f1,f2,f3};
		File files2[]={new File("./Water lilies.jpg"),new File("./Sunset.jpg"),new File("./Blue hills.jpg")};
		String lireid[]={"aaa","sss","whwh"};
//		result=new HQTransporter().Querybyexist(basicfeature);
		return new QueryResult(files1,files2,lireid);
	}
	public static QueryResult QueryByLireID(String lireID[]){
//		QueryResult result=null;
		File f1=new File("./Chocolate Cake.bmp");
		File f2=new File("./Dragon.bmp");
		File f3=new File("./Da Vinci.bmp");
		
		File files1[]={f1,f2,f3};
		File files2[]={new File("./Water lilies.jpg"),new File("./Sunset.jpg"),new File("./Blue hills.jpg")};
		String lireid[]={"aaa","sss","whwh"};
//		result=new HQTransporter().Querybyexist(basicfeature);
		return new QueryResult(files1,files2,lireid);
	}
	/**
	 * HQ接收Lire的插入信息
	 * @param existID
	 * @param lireID
	 * @return
	 */
	public static int returnLireID(String[] existID, String[] lireID){
		return 1;
	}
	/**
	 * HQ接收Lire的检索信息
	 * @param URL
	 * @param lireID
	 * @param thumbnailStream
	 * @return
	 */
	public static int returnLireResult(String URL, String[] lireID, File[] thumbnailStream){
		System.out.println("ok");
		return 1;
	}
}
