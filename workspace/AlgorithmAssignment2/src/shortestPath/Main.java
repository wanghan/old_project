/**
 * 
 */
package shortestPath;

import java.io.FileNotFoundException;

/**
 * 程序运行的入口函数
 * @author SY0906405 王晗
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ShortestPath sp=new ShortestPath("./m1.txt", "./m2.txt", 1500);
			sp.getShortestPath();
			
			//输出结果
			System.out.println("Best Solution:");
			StringBuffer sb=new StringBuffer();
			sb.append("0->");
			for(int i=sp.getBastSolution().size()-1;i>0;--i){
				sb.append(sp.getBastSolution().get(i).getIndex());
				sb.append("->");
			}
			sb.append(sp.getBastSolution().get(0).getIndex());
			System.out.println(sb.toString());
			
			System.out.println("Shortest Length:"+sp.getShortestLength());
			System.out.println("Cost:"+sp.getBastSolution().get(0).getCurCost());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
