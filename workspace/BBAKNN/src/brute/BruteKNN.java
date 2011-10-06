/**
 * 
 */
package brute;

import java.util.ArrayList;
import java.util.Collections;

import utils.EuclideanDistance;

import cluster.NearestNeighbor;
import cluster.Point;

/**
 * @author wanghan
 *
 */
public class BruteKNN {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private ArrayList<NearestNeighbor> NNs;
	private int K;
	
	public BruteKNN(int k) {
		// TODO Auto-generated constructor stub
		this.K=k;
		NNs=new ArrayList<NearestNeighbor>(k);
	}
	
	public void search(ArrayList<Point> dataPoints,Point x){
		for (Point point : dataPoints) {
			double dis=EuclideanDistance.euclideanDistance(point, x);
			double bound=getBound();
			if(bound>dis){
				addNewNeighbor(point, dis);
			}
		}
		
	}
	private void addNewNeighbor(Point p, double dis){
		if(NNs.size()==K){
			Collections.sort(NNs);
			NNs.remove(0);
			NNs.add(new NearestNeighbor(p,dis));
		}
		else{
			NNs.add(new NearestNeighbor(p,dis));
		}
	}
	public double getBound(){
		if(NNs.size()==0){
			return Double.MAX_VALUE;
		}
		Collections.sort(NNs);
		return NNs.get(0).getDistance();
	}

	public ArrayList<NearestNeighbor> getNNs() {
		return NNs;
	}
	
}
