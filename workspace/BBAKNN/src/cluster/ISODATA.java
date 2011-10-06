package cluster;

/* 
 * Copyright (c) 2004, Niek Sanders
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *     + Redistributions of source code must retain the above copyright notice, 
 *       this list of conditions and the following disclaimer.
 *     + Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the 
 *       documentation and/or other materials provided with the distribution.
 *     + Neither the name of the Rochester Institute of Technology nor the names
 *       of its contributors may be used to endorse or promote products derived 
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

/*
 * ISODATA.java
 *
 * Version:
 *     $Id: ISODATA.java,v 1.12 2004/05/21 20:08:40 nsanders Exp nsanders $
 *
 * Revisions:
 *     $Log: ISODATA.java,v $
 *     Revision 1.12  2004/05/21 20:08:40  nsanders
 *     Added cluster merging logic.
 *
 *     Revision 1.11  2004/05/21 18:51:02  nsanders
 *     Added hackish empty cluster purger.  It adds an unneeded iteration loop
 *     right now, because we need to threshold check after we purge.  But we 
 *     need to purge threshold lists too in that case.
 *
 *     Revision 1.10  2004/05/21 18:17:10  nsanders
 *     Added minimum change threshold checker.
 *
 *     Revision 1.9  2004/05/21 16:04:56  njs8030
 *     Added a BSD license.
 *
 *     Revision 1.8  2004/05/21 15:54:13  njs8030
 *     Updated the todo list.
 *
 *     Revision 1.7  2004/05/21 15:44:18  njs8030
 *     Fixed center recalc bug causing NaN with empty clusters.
 *
 *     Revision 1.6  2004/05/21 15:42:37  njs8030
 *     Cleaned up the tester program a bit.
 *
 *     Revision 1.5  2004/05/21 15:21:00  njs8030
 *     Broke generateCluster method down using helpers.
 *
 *     Revision 1.4  2004/05/21 14:51:10  njs8030
 *     Added in basic seeding algorithm.  Added in vec/vec and vec/scl helper 
 *     functions to clean up the code.
 *
 *     Revision 1.3  2004/05/21 03:50:47  njs8030
 *     Basics are now working.
 *
 *     Revision 1.2  2004/05/21 01:51:00  njs8030
 *     Compile bug squish.
 *
 *     Revision 1.1  2004/05/21 01:49:56  njs8030
 *     Initial revision
 *
 */

import java.util.Vector;

/**
 * This is an implementation of the ISODATA algorithm. Though it's currently a
 * standalone class, it was designed to be made part of a generic clustering
 * algorithm interface.
 * 
 * @author Niek Sanders
 * 
 */

public class ISODATA {

	// Default algorithm parameters
	private static final double defaultChangeThreshold = 0.01;
	private static final int defaultMaxIterations = 10;
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

	// Internal data structures for clustering
	private double clusterPts[][];
	private double oldClusterPts[][];
	private Vector clusterAsgn[];

	/**
	 * Default constructor. Sets up the ISODATA calculator with default
	 * parameters.
	 * 
	 */
	public ISODATA() {

	}

	/**
	 * Creation constructor.
	 * 
	 * @param maxIts
	 *            Maximum number of iteration loops.
	 * @param chgThres
	 *            Minimum cluster point chnage required to continue iterating.
	 * @param merge
	 *            Whether to merge clusters closer than a certain distance.
	 * @param mrgThres
	 *            Minimum distance before merging begins.
	 * 
	 */
	public ISODATA(int maxIts, double chgThres, boolean merge, double mrgThres) {

		changeThreshold = chgThres;
		maxIterations = maxIts;
		mergeClusters = merge;
		mergeThreshold = mrgThres;

	}

