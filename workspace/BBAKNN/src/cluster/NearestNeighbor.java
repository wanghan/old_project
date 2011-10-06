/**
 * 
 */
package cluster;

/**
 * @author wanghan
 * 
 */
public class NearestNeighbor implements Comparable<NearestNeighbor> {
	Point point;

	double distance;

	@Override
	public int compareTo(NearestNeighbor o) {
		// TODO Auto-generated method stub
		if (distance > o.distance) {
			return -1;
		} else {
			return 1;
		}
	}

	public NearestNeighbor(Point point, double dis) {
		// TODO Auto-generated constructor stub
		this.point = point;
		distance = dis;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}