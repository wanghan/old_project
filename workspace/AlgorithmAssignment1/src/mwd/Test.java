/**
 * 
 */
package mwd;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 程序运行的主程序
 * @author 王晗 SY0906405
 *
 */
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		DataReader dr=new DataReader("./test1.txt");
		try {
			MinWeightDividing mwd=new MinWeightDividing(dr.getData());
			mwd.getMinWeightDividing();
			mwd.outputMinWeightMatrix("./test1.out1");
			mwd.outputMinEdgeMatrix("test1.out2");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("文件没有找到");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		//参数数目不正确
		if(args.length!=1){
			System.out.println(args.length);
			System.out.println("Usage: MinWeightDividing.exe <inputFileName>");
		}
		else{
			String filename=args[0];
			//生成输出文件的名字
			String filenamePrefix=filename.substring(0, filename.lastIndexOf('.'));
			String foutname1=filenamePrefix+"_weight_matrix.txt";
			String foutname2=filenamePrefix+"_edge_matrix.txt";
			DataReader dr=new DataReader("./test1.txt");
			try {
				MinWeightDividing mwd=new MinWeightDividing(dr.getData());
				Result re=mwd.getMinWeightDividing();
				//输出结果
				System.out.println(re);
				
				mwd.outputMinWeightMatrix(foutname1);
				System.out.println("Output weight matrix data to file "+foutname1);
				mwd.outputMinEdgeMatrix(foutname2);
				System.out.println("Output edge matrix data to file "+foutname2);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("文件没有找到");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
