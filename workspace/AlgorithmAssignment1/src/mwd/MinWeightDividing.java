package mwd;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 凸多边形最小权重三角形划分算法实现的类
 * 算法采用动态规划的思想
 * @author 王晗 SY0906405
 *
 */
public class MinWeightDividing {

	
	private int data[][]; //输入数据，即图的邻接矩阵 data[i][j]表示弦vivj上的权重大小
	
	private int vertexNum;
	//状态变量minWeight[i][j]表示凸多边形{vi,vi+1,...,vj}的最小权重三角形划分对应的权重值
	private int minWeight[][]=null;
	//变量minEdge[i][j]表示最小划分时边vjvi所在三角形的第三个顶点的下标
	private int minEdge[][]=null;
	
	/**
	 * 构造函数
	 * @param data  输入数据，即图的邻接矩阵 data[i][j]表示弦vivj上的权重大小
	 */ 
	public MinWeightDividing(int data[][]) {
		// TODO Auto-generated constructor stub
		this.data=data;
		if(data!=null){
			vertexNum=data.length;
			minWeight=new int [vertexNum][vertexNum];
			minEdge=new int[vertexNum][vertexNum];
		}
	
	}
	/**
	 * 状态变量minWeight[i][j]表示凸多边形{vi,vi+1,...,vj}的最小权重三角形划分对应的权重值
	 * 递推关系式:
	 * minWeight[i][j]=min(minWeight[i][k]+minWeight[k][i]+weight(i,k,j))
	 * weight(i,k,j)代表顶点i,j,k组成的三角形的三边的权重和
	 * @return Result 程序运行结果的实例
	 */
	public Result getMinWeightDividing(){
		if(data==null){
			return null;
		}
		if(vertexNum==0){
			return null;
		}
		//初始化
		for(int i=0;i<vertexNum;++i){
			for(int j=0;j<vertexNum;++j){
				minEdge[i][j]=-1;
			}
		}
		//凸多边形退化成一个点，minWeight[i][i]=0;
		for(int i=0;i<vertexNum;++i){
			minWeight[i][i]=0;
		}
		//凸多边形退化成一条边，minWeight[i][i]=0;
		for(int i=0;i<vertexNum-1;++i){
			minWeight[i][i+1]=0;
		}
		//一下情况凸多边形{vi,vi+1,...,vj}至少包括三个顶点
		//l表示凸多边形顶点数目（不算顶点i），其从2开始，说明凸多边形{vi,vi+1,...,vj}至少包括三个顶点
		for(int l=2;l<vertexNum;++l){
			for(int i=0;i<vertexNum-l;++i){
				int j=i+l;
				//先将minWeight[i][j]赋值成最大值
				minWeight[i][j]=Integer.MAX_VALUE;
				
				//k遍历顶点i+1到j-1，找到此阶段的最优化分
				for(int k=i+1;k<j;++k){
					int temp=0;
					temp=minWeight[i][k]+minWeight[k][j]+data[i][k]+data[k][j]+data[j][i];
					//如果找到更好的三角形划分
					//更新minWeight和三角形的第三个顶点
					if(temp<minWeight[i][j]){
						minWeight[i][j]=temp;
						minEdge[i][j]=k;
					}
					
				}
			}
			
		}
		
		//edges变量保存最优划分是的弦的集合(不包括边)
		ArrayList<String> edges=new ArrayList<String>();
		
		//反向遍历minEdge，得到最小划分的弦的集合
		findMinEdges(0, vertexNum-1, minEdge,edges);
		
		
		return new Result(minWeight[0][vertexNum-1], edges);
	}
	
	private void findMinEdges(int i,int j,int [][]minEdge,ArrayList<String> edges){
		
		int k=minEdge[i][j];
		if(j==i+1)
			return ;
		if(k==i+1&&j==k+1){
			//vi vj vk 是相邻三个顶点
			//这三个顶点组成的三角形的边vivk, 和vkvj 都是凸多边形的边,不是弦，故不用计算在内
			//
	//		edges.add("v"+i+"v"+k);
	//		edges.add("v"+k+"v"+j);
		}
		else{
			//vivk不是凸多边形的边，计算在弦集合内
			if(k!=i+1)
				edges.add("v"+(i+1)+"v"+(k+1));
			//vkvj不是凸多边形的边，计算在弦集合内
			if(j!=k+1)
				edges.add("v"+(k+1)+"v"+(j+1));
			//递归查找两个子凸多边形
			//{vi,vi+1,...,vk}
			findMinEdges(i, k, minEdge,edges);
			//{vk,vk+1,...,vj}
			findMinEdges(k, j, minEdge,edges);
		}
	}
	/**
	 * 输出minWeight矩阵的信息到目标文件
	 * @param foutname 目标文件的路径
	 * @throws IOException
	 */
	public void outputMinWeightMatrix(String foutname) throws IOException{
		File fout=new File(foutname);
		FileWriter writer=new FileWriter(fout);
		
		//首先在文件的第一行写入顶点的数目
		if(vertexNum==0){
			writer.write("0\r\n");
		}
		else{
			writer.write(vertexNum+"\r\n");
			writer.write('\t');
			
			//写入第一行
			for(int i=0;i<vertexNum;++i){
				writer.write("v"+i+'\t');
			}
			writer.write("\r\n");
			writer.flush();
			
			//写入数据
			for(int i=0;i<vertexNum;++i){
				StringBuffer sb=new StringBuffer();
				sb.append("v"+i+'\t');
				for(int j=0;j<vertexNum;++j){
					sb.append(minWeight[i][j]);
					sb.append('\t');
				}
				sb.append("\r\n");
				writer.write(sb.toString());
				writer.flush();
			}
		}
		writer.close();
	}
	/**
	 * 输出minEdge矩阵的信息到目标文件
	 * @param foutname 目标文件的路径
	 * @throws IOException
	 */
	public void outputMinEdgeMatrix(String foutname) throws IOException{
		File fout=new File(foutname);
		FileWriter writer=new FileWriter(fout);
		if(vertexNum==0){
			writer.write("0\r\n");
		}
		else{
			writer.write(vertexNum+"\r\n");
			writer.write('\t');
			for(int i=0;i<vertexNum;++i){
				writer.write("v"+i+'\t');
			}
			writer.write("\r\n");
			writer.flush();
			for(int i=0;i<vertexNum;++i){
				StringBuffer sb=new StringBuffer();
				sb.append("v"+i+'\t');
				for(int j=0;j<vertexNum;++j){
					//如果第三顶点不存在，写入-1，否则，写入相应顶点的下标
					if(minEdge[i][j]!=-1){
						sb.append("v"+minEdge[i][j]);
					}
					else{
						sb.append("-1");
					}
					sb.append('\t');
				}
				sb.append("\r\n");

				writer.write(sb.toString());
				writer.flush();
			}
		}
		writer.close();
	}
}
