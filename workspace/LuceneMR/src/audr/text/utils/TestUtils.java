package audr.text.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import audr.text.lucene.distributed.indexer.Indexer;
import audr.text.lucene.fields.TextCategoryFields;

/**
 * @author wanghan
 * 
 */
public class TestUtils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		InitialIndex();
		
	}

	public static void randomNames(File file) {

		if (file.isDirectory()) {
			File[] subFiles = file.listFiles();
			for (File file2 : subFiles) {
				randomNames(file2);
			}
		} else {
			String oldName = file.getAbsolutePath();

			String newName = oldName.substring(0, oldName.lastIndexOf('\\'))
					+ System.currentTimeMillis() + ".txt";
			file.renameTo(new File(newName));
		}
	}
	/**
	 * copy a number of files 
	 * @param srcDir source dir
	 * @param desDir
	 * @param number
	 */
	public static void copyFiles(String srcDir, String desDir, int number) {
		File in = new File(srcDir);

		File[] subFiles = in.listFiles();

		for (int i = 0; i < number; ++i) {
			String path = desDir + subFiles[i].getName();
			copyfile(subFiles[i], new File(path));
		}
	}
	/**
	 * copy srFile to dtFile
	 * @param srFile
	 * @param dtFile
	 */
	private static void copyfile(File srFile, File dtFile) {
		try {
			File f1 = srFile;
			File f2 = dtFile;
			InputStream in = new FileInputStream(f1);

			// For Append the file.
			// OutputStream out = new FileOutputStream(f2,true);

			// For Overwrite the file.
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println("File copied.");
		} catch (FileNotFoundException ex) {
			System.out
					.println(ex.getMessage() + " in the specified directory.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static String GBKtoUTF8(String s)
			throws UnsupportedEncodingException {

		return new String(s.getBytes("gbk"), "utf-8");
	}
	
	public static void InitialIndex(){
		File dir=new File("D:\\Data\\1");
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
//			for(int i=0;i<TextCategoryFields.TEXT_CATEGOTIES_ENUM.length;++i){
//				new Indexer().insertAll(id, lfFilePath, originalFilePath,TextCategoryFields.TEXT_CATEGOTIES_ENUM[i]);
//			}
			new Indexer().insertAll(id, lfFilePath, originalFilePath,TextCategoryFields.FINANCE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