	/**
	 * Generates classes using the ISODATA algorithm. Note that bad things will
	 * happen if there isn't as least one data point.
	 * 
	 * @param dataPoints
	 *            Array of n-vector data points
	 * @param numClusters
	 *            Number of classes we are attempting to form
	 * 
	 * @return Array of n-vectors representing cluster centers
	 * 
	 */
	public ClusterModel[] generateClasses(double[][] dataPoints, int numClusters) {

		// Grab n-vector dimensionality
		int numDims = dataPoints[0].length;

		// Init clustering storage vars
		this.clusterPts = new double[numClusters][numDims];
		this.oldClusterPts = new double[numClusters][numDims];

		// Cluster assignment vectors store dataPoint indices as Integers objs
		this.clusterAsgn = new Vector[numClusters];
		for (int i = 0; i < clusterAsgn.length; i++) {
			clusterAsgn[i] = new Vector();
		}

		// Seed cluster points
		seedClusterPoints(dataPoints);

		// Iteration control vars
		boolean shouldIterate = true;
		int itCnt = 0;

		// Main iteration loop
		while ((itCnt < maxIterations) && (shouldIterate)) {

			// Clear out the assignment vectors
			for (int i = 0; i < clusterAsgn.length; i++) {
				clusterAsgn[i].clear();
			}

			// Assign every data point to a cluster
			for (int i = 0; i < dataPoints.length; i++) {

				int closest = closestClusterCenter(dataPoints[i]);
				clusterAsgn[closest].add(new Integer(i));

			}

			// Save the old cluster centers
			for (int i = 0; i < clusterPts.length; i++) {
				vec_vecAssign(oldClusterPts[i], clusterPts[i]);
			}

			// Calculate new cluster centers
			recomputeClusterCenters(dataPoints);

			// If relevant, dump any empty clusters
			purgeEmptyClusters();

			// If relevant, perform merging (not yet implemented)
			mergeCollapsedClusters();

			// Check if change threshold minimum was met
			if (underChangeThreshold()) {

				// Clusters pts now stable, check if splitting needed
				// FIXME: not yet implemented
				if (!splitSpreadClusters()) {
					shouldIterate = false;
				}

			}

			// Increment iteration count
			itCnt++;

		}

		ClusterModel[] clusters = new ClusterModel[clusterPts.length];
		for (int i = 0; i < clusterPts.length; ++i) {

		}

		System.out.println("Took " + itCnt + "/" + maxIterations
				+ " iterations.");
		System.out.println("Generated " + clusterPts.length + "/" + numClusters
				+ " clusters.");
		System.out.print("\n");

		return clusters;

	}

	/**
	 * Seeds the cluster points. There are really four reasonable ways of
	 * achieving this:
	 * 
	 * (1) Set the centers equal to some of the data points (2) Uniformly grid
	 * centers across the space (3) Randomly plop the centers in space (4) Use
	 * stratified random plopping (semi-grid)
	 * 
	 * Currently, ISODATA only supports the first of these methods.
	 * 
	 * @param dataPt
	 *            n-Vector which we are matching to closest cluster center
	 * 
	 */
	private void seedClusterPoints(double[][] dataPts) {

		for (int i = 0; i < clusterPts.length; i++) {
			vec_vecAssign(clusterPts[i], dataPts[i % dataPts.length]);
		}

	}

	/**
	 * Returns the index of the cluster center closest to the given point.
	 * 
	 * @param dataPt
	 *            n-Vector which we are matching to closest cluster center
	 * 
	 * @return index of closest center
	 * 
	 */
	private int closestClusterCenter(double[] dataPt) {

		int closestCluster = -1;
		double closestClusterDistance = Double.MAX_VALUE;

		for (int j = 0; j < clusterPts.length; j++) {

			// calculate euclidead distance
			double distance = euclideanDistance(dataPt, clusterPts[j]);

			// check and set if closest cluster
			if (distance < closestClusterDistance) {
				closestClusterDistance = distance;
				closestCluster = j;
			}
		}

		return closestCluster;

	}

