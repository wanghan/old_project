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
 * 甲城市、乙城市以及其它各城市之间的公路连通情况及每段公路的长度由矩阵M1 给出。
 * 每段公路均由地方政府收取不同额度的养路费等费用，具体数额由矩阵M2 给出。
 * 请给出在需付养路费总额不超过 1500 的情况下
 * 该公司货车运送其产品从甲城市到乙城市的最短运送路线。
 * 计算最短路径问题的类
 * @author SY0906405 王晗
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

	//可付养路费的最大值
	private int maxCost;
	//各城市之间的公路连通情况及每段公路的长度矩阵
	//lengthData[i][j]表示城市i和城市j之间公路的长度
	private int [][] lengthData;
	//每段公路收取的费用矩阵
	//costData[i][j]表示城市i和城市j之间公路收取的费用
	private int [][] costData;
	//记录城市的数目
	private int cityNumber;
	//lengthbound[i]表示城市i到最后一个城市的最短距离，这个值作为分支定界算法的bound值
	private int lengthbound[];
	//costbound[i]表示城市i到最后一个城市的最低养路费的值，这个值作为分支定界算法的bound值
	private int costbound[];
	
	//定义无穷大数值为9999
	public static int MAXVALUE=9999;
	
	//当前已搜索到得最优解得值
	private int shortestLength;
	
	//保存最优解对应的路径
	private LinkedList<node> bastSolution;
	
	
	//getter and setter
	public int getShortestLength() {
		return shortestLength;
	}
	public LinkedList<node> getBastSolution() {
		return bastSolution;
	}
	/**
	 * 构造函数
	 * @param lengthFile 保存城市之间公路的长度矩阵的文件名
	 * @param costFile 保存城市之间每段公路收取的费用矩阵的文件名
	 * @param maxCost 容许的最大养路费总额
	 * @throws FileNotFoundException 文件未找到，抛出异常
	 */
	public ShortestPath(String lengthFile, String costFile, int maxCost) throws FileNotFoundException {
		// TODO Auto-generated constructor stub
		this.maxCost=maxCost;
		//读取数据
		lengthData=new DataReader(lengthFile).getData();
		costData=new DataReader(costFile).getData();
		//初始化最优解对应的路径
		bastSolution=new LinkedList<node>();
		//城市的数目为矩阵的长度
		cityNumber=lengthData.length;

		//将不连通的城市之间的养路费的值设置为无穷大
		for(int i=0;i<cityNumber;++i){
			for(int j=0;j<cityNumber;++j){
				if(lengthData[i][j]==MAXVALUE){
					costData[i][j]=MAXVALUE;
				}
			}
		}
		
		//初始化bound值为无穷大
		lengthbound=new int[cityNumber];
		costbound=new int[cityNumber];
		for(int i=0;i<cityNumber-1;++i){
			lengthbound[i]=lengthData[i][cityNumber-1];
			costbound[i]=costData[i][cityNumber-1];
		}
		//初始化已搜索到得最优解值为无穷大
		shortestLength=MAXVALUE;
	}
	/**
	 * 用Dijkstra算法计算前N-1个城市到达最后一个城市的最短距离
	 * 算法从最后一个城市开始反向搜索
	 * 该距离用于分支定界算法的bound值
	 */
	public void getLengthBound(){
		//set 为未扩展到的集合
		ArrayList<Integer> set=new ArrayList<Integer>();
		//将前N-1个城市加入set集合中
		for(int i=0;i<cityNumber-1;++i){
			set.add(i);
		}
		
		while(set.size()>0){
			//找到set集合中到达最后一个城市距离最近的城市
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
				//将找到的城市从set集合中除去
				set.remove(min_index);
				//对于set集合的其他元素，更新其到达最后一个城市的最短距离
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
	 * 用Dijkstra算法计算前N-1个城市到达最后一个城市的最小养路费的值
	 * 算法从最后一个城市开始反向搜索
	 * 该距离用于分支定界算法的bound值
	 */
	public void getCostBound(){
		//set 为未扩展到的集合
		ArrayList<Integer> set=new ArrayList<Integer>();
		for(int i=0;i<cityNumber-1;++i){
			set.add(i);
		}
		//将前N-1个城市加入set集合中
		while(set.size()>0){
			//找到set集合中到达最后一个城市养路费最小的城市
			int min=Integer.MAX_VALUE;
			int min_index=-1;
			for(int i=0;i<set.size();++i){
				if(costbound[set.get(i)]<min){
					min=costbound[set.get(i)];
					min_index=i;
				}
			}
			if(min_index!=-1){
				//对于set集合的其他元素，更新其到达最后一个城市的最小养路费
				for(int i=0;i<set.size();++i){
					if(set.get(i)!=set.get(min_index)){
						int temp=costbound[set.get(min_index)]+costData[set.get(i)][set.get(min_index)];
						if(temp<costbound[set.get(i)]){
							costbound[set.get(i)]=temp;
						}
					}
				}
				//将找到的城市从set集合中除去
				set.remove(min_index);
			}
			else{
				break;
			}
		}
	}
	/**
	 * 用分支定界法计算最短路径
	 * 使用一个堆栈实现回溯搜索
	 */
	public void getShortestPath(){
		//计算最短路径和最小养路费
		getLengthBound();
		getCostBound();
		//初始化堆栈
		LinkedList<node> stack=new LinkedList<node>();
		
		//申请一个临时的链表
		ArrayList<node> temp=new ArrayList<node>();
		for(int i=1;i<cityNumber;++i){
			//将能到达城市0的其他城市都加入到链表中
			if(lengthData[0][i]!=MAXVALUE){
				node nn=new node(i,null);
				nn.setCostBound(costbound[i]);
				nn.setLengthBound(lengthbound[i]);
				nn.setCurLength(lengthData[0][i]);
				nn.setCurCost(costData[0][i]);
				temp.add(nn);
			}
		}
		//对链表排序
		//bound值大的元素 排在前面
		//bound值的计算：当前路径的长度+到达最后一个城市最短路径的长度
		Collections.sort(temp, new Comparator<node>() {
			@Override
			public int compare(node o1, node o2) {
				// TODO Auto-generated method stub
				return o2.getLengthBound()+o2.getCurLength()-o1.getLengthBound()-o1.getCurLength();
			}
		});
		//将链表的元素从大到小压入堆栈
		stack.addAll(temp);
		int count=0;
		while(stack.size()>0){
			//弹出栈顶节点，即当前路径的长度+到达最后一个城市最短路径的长度值最小的元素
			node curnode=stack.pollLast();
			//如果该节点不满足剪枝要求
			if(!curnode.shouldPrun(shortestLength, maxCost)){
				count++; //for debug
				//若当前节点到达目标城市
				if(curnode.getIndex()==cityNumber-1){
					//是满足条件的解
					if(curnode.getCurCost()<=maxCost&&curnode.getCurLength()<=shortestLength){
						shortestLength=curnode.getCurLength();
						
						//保存当前最优解的路径
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
				//当前节点是其他城市
				else{
					int cur_index=curnode.getIndex();
					//将能到达当前城市的其他城市都加入到temp链表中
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
					//对链表排序
					//bound值大的元素 排在前面
					//bound值的计算：当前路径的长度+到达最后一个城市最短路径的长度
					Collections.sort(temp, new Comparator<node>() {
						@Override
						public int compare(node o1, node o2) {
							// TODO Auto-generated method stub
							return o2.getLengthBound()+o2.getCurLength()-o1.getLengthBound()-o1.getCurLength();
						}
					});
					//将链表的元素从大到小压入堆栈
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
 * 记录每次回溯遍历的状态节点
 * @author wanghan
 *
 */
class node{
	int index; //城市下标值
	int costBound; //该状态的养路费的bound
	int lengthBound; //该状态的路径长的bound
	int curCost; //当前养路费
	int curLength; //当前路径长
	node parent; //该状态的父状态，即此搜索路径下，到达该城市的上一个城市的状态
	public node(int index,node parent) {
		// TODO Auto-generated constructor stub
		this.index=index;
		this.parent=parent;
	}
	/**
	 * 返回一个节点时候应该被剪枝
	 * @param minLength 目前已搜索到得最优解（最短距离）
	 * @param maxCost 最大可支付的养路费的值
	 * @return
	 */
	public boolean shouldPrun(int minLength,int maxCost){
		//如果当前已付养路费的值+当前城市到达目标城市最小养路费的值大于可支付的最大的养路费值，则剪枝
		//如果当前已走路径的长度+当前城市到达目标城市最小路径长度大于已搜到最优解的路径长度，则剪枝
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