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
			copyDir("D:\\Data\\ԭʼ����\\health\\doc\\", "\\\\Long\\ԭʼ����\\����\\doc", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\health\\html\\", "\\\\Long\\ԭʼ����\\����\\html", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\health\\ppt\\", "\\\\Long\\ԭʼ����\\����\\ppt", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\health\\xls\\", "\\\\Long\\ԭʼ����\\����\\xls", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\health\\pdf\\", "\\\\Long\\ԭʼ����\\����\\pdf", 500,1);
			
			copyDir("D:\\Data\\ԭʼ����\\consumption\\doc\\", "\\\\Long\\ԭʼ����\\����\\doc", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\consumption\\html\\", "\\\\Long\\ԭʼ����\\����\\html", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\consumption\\ppt\\", "\\\\Long\\ԭʼ����\\����\\ppt", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\consumption\\xls\\", "\\\\Long\\ԭʼ����\\����\\xls", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\consumption\\pdf\\", "\\\\Long\\ԭʼ����\\����\\pdf", 500,1);
			
			copyDir("D:\\Data\\ԭʼ����\\education\\doc\\", "\\\\Long\\ԭʼ����\\����\\doc", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\education\\html\\", "\\\\Long\\ԭʼ����\\����\\html", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\education\\ppt\\", "\\\\Long\\ԭʼ����\\����\\ppt", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\education\\xls\\", "\\\\Long\\ԭʼ����\\����\\xls", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\education\\pdf\\", "\\\\Long\\ԭʼ����\\����\\pdf", 500,1);
			
			
			copyDir("D:\\Data\\ԭʼ����\\government\\doc\\", "\\\\Long\\ԭʼ����\\����\\doc", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\government\\html\\", "\\\\Long\\ԭʼ����\\����\\html", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\government\\ppt\\", "\\\\Long\\ԭʼ����\\����\\ppt", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\government\\xls\\", "\\\\Long\\ԭʼ����\\����\\xls", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\government\\pdf\\", "\\\\Long\\ԭʼ����\\����\\pdf", 500,1);
			
			copyDir("D:\\Data\\ԭʼ����\\military\\doc\\", "\\\\Long\\ԭʼ����\\����\\doc", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\military\\html\\", "\\\\Long\\ԭʼ����\\����\\html", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\military\\ppt\\", "\\\\Long\\ԭʼ����\\����\\ppt", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\military\\xls\\", "\\\\Long\\ԭʼ����\\����\\xls", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\military\\pdf\\", "\\\\Long\\ԭʼ����\\����\\pdf", 500,1);
			
			copyDir("D:\\Data\\ԭʼ����\\science\\doc\\", "\\\\Long\\ԭʼ����\\�Ƽ�\\doc", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\science\\html\\", "\\\\Long\\ԭʼ����\\�Ƽ�\\html", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\science\\ppt\\", "\\\\Long\\ԭʼ����\\�Ƽ�\\ppt", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\science\\xls\\", "\\\\Long\\ԭʼ����\\�Ƽ�\\xls", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\science\\pdf\\", "\\\\Long\\ԭʼ����\\�Ƽ�\\pdf", 500,1);
			
			copyDir("D:\\Data\\ԭʼ����\\sport\\doc\\", "\\\\Long\\ԭʼ����\\����\\doc", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\sport\\html\\", "\\\\Long\\ԭʼ����\\����\\html", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\sport\\ppt\\", "\\\\Long\\ԭʼ����\\����\\ppt", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\sport\\xls\\", "\\\\Long\\ԭʼ����\\����\\xls", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\sport\\pdf\\", "\\\\Long\\ԭʼ����\\����\\pdf", 500,1);
			
			copyDir("D:\\Data\\ԭʼ����\\tour\\doc\\", "\\\\Long\\ԭʼ����\\����\\doc", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\tour\\html\\", "\\\\Long\\ԭʼ����\\����\\html", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\tour\\ppt\\", "\\\\Long\\ԭʼ����\\����\\ppt", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\tour\\xls\\", "\\\\Long\\ԭʼ����\\����\\xls", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\tour\\pdf\\", "\\\\Long\\ԭʼ����\\����\\pdf", 500,1);
			
			copyDir("D:\\Data\\ԭʼ����\\culture\\doc\\", "\\\\Long\\ԭʼ����\\�Ļ�\\doc", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\culture\\html\\", "\\\\Long\\ԭʼ����\\�Ļ�\\html", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\culture\\ppt\\", "\\\\Long\\ԭʼ����\\�Ļ�\\ppt", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\culture\\xls\\", "\\\\Long\\ԭʼ����\\�Ļ�\\xls", 500,1);
			copyDir("D:\\Data\\ԭʼ����\\culture\\pdf\\", "\\\\Long\\ԭʼ����\\�Ļ�\\pdf", 500,1);
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
		// �½��ļ����������������л���
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// �½��ļ���������������л���
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// ��������
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// ˢ�´˻���������
		outBuff.flush();

		// �ر���
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}
}
