package utils;

import cluster.Point;

public class EuclideanDistance {

	public static double euclideanDistance(float[] a, float[] b) {

		double sum = 0.0;

		for (int i = 0; i < a.length; i++) {

			sum += (a[i] - b[i]) * (a[i] - b[i]);

		}

		return Math.sqrt(sum);

	}

	public static double euclideanDistance(Point a, Point b) {

		return euclideanDistance(a.coordinates, b.coordinates);

	}

}
