/**
 * 
 */
package mwd;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * ���ڱ���������н������
 * ���������֣���СȨ�ش�С����С����ʱ��Ӧ���ҵļ���
 * @author ���� SY0906405
 *
 */
public class Result {

	private int minWeight;				//��СȨ�ش�С
	private ArrayList<String> minEdges;	//��С����ʱ��Ӧ���ҵļ���
	/**
	 * ���캯��
	 * @param minWeight ��СȨ�ش�С
	 * @param minEdges ��С����ʱ��Ӧ���ҵļ���
	 */
	public Result(int minWeight,ArrayList<String> minEdges) {
		// TODO Auto-generated constructor stub
		this.minEdges=minEdges;
		this.minWeight=minWeight;
	}
	
	//getter and setter
	public int getMinWeight() {
		return minWeight;
	}
	public ArrayList<String> getMinEdges() {
		return minEdges;
	}
	
	/**
	 * ����������н��ת��Ϊ����ʾ���ַ���
	 */
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		sb.append("��СȨ�غ�Ϊ��"+minWeight+"\n");
		sb.append("�����ı�Ϊ��\n");
		
		//ʹ�õ���������ÿ����
		Iterator<String> it=minEdges.iterator();
		while(it.hasNext()){
			sb.append(it.next());
			sb.append("\n");
		}
		return sb.toString();
	}
}
