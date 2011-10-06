/**
 * 
 */
package audr.text.tools;

import java.io.File;

import audr.text.tools.utils.FileUtils;

/**
 * @author wanghan
 *
 */
public class FTPDataPicker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FTPDataPicker.pick("\\\\192.168.13.212\\public\\AUDR\\text\\backup 2010.8.31\\originalData\\consumption\\doc", "./temp/", 100);
	}

	public static void pick(String root, String output, int number){
		int count=0;
		File rootDir=new File(root);
		for (File dir : rootDir.listFiles()) {
			for (File file : dir.listFiles()) {
				if(file.isFile()&&file.length()>50000){
					File desFile=new File(output+System.currentTimeMillis()+"_"+count+"."+FileUtils.getExtension(file.getName()));
					try {
						System.out.println(count+" copying file: "+file.getAbsolutePath());
						FileUtils.copyFile(file, desFile);
						count++;
						if(count>=number){
							return ;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
				}
				
			}
		}
	}
	
}
