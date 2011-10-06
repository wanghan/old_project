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
 * ���ļ���ȡ���ݵ���
 * ���ݵ�������һ��N*N�ľ���
 * @author SY0906405 ����
 *
 */
public class DataReader {


	private String filename; //�������ݵ��ļ���
	/**
	 * ���캯��
	 * @param finName �������ݵ��ļ���
	 */
	public DataReader(String finName) {
		// TODO Auto-generated constructor stub
		this.filename=finName;
	}
	/**
	 * ��ȡ����
	 * @return N*N�ľ���
	 * @throws FileNotFoundException �ļ�δ�ҵ����׳��쳣
	 */
	public int[][] getData() throws FileNotFoundException{
		Scanner reader=new Scanner(new File(filename));
		//�ļ���û�����ݣ�����null
		if(!reader.hasNext()){
			reader.close();
			return null;
		}
		else{
			//���ȶ�ȡһ�У���ȷ��N�Ĵ�С
			String line=reader.nextLine();
			StringTokenizer st=new StringTokenizer(line);
			ArrayList<Integer> numbers=new ArrayList<Integer>();
			while(st.hasMoreTokens()){
				numbers.add(Integer.parseInt(st.nextToken()));
			}
			//size��ΪN����ʾ����Ľ���
			int size=numbers.size();
			int [][]data=new int[size][size];
			for(int i=0;i<size;++i){
				data[0][i]=numbers.get(i);
			}
			
			//��ȡ���������
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
