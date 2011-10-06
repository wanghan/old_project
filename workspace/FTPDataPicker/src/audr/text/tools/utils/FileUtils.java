package audr.text.tools.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileUtils {

	/**
	 * get file extention name
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtension(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');
			if ((i > -1) && (i < (filename.length() - 1))) {
				return filename.substring(i + 1);
			}
		}
		return null;
	}

	/**
	 * get file name
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');
			if ((i > -1) && (i < (filename.length() - 1))) {
				return filename.substring(0, i);
			}
		}
		return null;
	}

	/**
	 * read a file to byte []
	 * 
	 * @return£ºbyte[]
	 * @throws IOException
	 */
	public static byte[] File2ByteArray(File image) throws IOException {
		FileInputStream fis = new FileInputStream(image);
		BufferedInputStream in = new BufferedInputStream(fis);
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

		// System.out.println("Available bytes:" + in.available());

		byte[] temp = new byte[1024];
		int size = 0;
		while ((size = in.read(temp)) != -1) {
			out.write(temp, 0, size);
		}
		in.close();

		byte[] content = out.toByteArray();
		// System.out.println("Readed bytes count:" + new String(content));
		return content;
	}

	/**
	 * read byte[] into a file
	 * 
	 * @param temp
	 */
	public static File ByteArray2File(String fileName, byte[] temp) {
		File path = new File("./temp");
		if (!path.exists())
			path.mkdir();

		File file = new File("./temp/" + fileName);
		if (file.exists())
			file.delete();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, true);
			fos.write(temp);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException iex) {
				iex.printStackTrace();
			}
		}
		return file;
	}


	public static void copyFile(File resFile, File desFile) throws Exception {
		int length = 2097152;
		FileInputStream in = new FileInputStream(resFile);
		if(!desFile.getParentFile().exists()){
			desFile.getParentFile().mkdirs();
		}
		FileOutputStream out = new FileOutputStream(desFile);
		byte[] buffer = new byte[length];
		while (true) {
			int ins = in.read(buffer);
			if (ins == -1) {
				in.close();
				
				out.close();
				break;
			} 
			else{
				out.write(buffer, 0, ins);
				out.flush();
			}
		}
	}
}
