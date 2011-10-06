package cluster;

import java.util.ArrayList;

import utils.EuclideanDistance;

public class ClusterModel {

	public ArrayList<Point> points;

	public Point center;

	public ClusterModel() {
		points = new ArrayList<Point>();
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	public void updateCenter() {
		int length = center.coordinates.length;
		float newCenter[] = new float[length];
		for (int i = 0; i < points.size(); ++i) {
			for (int j = 0; j < length; ++j) {
				newCenter[j] += points.get(i).coordinates[j];
			}
		}
		for (int j = 0; j < length; ++j) {
			newCenter[j] /= points.size();
		}
		center = new Point(newCenter);
	}

	public Point getCenterPoint() {
		int length = center.coordinates.length;
		float newCenter[] = new float[length];
		for (int i = 0; i < points.size(); ++i) {
			for (int j = 0; j < length; ++j) {
				newCenter[j] += points.get(i).coordinates[j];
			}
		}
		for (int j = 0; j < length; ++j) {
			newCenter[j] /= points.size();
		}
		return new Point(newCenter);
	}

	public double getFarestDis() {
		double max = Double.MIN_VALUE;
		for (Point point : points) {
			double dis = EuclideanDistance.euclideanDistance(point, center);
			if (dis > max) {
				max = dis;
			}
		}
		return max;
	}
}
