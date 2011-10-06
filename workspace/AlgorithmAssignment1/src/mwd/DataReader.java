/**
 * 
 */
package mwd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 输入数据读取的类
 * 将制定格式的数据从文件读入内存
 * @author 王晗 SY0906405
 *
 */
public class DataReader {

	private String filename; //保存输入数据的文件的名称
	/**
	 * 构造函数
	 * @param filename 保存输入数据的文件的名称
	 */
	public DataReader(String filename) {
		// TODO Auto-generated constructor stub
		this.filename=filename;
	}
	
	/**
	 * 从文件读取数据
	 * @return 数据，即图的邻接矩阵
	 * @throws FileNotFoundException 文件若没找到，则抛出FileNotFoundException异常
	 */
	public int [][] getData() throws FileNotFoundException{
		//打开文件
		File fin=new File(filename);
		Scanner reader=new Scanner(fin);
		
		//首先读取节点数目
		int n=reader.nextInt();
		
		//data 用于保存读取的图的邻接矩阵
		int data[][]=new int[n][n];
		
		//二重循环 读取数据
		for(int i=0;i<n;++i){
			for(int j=i;j<n;++j){
				data[i][j]=reader.nextInt();
				//邻接矩阵式对称阵
				data[j][i]=data[i][j];
			}
		}
		return data;
	}
	
}
