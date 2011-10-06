/**
 * 
 */
package shortestPath;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
/**
 * �׳��С��ҳ����Լ�����������֮��Ĺ�·��ͨ�����ÿ�ι�·�ĳ����ɾ���M1 ������
 * ÿ�ι�·���ɵط�������ȡ��ͬ��ȵ���·�ѵȷ��ã����������ɾ���M2 ������
 * ��������踶��·���ܶ���� 1500 �������
 * �ù�˾�����������Ʒ�Ӽ׳��е��ҳ��е��������·�ߡ�
 * �������·���������
 * @author SY0906405 ����
 *
 */
public class ShortestPath {
	public static void main(String[] args) {
		try {
			ShortestPath sv=new ShortestPath("./m1.txt", "./m2.txt", 1500);
			sv.getShortestPath();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//�ɸ���·�ѵ����ֵ
	private int maxCost;
	//������֮��Ĺ�·��ͨ�����ÿ�ι�·�ĳ��Ⱦ���
	//lengthData[i][j]��ʾ����i�ͳ���j֮�乫·�ĳ���
	private int [][] lengthData;
	//ÿ�ι�·��ȡ�ķ��þ���
	//costData[i][j]��ʾ����i�ͳ���j֮�乫·��ȡ�ķ���
	private int [][] costData;
	//��¼���е���Ŀ
	private int cityNumber;
	//lengthbound[i]��ʾ����i�����һ�����е���̾��룬���ֵ��Ϊ��֧�����㷨��boundֵ
	private int lengthbound[];
	//costbound[i]��ʾ����i�����һ�����е������·�ѵ�ֵ�����ֵ��Ϊ��֧�����㷨��boundֵ
	private int costbound[];
	
	//�����������ֵΪ9999
	public static int MAXVALUE=9999;
	
	//��ǰ�������������Ž��ֵ
	private int shortestLength;
	
	//�������Ž��Ӧ��·��
	private LinkedList<node> bastSolution;
	
	
	//getter and setter
	public int getShortestLength() {
		return shortestLength;
	}
	public LinkedList<node> getBastSolution() {
		return bastSolution;
	}
	/**
	 * ���캯��
	 * @param lengthFile �������֮�乫·�ĳ��Ⱦ�����ļ���
	 * @param costFile �������֮��ÿ�ι�·��ȡ�ķ��þ�����ļ���
	 * @param maxCost ����������·���ܶ�
	 * @throws FileNotFoundException �ļ�δ�ҵ����׳��쳣
	 */
	public ShortestPath(String lengthFile, String costFile, int maxCost) throws FileNotFoundException {
		// TODO Auto-generated constructor stub
		this.maxCost=maxCost;
		//��ȡ����
		lengthData=new DataReader(lengthFile).getData();
		costData=new DataReader(costFile).getData();
		//��ʼ�����Ž��Ӧ��·��
		bastSolution=new LinkedList<node>();
		//���е���ĿΪ����ĳ���
		cityNumber=lengthData.length;

		//������ͨ�ĳ���֮�����·�ѵ�ֵ����Ϊ�����
		for(int i=0;i<cityNumber;++i){
			for(int j=0;j<cityNumber;++j){
				if(lengthData[i][j]==MAXVALUE){
					costData[i][j]=MAXVALUE;
				}
			}
		}
		
		//��ʼ��boundֵΪ�����
		lengthbound=new int[cityNumber];
		costbound=new int[cityNumber];
		for(int i=0;i<cityNumber-1;++i){
			lengthbound[i]=lengthData[i][cityNumber-1];
			costbound[i]=costData[i][cityNumber-1];
		}
		//��ʼ���������������Ž�ֵΪ�����
		shortestLength=MAXVALUE;
	}
	/**
	 * ��Dijkstra�㷨����ǰN-1�����е������һ�����е���̾���
	 * �㷨�����һ�����п�ʼ��������
	 * �þ������ڷ�֧�����㷨��boundֵ
	 */
	public void getLengthBound(){
		//set Ϊδ��չ���ļ���
		ArrayList<Integer> set=new ArrayList<Integer>();
		//��ǰN-1�����м���set������
		for(int i=0;i<cityNumber-1;++i){
			set.add(i);
		}
		
		while(set.size()>0){
			//�ҵ�set�����е������һ�����о�������ĳ���
			int min=Integer.MAX_VALUE;
			int min_index=-1;
			for(int i=0;i<set.size();++i){
				if(lengthbound[set.get(i)]<min){
					min=lengthbound[set.get(i)];
					min_index=i;
				}
			}
			
			if(min_index!=-1){
				int node_index=set.get(min_index);
				//���ҵ��ĳ��д�set�����г�ȥ
				set.remove(min_index);
				//����set���ϵ�����Ԫ�أ������䵽�����һ�����е���̾���
				for(int i=0;i<set.size();++i){
					int temp_index=set.get(i);
					int temp=lengthbound[node_index]+lengthData[temp_index][node_index];
					if(temp<lengthbound[temp_index]){
						lengthbound[temp_index]=temp;
					}
				}
			}
			else{
				break;
			}
		}
	}
	/**
	 * ��Dijkstra�㷨����ǰN-1�����е������һ�����е���С��·�ѵ�ֵ
	 * �㷨�����һ�����п�ʼ��������
	 * �þ������ڷ�֧�����㷨��boundֵ
	 */
	public void getCostBound(){
		//set Ϊδ��չ���ļ���
		ArrayList<Integer> set=new ArrayList<Integer>();
		for(int i=0;i<cityNumber-1;++i){
			set.add(i);
		}
		//��ǰN-1�����м���set������
		while(set.size()>0){
			//�ҵ�set�����е������һ��������·����С�ĳ���
			int min=Integer.MAX_VALUE;
			int min_index=-1;
			for(int i=0;i<set.size();++i){
				if(costbound[set.get(i)]<min){
					min=costbound[set.get(i)];
					min_index=i;
				}
			}
			if(min_index!=-1){
				//����set���ϵ�����Ԫ�أ������䵽�����һ�����е���С��·��
				for(int i=0;i<set.size();++i){
					if(set.get(i)!=set.get(min_index)){
						int temp=costbound[set.get(min_index)]+costData[set.get(i)][set.get(min_index)];
						if(temp<costbound[set.get(i)]){
							costbound[set.get(i)]=temp;
						}
					}
				}
				//���ҵ��ĳ��д�set�����г�ȥ
				set.remove(min_index);
			}
			else{
				break;
			}
		}
	}
	/**
	 * �÷�֧���編�������·��
	 * ʹ��һ����ջʵ�ֻ�������
	 */
	public void getShortestPath(){
		//�������·������С��·��
		getLengthBound();
		getCostBound();
		//��ʼ����ջ
		LinkedList<node> stack=new LinkedList<node>();
		
		//����һ����ʱ������
		ArrayList<node> temp=new ArrayList<node>();
		for(int i=1;i<cityNumber;++i){
			//���ܵ������0���������ж����뵽������
			if(lengthData[0][i]!=MAXVALUE){
				node nn=new node(i,null);
				nn.setCostBound(costbound[i]);
				nn.setLengthBound(lengthbound[i]);
				nn.setCurLength(lengthData[0][i]);
				nn.setCurCost(costData[0][i]);
				temp.add(nn);
			}
		}
		//����������
		//boundֵ���Ԫ�� ����ǰ��
		//boundֵ�ļ��㣺��ǰ·���ĳ���+�������һ���������·���ĳ���
		Collections.sort(temp, new Comparator<node>() {
			@Override
			public int compare(node o1, node o2) {
				// TODO Auto-generated method stub
				return o2.getLengthBound()+o2.getCurLength()-o1.getLengthBound()-o1.getCurLength();
			}
		});
		//�������Ԫ�شӴ�Сѹ���ջ
		stack.addAll(temp);
		int count=0;
		while(stack.size()>0){
			//����ջ���ڵ㣬����ǰ·���ĳ���+�������һ���������·���ĳ���ֵ��С��Ԫ��
			node curnode=stack.pollLast();
			//����ýڵ㲻�����֦Ҫ��
			if(!curnode.shouldPrun(shortestLength, maxCost)){
				count++; //for debug
				//����ǰ�ڵ㵽��Ŀ�����
				if(curnode.getIndex()==cityNumber-1){
					//�����������Ľ�
					if(curnode.getCurCost()<=maxCost&&curnode.getCurLength()<=shortestLength){
						shortestLength=curnode.getCurLength();
						
						//���浱ǰ���Ž��·��
						bastSolution.clear();
						bastSolution.add(curnode);
						
						node n=curnode.getParent();
						while(n!=null){
							bastSolution.add(n);
							n=n.getParent();
						}
						
						showBestPath(bastSolution);
					}
				}
				//��ǰ�ڵ�����������
				else{
					int cur_index=curnode.getIndex();
					//���ܵ��ﵱǰ���е��������ж����뵽temp������
					temp=new ArrayList<node>();
					for(int i=0;i<cityNumber;++i){
						if(lengthData[cur_index][i]!=MAXVALUE){
							node nn=new node(i,curnode);
							nn.setCostBound(costbound[i]);
							nn.setLengthBound(lengthbound[i]);
							nn.setCurLength(lengthData[cur_index][i]+curnode.getCurLength());
							nn.setCurCost(costData[cur_index][i]+curnode.getCurCost());
							temp.add(nn);
						}
					}
					//����������
					//boundֵ���Ԫ�� ����ǰ��
					//boundֵ�ļ��㣺��ǰ·���ĳ���+�������һ���������·���ĳ���
					Collections.sort(temp, new Comparator<node>() {
						@Override
						public int compare(node o1, node o2) {
							// TODO Auto-generated method stub
							return o2.getLengthBound()+o2.getCurLength()-o1.getLengthBound()-o1.getCurLength();
						}
					});
					//�������Ԫ�شӴ�Сѹ���ջ
					stack.addAll(temp);
				}
			}
			else{
				;
			}
		}
//		System.out.println(count);
//		System.out.println(shortestLength);
	}
	private void showBestPath(LinkedList<node> best){
		StringBuffer sb=new StringBuffer();
		sb.append("0->");
		for(int i=best.size()-1;i>0;--i){
			sb.append(best.get(i).getIndex());
			sb.append("->");
		}
		sb.append(best.get(0).getIndex());
		System.out.print(sb.toString());
		
		System.out.print("\tShortest Length:"+best.get(0).getCurLength());
		System.out.println("\tCost:"+best.get(0).getCurCost());
	}
}

/**
 * ��¼ÿ�λ��ݱ�����״̬�ڵ�
 * @author wanghan
 *
 */
class node{
	int index; //�����±�ֵ
	int costBound; //��״̬����·�ѵ�bound
	int lengthBound; //��״̬��·������bound
	int curCost; //��ǰ��·��
	int curLength; //��ǰ·����
	node parent; //��״̬�ĸ�״̬����������·���£�����ó��е���һ�����е�״̬
	public node(int index,node parent) {
		// TODO Auto-generated constructor stub
		this.index=index;
		this.parent=parent;
	}
	/**
	 * ����һ���ڵ�ʱ��Ӧ�ñ���֦
	 * @param minLength Ŀǰ�������������Ž⣨��̾��룩
	 * @param maxCost ����֧������·�ѵ�ֵ
	 * @return
	 */
	public boolean shouldPrun(int minLength,int maxCost){
		//�����ǰ�Ѹ���·�ѵ�ֵ+��ǰ���е���Ŀ�������С��·�ѵ�ֵ���ڿ�֧����������·��ֵ�����֦
		//�����ǰ����·���ĳ���+��ǰ���е���Ŀ�������С·�����ȴ������ѵ����Ž��·�����ȣ����֦
		if(costBound+curCost>=maxCost||lengthBound+curLength>=minLength){
			return true;
		}
		return false;
	}
	//getter and setter
	public int getCostBound() {
		return costBound;
	}
	public void setCostBound(int costBound) {
		this.costBound = costBound;
	}
	public int getLengthBound() {
		return lengthBound;
	}
	public void setLengthBound(int lengthBound) {
		this.lengthBound = lengthBound;
	}
	public int getIndex() {
		return index;
	}
	public int getCurCost() {
		return curCost;
	}
	public void setCurCost(int curCost) {
		this.curCost = curCost;
	}
	public int getCurLength() {
		return curLength;
	}
	public void setCurLength(int curLength) {
		this.curLength = curLength;
	}
	
	
	public node getParent() {
		return parent;
	}
	
}