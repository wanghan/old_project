/**
 * 
 */
package mwd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * �������ݶ�ȡ����
 * ���ƶ���ʽ�����ݴ��ļ������ڴ�
 * @author ���� SY0906405
 *
 */
public class DataReader {

	private String filename; //�����������ݵ��ļ�������
	/**
	 * ���캯��
	 * @param filename �����������ݵ��ļ�������
	 */
	public DataReader(String filename) {
		// TODO Auto-generated constructor stub
		this.filename=filename;
	}
	
	/**
	 * ���ļ���ȡ����
	 * @return ���ݣ���ͼ���ڽӾ���
	 * @throws FileNotFoundException �ļ���û�ҵ������׳�FileNotFoundException�쳣
	 */
	public int [][] getData() throws FileNotFoundException{
		//���ļ�
		File fin=new File(filename);
		Scanner reader=new Scanner(fin);
		
		//���ȶ�ȡ�ڵ���Ŀ
		int n=reader.nextInt();
		
		//data ���ڱ����ȡ��ͼ���ڽӾ���
		int data[][]=new int[n][n];
		
		//����ѭ�� ��ȡ����
		for(int i=0;i<n;++i){
			for(int j=i;j<n;++j){
				data[i][j]=reader.nextInt();
				//�ڽӾ���ʽ�Գ���
				data[j][i]=data[i][j];
			}
		}
		return data;
	}
	
}
