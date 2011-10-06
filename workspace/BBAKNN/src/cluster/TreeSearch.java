/**
 * 
 */
package cluster;

import java.util.ArrayList;
import java.util.Collections;

import brute.BruteKNN;

import ui.ShowFrame;
import utils.EuclideanDistance;

/**
 * @author wanghan
 * 
 */
public class TreeSearch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int pts = 1000;
		int ptDims = 2;
		float dataPts[][] = new float[pts][ptDims];
		ArrayList<Point> points = new ArrayList<Point>();
		float[] xPts = new float[ptDims];
		for (int i = 0; i < dataPts.length; i++) {
			for (int j = 0; j < ptDims; j++) {
				dataPts[i][j] = (float) Math.random() * 800;
				dataPts[i][j] = (float) Math.random() * 800;
			}

			points.add(new Point(dataPts[i]));
		}
		for (int j = 0; j < ptDims; j++) {
			xPts[j] = (float) Math.random() * 800;
		}

		Point x = new Point(xPts);
		for (int i = 5; i < 11; i += 10) {
			long begin = System.currentTimeMillis();
			TreeSearch BABKNN = new TreeSearch(i);

			BABKNN.search(points, x);

			ShowFrame window_result = new ShowFrame(x, BABKNN.getRoot()
					.getChilren(), BABKNN.getNNs());
			window_result.getFrame().setVisible(true);

			long after = System.currentTimeMillis();
			BruteKNN bruteKNN = new BruteKNN(i);
			bruteKNN.search(points, x);
			long after2 = System.currentTimeMillis();
			System.out.println(i + "  " + (after - begin) + "   "
					+ (after2 - after));
		}

	}

	private int MAX_LEVEL_NUMBER = 3;
	private int MAX_CLUSTER_NUMBER = 3;
	private int K;
	private ArrayList<NearestNeighbor> NNs;
	private TreeModel root = null;

	public TreeSearch(int k) {
		// TODO Auto-generated constructor stub
		this.K = k;
		NNs = new ArrayList<NearestNeighbor>(k);
	}

	public void search(ArrayList<Point> dataPoints, Point x) {

		ClusterModel cluster = new ClusterModel();
		cluster.points = dataPoints;
		cluster.center = dataPoints.get(0);
		cluster.updateCenter();

		long before = System.currentTimeMillis();
		root = generateTree(cluster, 0);
		ShowFrame window_result = new ShowFrame(x, root.getChilren(), 1);
		window_result.getFrame().setVisible(true);
		long after = System.currentTimeMillis();

		System.out
				.println("Clustering Time: " + String.valueOf(after - before));
		NNs.clear();
		// double
		// initialBound=EuclideanDistance.euclideanDistance(root.node.center,
		// x)+root.node.getFarestDis();
		// NNs.add(new NearestNeighbor(new Point(new
		// double[]{-1,-1}),initialBound));
		int currentLevel = 1;

		subSearch(currentLevel, root, x);

		System.out.println(1);
	}

	private void subSearch(int level, TreeModel node, Point x) {
		if (level > MAX_LEVEL_NUMBER) {
			return;
		}
		if (level == MAX_LEVEL_NUMBER) {
			double d_x_mp = EuclideanDistance.euclideanDistance(x,
					node.node.center);
			for (Point point : node.node.points) {
				double bound = getBound();
				double dis = d_x_mp
						- EuclideanDistance.euclideanDistance(point,
								node.node.center);

				if (dis > bound) {
					continue;
				} else {
					double temp = EuclideanDistance.euclideanDistance(point, x);
					if (temp < bound) {
						UpdateBound(point, temp);
					}
				}
			}
		} else {
			ArrayList<TreeModel> activeList = new ArrayList<TreeModel>();

			for (TreeModel treeModel : node.chilren) {
				activeList.add(treeModel);
			}
			// test rule 1
			double bound = getBound();
			for (int i = 0; i < activeList.size(); ++i) {

				double dis = EuclideanDistance.euclideanDistance(activeList
						.get(i).node.center, x)
						- activeList.get(i).node.getFarestDis();
				if (dis > bound) {
					activeList.remove(i);
					i--;
				}
			}
			while (activeList.size() != 0) {

				// choose the nearest node p
				int nearest_index = -1;
				double nearest_dis = Double.MAX_VALUE;
				for (int i = 0; i < activeList.size(); ++i) {
					double temp = EuclideanDistance.euclideanDistance(x,
							activeList.get(i).node.center);
					if (temp < nearest_dis) {
						nearest_dis = temp;
						nearest_index = i;
					}
				}

				TreeModel curnode = generateTree(
						activeList.get(nearest_index).node, node.level);
				ShowFrame window_result = new ShowFrame(x,
						curnode.getChilren(), 1);
				window_result.getFrame().setVisible(true);
				subSearch(level + 1, curnode, x);
				activeList.remove(nearest_index);
			}
		}

	}

	public double getBound() {

		if (NNs.size() == 0) {
			return Double.MAX_VALUE;
		}
		Collections.sort(NNs);
		return NNs.get(0).getDistance();
	}

	public void UpdateBound(Point p, double dis) {
		if (NNs.size() == K) {
			Collections.sort(NNs);
			NNs.remove(0);
			NNs.add(new NearestNeighbor(p, dis));
		} else {
			NNs.add(new NearestNeighbor(p, dis));
		}
	}

	public TreeModel generateTree(ClusterModel cluster, int level) {
		if (level > MAX_LEVEL_NUMBER)
			return null;
		ISODATAClustering isodataAlgorithm = new ISODATAClustering();
		final ArrayList<ClusterModel> results = isodataAlgorithm.clustering(
				cluster.points, MAX_CLUSTER_NUMBER);
		TreeModel root = new TreeModel(cluster, level);
		for (int i = 0; i < results.size(); ++i) {
			// TreeModel child=generateTree(results.get(i), level+1);
			TreeModel child = new TreeModel(results.get(i), level + 1);
			root.chilren.add(child);
		}
		return root;
	}

	public ArrayList<NearestNeighbor> getNNs() {
		return NNs;
	}

	public TreeModel getRoot() {
		return root;
	}

}
