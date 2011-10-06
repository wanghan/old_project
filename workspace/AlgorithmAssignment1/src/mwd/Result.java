/**
 * 
 */
package mwd;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 用于保存程序运行结果的类
 * 包含两部分：最小权重大小，最小划分时对应的弦的集合
 * @author 王晗 SY0906405
 *
 */
public class Result {

	private int minWeight;				//最小权重大小
	private ArrayList<String> minEdges;	//最小划分时对应的弦的集合
	/**
	 * 构造函数
	 * @param minWeight 最小权重大小
	 * @param minEdges 最小划分时对应的弦的集合
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
	 * 将程序的运行结果转化为可显示的字符串
	 */
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		sb.append("最小权重和为："+minWeight+"\n");
		sb.append("包含的边为：\n");
		
		//使用迭代器遍历每条边
		Iterator<String> it=minEdges.iterator();
		while(it.hasNext()){
			sb.append(it.next());
			sb.append("\n");
		}
		return sb.toString();
	}
}
