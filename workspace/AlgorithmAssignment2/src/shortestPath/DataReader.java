/**
 * 
 */
package shortestPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * 从文件读取数据的类
 * 数据的类型是一个N*N的矩阵
 * @author SY0906405 王晗
 *
 */
public class DataReader {


	private String filename; //保存数据的文件名
	/**
	 * 构造函数
	 * @param finName 保存数据的文件名
	 */
	public DataReader(String finName) {
		// TODO Auto-generated constructor stub
		this.filename=finName;
	}
	/**
	 * 读取数据
	 * @return N*N的矩阵
	 * @throws FileNotFoundException 文件未找到，抛出异常
	 */
	public int[][] getData() throws FileNotFoundException{
		Scanner reader=new Scanner(new File(filename));
		//文件内没有内容，返回null
		if(!reader.hasNext()){
			reader.close();
			return null;
		}
		else{
			//首先读取一行，以确定N的大小
			String line=reader.nextLine();
			StringTokenizer st=new StringTokenizer(line);
			ArrayList<Integer> numbers=new ArrayList<Integer>();
			while(st.hasMoreTokens()){
				numbers.add(Integer.parseInt(st.nextToken()));
			}
			//size即为N，表示矩阵的阶数
			int size=numbers.size();
			int [][]data=new int[size][size];
			for(int i=0;i<size;++i){
				data[0][i]=numbers.get(i);
			}
			
			//读取其余的数据
			for(int i=1;i<size;++i){
				for(int j=0;j<size;++j){
					data[i][j]=reader.nextInt();
				}
			}
			reader.close();
			return data;
		}
		
		
	}
}
