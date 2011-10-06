package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopyer {
	
	public static void main(String[] args){
		
		try {
			copyDir("D:\\Data\\原始数据\\health\\doc\\", "\\\\Long\\原始数据\\健康\\doc", 500,1);
			copyDir("D:\\Data\\原始数据\\health\\html\\", "\\\\Long\\原始数据\\健康\\html", 500,1);
			copyDir("D:\\Data\\原始数据\\health\\ppt\\", "\\\\Long\\原始数据\\健康\\ppt", 500,1);
			copyDir("D:\\Data\\原始数据\\health\\xls\\", "\\\\Long\\原始数据\\健康\\xls", 500,1);
			copyDir("D:\\Data\\原始数据\\health\\pdf\\", "\\\\Long\\原始数据\\健康\\pdf", 500,1);
			
			copyDir("D:\\Data\\原始数据\\consumption\\doc\\", "\\\\Long\\原始数据\\消费\\doc", 500,1);
			copyDir("D:\\Data\\原始数据\\consumption\\html\\", "\\\\Long\\原始数据\\消费\\html", 500,1);
			copyDir("D:\\Data\\原始数据\\consumption\\ppt\\", "\\\\Long\\原始数据\\消费\\ppt", 500,1);
			copyDir("D:\\Data\\原始数据\\consumption\\xls\\", "\\\\Long\\原始数据\\消费\\xls", 500,1);
			copyDir("D:\\Data\\原始数据\\consumption\\pdf\\", "\\\\Long\\原始数据\\消费\\pdf", 500,1);
			
			copyDir("D:\\Data\\原始数据\\education\\doc\\", "\\\\Long\\原始数据\\教育\\doc", 500,1);
			copyDir("D:\\Data\\原始数据\\education\\html\\", "\\\\Long\\原始数据\\教育\\html", 500,1);
			copyDir("D:\\Data\\原始数据\\education\\ppt\\", "\\\\Long\\原始数据\\教育\\ppt", 500,1);
			copyDir("D:\\Data\\原始数据\\education\\xls\\", "\\\\Long\\原始数据\\教育\\xls", 500,1);
			copyDir("D:\\Data\\原始数据\\education\\pdf\\", "\\\\Long\\原始数据\\教育\\pdf", 500,1);
			
			
			copyDir("D:\\Data\\原始数据\\government\\doc\\", "\\\\Long\\原始数据\\政府\\doc", 500,1);
			copyDir("D:\\Data\\原始数据\\government\\html\\", "\\\\Long\\原始数据\\政府\\html", 500,1);
			copyDir("D:\\Data\\原始数据\\government\\ppt\\", "\\\\Long\\原始数据\\政府\\ppt", 500,1);
			copyDir("D:\\Data\\原始数据\\government\\xls\\", "\\\\Long\\原始数据\\政府\\xls", 500,1);
			copyDir("D:\\Data\\原始数据\\government\\pdf\\", "\\\\Long\\原始数据\\政府\\pdf", 500,1);
			
			copyDir("D:\\Data\\原始数据\\military\\doc\\", "\\\\Long\\原始数据\\军事\\doc", 500,1);
			copyDir("D:\\Data\\原始数据\\military\\html\\", "\\\\Long\\原始数据\\军事\\html", 500,1);
			copyDir("D:\\Data\\原始数据\\military\\ppt\\", "\\\\Long\\原始数据\\军事\\ppt", 500,1);
			copyDir("D:\\Data\\原始数据\\military\\xls\\", "\\\\Long\\原始数据\\军事\\xls", 500,1);
			copyDir("D:\\Data\\原始数据\\military\\pdf\\", "\\\\Long\\原始数据\\军事\\pdf", 500,1);
			
			copyDir("D:\\Data\\原始数据\\science\\doc\\", "\\\\Long\\原始数据\\科技\\doc", 500,1);
			copyDir("D:\\Data\\原始数据\\science\\html\\", "\\\\Long\\原始数据\\科技\\html", 500,1);
			copyDir("D:\\Data\\原始数据\\science\\ppt\\", "\\\\Long\\原始数据\\科技\\ppt", 500,1);
			copyDir("D:\\Data\\原始数据\\science\\xls\\", "\\\\Long\\原始数据\\科技\\xls", 500,1);
			copyDir("D:\\Data\\原始数据\\science\\pdf\\", "\\\\Long\\原始数据\\科技\\pdf", 500,1);
			
			copyDir("D:\\Data\\原始数据\\sport\\doc\\", "\\\\Long\\原始数据\\体育\\doc", 500,1);
			copyDir("D:\\Data\\原始数据\\sport\\html\\", "\\\\Long\\原始数据\\体育\\html", 500,1);
			copyDir("D:\\Data\\原始数据\\sport\\ppt\\", "\\\\Long\\原始数据\\体育\\ppt", 500,1);
			copyDir("D:\\Data\\原始数据\\sport\\xls\\", "\\\\Long\\原始数据\\体育\\xls", 500,1);
			copyDir("D:\\Data\\原始数据\\sport\\pdf\\", "\\\\Long\\原始数据\\体育\\pdf", 500,1);
			
			copyDir("D:\\Data\\原始数据\\tour\\doc\\", "\\\\Long\\原始数据\\旅游\\doc", 500,1);
			copyDir("D:\\Data\\原始数据\\tour\\html\\", "\\\\Long\\原始数据\\旅游\\html", 500,1);
			copyDir("D:\\Data\\原始数据\\tour\\ppt\\", "\\\\Long\\原始数据\\旅游\\ppt", 500,1);
			copyDir("D:\\Data\\原始数据\\tour\\xls\\", "\\\\Long\\原始数据\\旅游\\xls", 500,1);
			copyDir("D:\\Data\\原始数据\\tour\\pdf\\", "\\\\Long\\原始数据\\旅游\\pdf", 500,1);
			
			copyDir("D:\\Data\\原始数据\\culture\\doc\\", "\\\\Long\\原始数据\\文化\\doc", 500,1);
			copyDir("D:\\Data\\原始数据\\culture\\html\\", "\\\\Long\\原始数据\\文化\\html", 500,1);
			copyDir("D:\\Data\\原始数据\\culture\\ppt\\", "\\\\Long\\原始数据\\文化\\ppt", 500,1);
			copyDir("D:\\Data\\原始数据\\culture\\xls\\", "\\\\Long\\原始数据\\文化\\xls", 500,1);
			copyDir("D:\\Data\\原始数据\\culture\\pdf\\", "\\\\Long\\原始数据\\文化\\pdf", 500,1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void copyDir(String des, String src, int number,int step) throws IOException {
		File file_src = new File(src);
		File files_src[] = file_src.listFiles();
		
		int num=0;
		int total=0;
		for (File file : files_src) {
			if (file.isDirectory()) {
				for (File file1 : file.listFiles()) {
					
					if(num%step==0){
						String target_path=des+System.currentTimeMillis()+getSuffix(file1.getName());
						System.out.println(num+" Copying "+file1.getCanonicalPath());
						copyFile(file1, new File(target_path));
						total++;
						if(total==number)
							return;
					}
					num++;
				}
				
			}
		
		}
	}
	public static String getSuffix(String filename){
		int index=filename.lastIndexOf('.');
		return filename.substring(index);
	}
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}
}