	/**
	 * Recomputes the cluster centers by taking the mean values of all the data
	 * points assigned to each cluster.
	 * 
	 * @param dataPts
	 *            data points (n-vectors)
	 * 
	 */
	private void recomputeClusterCenters(double[][] dataPts) {

		for (int i = 0; i < clusterPts.length; i++) {

			// Clear old value
			vec_sclSet(clusterPts[i], 0);

			// Sum in all the assigned points
			for (int j = 0; j < clusterAsgn[i].size(); j++) {

				int pIdx = ((Integer) (clusterAsgn[i].get(j))).intValue();
				vec_vecAdd(clusterPts[i], dataPts[pIdx]);

			}

			// Divide by num assigned points to get mean
			if (clusterAsgn[i].size() > 0) {
				vec_sclMult(clusterPts[i], 1.0 / clusterAsgn[i].size());
			}

		}

	}

	/**
	 * Checks if all the cluster centers moved less than the change threshold,
	 * in which case we are done.
	 * 
	 * @return true if all the points moved less than minimum change threshold.
	 * 
	 */
	private boolean underChangeThreshold() {

		boolean result = true;

		for (int i = 0; i < clusterPts.length; i++) {

			if (euclideanDistance(clusterPts[i], oldClusterPts[i]) >= changeThreshold) {

				result = false;

			}

		}

		return result;

	}

	/**
	 * Once we create a stable set of cluster centers, we check if points in a
	 * given cluster are spread out too far. If they are, then it would be wise
	 * to break into two separate clusters and restabilize. However, care must
	 * be taken that we don't get into an infinite splitting/merging loop.
	 * 
	 * @return true if any splitting occurred.
	 * 
	 */
	private boolean splitSpreadClusters() {

		// Just a skeleton function for now.
		return false;

	}

	/**
	 * Get rid of empty clusters. The way the code is set up right now, the
	 * actual dumping of the bad clusters need to be done in the caller, since
	 * we can't pass in the caller's object pointers by reference. FIXME.
	 * 
	 */
	private void purgeEmptyClusters() {

		int numC = clusterPts.length;
		int badCCount = 0;

		// Check for bad cluster points and assignment arrays
		for (int i = 0; i < clusterPts.length; i++) {

			// Check if bad point
			if (clusterAsgn[i].size() == 0) {

				// Replace with good cluster from end of array
				clusterPts[numC - 1 - badCCount] = clusterPts[i];
				oldClusterPts[numC - 1 - badCCount] = oldClusterPts[i];
				clusterAsgn[numC - 1 - badCCount] = clusterAsgn[i];

				// Increment bad cluster count
				badCCount++;

			}

		}

		chopBad(badCCount);

	}

	/**
	 * Merge clusters which are too close together. This is determined by a
	 * minimum merging distance threshold. This code does nothing if the merging
	 * flag is set to false.
	 * 
	 */
	private void mergeCollapsedClusters() {

		if (mergeClusters && false) {

			int mCnt = 0;
			int numC = clusterPts.length;

			for (int i = 0; i < numC; i++) {
				for (int j = i; j < numC; j++) {
					if (euclideanDistance(clusterPts[i], clusterPts[j]) < mergeThreshold) {

						// Replace with good cluster from end of array
						clusterPts[numC - 1 - mCnt] = clusterPts[i];
						oldClusterPts[numC - 1 - mCnt] = oldClusterPts[i];
						clusterAsgn[numC - 1 - mCnt] = clusterAsgn[i];

						// Increment bad cluster count
						mCnt++;

					}
				}
			}

			chopBad(mCnt);

		}

	}

	/**
	 * Chops the last entries from the old and new cluster point arrays, as well
	 * as the cluster-data assignment array. This allows arrays to be shrunk by
	 * swapping bad values to the back.
	 * 
	 * @param numBad
	 *            Number of entries to chop from ends
	 * 
	 */
	private void chopBad(int numBad) {

		// Rebuild the arrays if we dumped anything
		if (numBad > 0) {

			// Grab some dimension info
			int numC = clusterPts.length;
			int numD = clusterPts[0].length;

			// Create shrunk down cluster pt/assgn vars
			double purgedCPts[][] = new double[numC - numBad][numD];
			double purgedOCPts[][] = new double[numC - numBad][numD];
			Vector purgedCAsgn[] = new Vector[numC - numBad];

			// Copy over old data
			for (int i = 0; i < numC - numBad; i++) {
				purgedCPts[i] = clusterPts[i];
				purgedOCPts[i] = oldClusterPts[i];
				purgedCAsgn[i] = clusterAsgn[i];
			}

			// Nuke old vars with new ones
			clusterPts = purgedCPts;
			oldClusterPts = purgedCPts;
			clusterAsgn = purgedCAsgn;

		}

	}

