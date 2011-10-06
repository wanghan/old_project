/**
 * 
 */
package audr.text.extractor.util;


import java.io.File;

import audr.text.extractor.bf.BFExtractor;
import audr.text.extractor.lf.LFExtractor;
import audr.text.extractor.sf.SFExtractor;

/**
 * @author wanghan
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		TypeAttributes ta=TypeAttributes.getInstance();
//		Object o=ta.GetBFAttributes("doc");
//		System.out.println(1);
		
		
		File dir=new File("D:\\Data\\test");
		
		File Files[]=dir.listFiles();
		
		try {
			new SFExtractor().Extract(Files, "D:/Data/test/111.xml");
			for (File file : Files) {
				new LFExtractor().Extract(file, "D:/Data/test/"+file.getName()+".lf");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
