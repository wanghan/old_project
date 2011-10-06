/**
 * 
 */
package cluster;

import java.awt.geom.Ellipse2D;

/**
 * @author wanghan
 * 
 */
public class Point {
	public float[] coordinates;

	public Point(float[] value) {
		// TODO Auto-generated constructor stub
		coordinates = new float[value.length];
		for (int i = 0; i < value.length; ++i) {
			coordinates[i] = value[i];
		}
	}

	public Ellipse2D get2DShape() {
		return new Ellipse2D.Double(coordinates[0], coordinates[1], 5, 5);
	}

	public Ellipse2D get2DShape(int size) {
		return new Ellipse2D.Double(coordinates[0], coordinates[1], size, size);
	}
}
