package audr.text.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;

import audr.text.lucene.fields.TextCategoryFields;

/*
 * This file is part of the Caliph and Emir project: http://www.SemanticMetadata.net.
 *
 * Caliph & Emir is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Caliph & Emir is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Caliph & Emir; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Copyright statement:
 * --------------------
 * (c) 2002-2007 by Mathias Lux (mathias@juggle.at)
 * http://www.juggle.at, http://www.SemanticMetadata.net
 */

/**
 * This file is part of the Caliph and Emir project:
 * http://www.SemanticMetadata.net <br>
 * Date: 04.02.2006 <br>
 * Time: 09:44:49
 */
public class FileUtils {
	
	private static final int MAX_LENGTH = 4096;

	/**
	 * 获得图像的扩展名
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
	 * 获得文件名
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
	 * 将文件读入到byte[]中
	 * 
	 * @return：byte[]
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

	public static byte[] HDFSFile2ByteArray(FSDataInputStream image)
			throws IOException {
		image.seek(0);
		// BufferedInputStream in = new BufferedInputStream(image);
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

		// System.out.println("Available bytes:" + in.available());
		byte[] temp = new byte[1024];
		int size = 0;
		while ((size = image.read(temp)) > 0) {
			out.write(temp, 0, size);
		}

		byte[] content = out.toByteArray();
		// System.out.println("Readed bytes count:" + new String(content));
		return content;
	}

	/**
	 * 将byte[]读至文件中
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

	/**
	 * 拷贝本地文件到hadoop
	 * 
	 * @param localFile
	 *            本地文件和路径名
	 * @param hadoopFile
	 *            hadoop文件和路径名
	 * @return
	 */
	public static void uploadFile2HDFS(String localFile, String hadoopFile) {
		try {
			Configuration conf = new Configuration();
			FileSystem src = FileSystem.getLocal(conf);
			FileSystem dst = FileSystem.get(conf);
			Path srcpath = new Path(localFile);
			Path dstpath = new Path(hadoopFile);
			FileUtil.copy(src, srcpath, dst, dstpath, false, conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除HDFS上目录path下所有文件
	 * 
	 * @param path
	 */
	public static void deleteFileFromHDFS(String path) {
		try {
			Configuration conf = new Configuration();
			FileSystem dst = FileSystem.get(conf);
			Path dstpath = new Path(path);
			dst.delete(dstpath, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 检测是否指定的目录存在
	 * 
	 * @param path
	 * @return true 存在 false 不存在
	 */
	public static boolean isDirectoryExist(Path path) {
		int existFlag = 0;
		try {
			Configuration conf = new Configuration();
			FileSystem dst = FileSystem.get(conf);
			if (dst.exists(path))
				existFlag = 1;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (existFlag == 1)
			return true;
		return false;

	}

	public static String readTextFromHDFS(Path filePath) throws IOException {

		// 指定Configuration
		Configuration conf = new Configuration();
		// 定义一个DataInputStream
		FSDataInputStream in = null;
		// 定义byte数据接收从FSDataInputStream中读取的数据
		byte[] line;

		// 得到文件系统的实例
		FileSystem fs = FileSystem.get(conf);
		// 通过FileSystem的open方法打开一个指定的文件
		in = fs.open(filePath);
		line = new byte[MAX_LENGTH];
		in.read(line);

		return new String(line).trim();

	}
	
	public static String genTempDir(String root) {
		return root + System.currentTimeMillis() + "/";
	}
	/**
	 * create index dirs for each text category
	 * @param root
	 * @throws IOException 
	 */
	public static void makeIndexDirs() throws IOException{
		FileSystem fs=FileSystem.get(new Configuration());
		for(int i=0;i<TextCategoryFields.TEXT_CATEGOTIES_ENUM.length;++i){
			String oriDir=Constants.INPUT_PATH.replace("%Category%", TextCategoryFields.TEXT_CATEGOTIES_ENUM[i]);
			String lfDir=Constants.INPUT_PATH_LF.replace("%Category%", TextCategoryFields.TEXT_CATEGOTIES_ENUM[i]);
			FileSystem.mkdirs(fs, new Path(oriDir), FsPermission.getDefault());
			FileSystem.mkdirs(fs, new Path(lfDir), FsPermission.getDefault());
			
			for(int j=0;j<Constants.INDEX_SHARDS.length;++j){
				String indexDir=Constants.INDEX_SHARDS[j].replace("%Category%", TextCategoryFields.TEXT_CATEGOTIES_ENUM[i]);
				FileSystem.mkdirs(fs, new Path(indexDir), FsPermission.getDefault());
			}
		}
	}
}
