/**
 * 
 */
package cluster;

import java.util.ArrayList;
import utils.EuclideanDistance;

/**
 * @author wanghan
 * 
 */
public class ISODATAClustering {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int pts = 1000;
		int ptDims = 2;
		int clsts = 25;

		ISODATAClustering isodataAlgorithm = new ISODATAClustering();

		float dataPts[][] = new float[pts][ptDims];
		ArrayList<Point> points = new ArrayList<Point>();
		for (int i = 0; i < dataPts.length; i++) {
			for (int j = 0; j < ptDims; j++) {
				dataPts[i][j] = (float) Math.random();
				dataPts[i][j] = (float) Math.random();
			}
			points.add(new Point(dataPts[i]));
		}

		// Print data pts
		for (int j = 0; j < dataPts[0].length; j++) {
			for (int i = 0; i < dataPts.length; i++) {

				System.out.print("" + dataPts[i][j] + " ");

			}
			System.out.print("\n");
		}
		System.out.print("\n");
		ClusterModel cluster = new ClusterModel();
		cluster.points = points;
		cluster.updateCenter();

		final ArrayList<ClusterModel> results = isodataAlgorithm.clustering(
				cluster.points, clsts);

		// try {
		// ShowFrame window = new ShowFrame(results);
		// window.getFrame().setVisible(true);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// System.out.println(1);
	}

	// Default algorithm parameters
	private static final double defaultChangeThreshold = 0.1;
	private static final int defaultMaxIterations = 5;
	private static final boolean defaultMergeClusters = false;
	private static final double defaultMergeThreshold = 1.0;
	private static final boolean defaultSplitClusters = false;
	private static final double defaultSplitThreshold = 1.0e20;

	// Our algorithm parameters
	private double changeThreshold = defaultChangeThreshold;
	private int maxIterations = defaultMaxIterations;
	private boolean mergeClusters = defaultMergeClusters;
	private double mergeThreshold = defaultMergeThreshold;
	private boolean splitClusters = defaultSplitClusters;
	private double splitThreshold = defaultSplitThreshold;

	public ISODATAClustering() {
		// TODO Auto-generated constructor stub
	}

	public ISODATAClustering(int maxIts, double chgThres, boolean merge,
			double mrgThres) {

		changeThreshold = chgThres;
		maxIterations = maxIts;
		mergeClusters = merge;
		mergeThreshold = mrgThres;

	}

	public ArrayList<ClusterModel> clustering(ArrayList<Point> dataPoints,
			int numClusters) {
		ArrayList<ClusterModel> result = new ArrayList<ClusterModel>();
		// Choose initial cluster centers
		for (int i = 0; i < numClusters; ++i) {
			result.add(new ClusterModel());
			result.get(i).setCenter(dataPoints.get(i % dataPoints.size()));
		}

		boolean shouldIterate = true;
		int iterNumber = 0;

		while ((iterNumber < maxIterations) && (shouldIterate)) {

			// Clear out the assignment vectors
			for (int i = 0; i < result.size(); i++) {
				result.get(i).points.clear();
			}

			// Assign every data point to a cluster
			for (int i = 0; i < dataPoints.size(); i++) {

				int closest = closestClusterCenter(result, dataPoints.get(i));
				result.get(closest).points.add(dataPoints.get(i));

			}
			// update new cluster center
			// updateClusterCenters(result);
			// If relevant, dump any empty clusters
			purgeEmptyClusters(result);

			if (underChangeThreshold(result)) {

				shouldIterate = false;

			}

			// Increment iteration count
			iterNumber++;
		}

		// System.out.println("Iter Num:"+iterNumber);
		return result;
	}

	private int closestClusterCenter(ArrayList<ClusterModel> clusters,
			Point dataPt) {

		int closestCluster = -1;
		double closestClusterDistance = Double.MAX_VALUE;

		for (int j = 0; j < clusters.size(); j++) {

			// calculate euclidead distance
			double distance = EuclideanDistance.euclideanDistance(dataPt,
					clusters.get(j).getCenter());

			// check and set if closest cluster
			if (distance < closestClusterDistance) {
				closestClusterDistance = distance;
				closestCluster = j;
			}
		}

		return closestCluster;

	}

	private void updateClusterCenters(ArrayList<ClusterModel> clusters) {

		for (int i = 0; i < clusters.size(); i++) {

			clusters.get(i).updateCenter();
		}

	}

	private void purgeEmptyClusters(ArrayList<ClusterModel> clusters) {

		// Check for bad cluster points and assignment arrays
		for (int i = 0; i < clusters.size(); i++) {

			// Check if bad point
			if (clusters.get(i).points.size() == 0) {

				clusters.remove(i);
				i--;

			}

		}

	}

	private boolean underChangeThreshold(ArrayList<ClusterModel> newClusters) {

		boolean result = true;

		for (int i = 0; i < newClusters.size(); i++) {
			Point newCenter = newClusters.get(i).getCenterPoint();
			if (EuclideanDistance.euclideanDistance(newClusters.get(i).center,
					newCenter) >= changeThreshold) {

				result = false;

			}
			newClusters.get(i).center = newCenter;
		}

		return result;

	}

}
