package mwd;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ͹�������СȨ�������λ����㷨ʵ�ֵ���
 * �㷨���ö�̬�滮��˼��
 * @author ���� SY0906405
 *
 */
public class MinWeightDividing {

	
	private int data[][]; //�������ݣ���ͼ���ڽӾ��� data[i][j]��ʾ��vivj�ϵ�Ȩ�ش�С
	
	private int vertexNum;
	//״̬����minWeight[i][j]��ʾ͹�����{vi,vi+1,...,vj}����СȨ�������λ��ֶ�Ӧ��Ȩ��ֵ
	private int minWeight[][]=null;
	//����minEdge[i][j]��ʾ��С����ʱ��vjvi���������εĵ�����������±�
	private int minEdge[][]=null;
	
	/**
	 * ���캯��
	 * @param data  �������ݣ���ͼ���ڽӾ��� data[i][j]��ʾ��vivj�ϵ�Ȩ�ش�С
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
	 * ״̬����minWeight[i][j]��ʾ͹�����{vi,vi+1,...,vj}����СȨ�������λ��ֶ�Ӧ��Ȩ��ֵ
	 * ���ƹ�ϵʽ:
	 * minWeight[i][j]=min(minWeight[i][k]+minWeight[k][i]+weight(i,k,j))
	 * weight(i,k,j)������i,j,k��ɵ������ε����ߵ�Ȩ�غ�
	 * @return Result �������н����ʵ��
	 */
	public Result getMinWeightDividing(){
		if(data==null){
			return null;
		}
		if(vertexNum==0){
			return null;
		}
		//��ʼ��
		for(int i=0;i<vertexNum;++i){
			for(int j=0;j<vertexNum;++j){
				minEdge[i][j]=-1;
			}
		}
		//͹������˻���һ���㣬minWeight[i][i]=0;
		for(int i=0;i<vertexNum;++i){
			minWeight[i][i]=0;
		}
		//͹������˻���һ���ߣ�minWeight[i][i]=0;
		for(int i=0;i<vertexNum-1;++i){
			minWeight[i][i+1]=0;
		}
		//һ�����͹�����{vi,vi+1,...,vj}���ٰ�����������
		//l��ʾ͹����ζ�����Ŀ�����㶥��i�������2��ʼ��˵��͹�����{vi,vi+1,...,vj}���ٰ�����������
		for(int l=2;l<vertexNum;++l){
			for(int i=0;i<vertexNum-l;++i){
				int j=i+l;
				//�Ƚ�minWeight[i][j]��ֵ�����ֵ
				minWeight[i][j]=Integer.MAX_VALUE;
				
				//k��������i+1��j-1���ҵ��˽׶ε����Ż���
				for(int k=i+1;k<j;++k){
					int temp=0;
					temp=minWeight[i][k]+minWeight[k][j]+data[i][k]+data[k][j]+data[j][i];
					//����ҵ����õ������λ���
					//����minWeight�������εĵ���������
					if(temp<minWeight[i][j]){
						minWeight[i][j]=temp;
						minEdge[i][j]=k;
					}
					
				}
			}
			
		}
		
		//edges�����������Ż����ǵ��ҵļ���(��������)
		ArrayList<String> edges=new ArrayList<String>();
		
		//�������minEdge���õ���С���ֵ��ҵļ���
		findMinEdges(0, vertexNum-1, minEdge,edges);
		
		
		return new Result(minWeight[0][vertexNum-1], edges);
	}
	
	private void findMinEdges(int i,int j,int [][]minEdge,ArrayList<String> edges){
		
		int k=minEdge[i][j];
		if(j==i+1)
			return ;
		if(k==i+1&&j==k+1){
			//vi vj vk ��������������
			//������������ɵ������εı�vivk, ��vkvj ����͹����εı�,�����ң��ʲ��ü�������
			//
	//		edges.add("v"+i+"v"+k);
	//		edges.add("v"+k+"v"+j);
		}
		else{
			//vivk����͹����εıߣ��������Ҽ�����
			if(k!=i+1)
				edges.add("v"+(i+1)+"v"+(k+1));
			//vkvj����͹����εıߣ��������Ҽ�����
			if(j!=k+1)
				edges.add("v"+(k+1)+"v"+(j+1));
			//�ݹ����������͹�����
			//{vi,vi+1,...,vk}
			findMinEdges(i, k, minEdge,edges);
			//{vk,vk+1,...,vj}
			findMinEdges(k, j, minEdge,edges);
		}
	}
	/**
	 * ���minWeight�������Ϣ��Ŀ���ļ�
	 * @param foutname Ŀ���ļ���·��
	 * @throws IOException
	 */
	public void outputMinWeightMatrix(String foutname) throws IOException{
		File fout=new File(foutname);
		FileWriter writer=new FileWriter(fout);
		
		//�������ļ��ĵ�һ��д�붥�����Ŀ
		if(vertexNum==0){
			writer.write("0\r\n");
		}
		else{
			writer.write(vertexNum+"\r\n");
			writer.write('\t');
			
			//д���һ��
			for(int i=0;i<vertexNum;++i){
				writer.write("v"+i+'\t');
			}
			writer.write("\r\n");
			writer.flush();
			
			//д������
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
	 * ���minEdge�������Ϣ��Ŀ���ļ�
	 * @param foutname Ŀ���ļ���·��
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
					//����������㲻���ڣ�д��-1������д����Ӧ������±�
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