	/**
	 * Calculates the euclidean distance between two points. The points are
	 * represented as two matrices, which had darn better be the same size.
	 * 
	 * @param a
	 *            n-vector representing a point
	 * @param b
	 *            n-vector representing a point
	 * 
	 * @return Euclidean distance between points
	 * 
	 */
	private double euclideanDistance(double[] a, double[] b) {

		double sum = 0.0;

		for (int i = 0; i < a.length; i++) {

			sum += (a[i] - b[i]) * (a[i] - b[i]);

		}

		return Math.sqrt(sum);

	}

	/**
	 * Vector-vector addition. <a> = <a> + <b>
	 * 
	 * @param a
	 *            n-vector
	 * @param b
	 *            n-vector
	 * 
	 */
	private void vec_vecAdd(double[] a, double[] b) {
		for (int i = 0; i < a.length; i++) {
			a[i] = a[i] + b[i];
		}
	}

	/**
	 * Vector-vector assignment. Sets <a> = <b>.
	 * 
	 * @param a
	 *            n-vector
	 * @param b
	 *            n-vector
	 * 
	 */
	private void vec_vecAssign(double[] a, double[] b) {
		for (int i = 0; i < a.length; i++) {
			a[i] = b[i];
		}
	}

	/**
	 * Vector-scalar multiplication. Sets <A> = b<a>.
	 * 
	 * @param a
	 *            n-vector
	 * @param b
	 *            scalar
	 * 
	 */
	private void vec_sclMult(double[] a, double b) {
		for (int i = 0; i < a.length; i++) {
			a[i] = a[i] * b;
		}
	}

	/**
	 * Sets all elements of a vector equal to given scalar.
	 * 
	 * @param a
	 *            n-vector
	 * @param b
	 *            scalar
	 * 
	 */
	private void vec_sclSet(double[] a, double b) {
		for (int i = 0; i < a.length; i++) {
			a[i] = b;
		}
	}

	/**
	 * Quick and dirty tester program.
	 * 
	 * @param args
	 *            Command line arguments
	 * 
	 */
	public static void main(String args[]) {

		if (args.length != 3) {

			System.out
					.println("\nUsage: java ISODATA <num pts> <dims> <num clusters>\n");

		} else {

			int pts = Integer.parseInt(args[0]);
			int ptDims = Integer.parseInt(args[1]);
			int clsts = Integer.parseInt(args[2]);

			ISODATA isodataAlgorithm = new ISODATA();

			double dataPts[][] = new double[pts][ptDims];

			for (int i = 0; i < dataPts.length; i++) {
				for (int j = 0; j < ptDims; j++) {
					dataPts[i][j] = Math.random();
					dataPts[i][j] = Math.random();
				}
			}

			// Print data pts
			for (int j = 0; j < dataPts[0].length; j++) {
				for (int i = 0; i < dataPts.length; i++) {

					System.out.print("" + dataPts[i][j] + " ");

				}
				System.out.print("\n");
			}
			System.out.print("\n");

			// Calculate clusters
			// double[][] foo = isodataAlgorithm.generateClasses( dataPts, clsts
			// );
			//     
			// // Print clusters
			// for ( int j = 0; j < foo[ 0 ].length; j++ ) {
			// for ( int i = 0; i < foo.length; i++ ) {
			//     
			// System.out.print( "" + foo[ i ][ j ] + " " );
			//     
			// }
			// System.out.print( "\n" );
			// }

		}

	}

} // ISODATA
