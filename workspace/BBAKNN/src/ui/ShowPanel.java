package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import cluster.ClusterModel;
import cluster.NearestNeighbor;
import cluster.Point;

public class ShowPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	
	private Color[] COLORS=new Color[]{Color.red,
			Color.BLUE,
			Color.GREEN,
			Color.CYAN,
			Color.PINK,
			Color.ORANGE,
			Color.YELLOW};
	
	private ArrayList<ClusterModel> clusters;
	private Point x;
	private ArrayList<Point> dataPoints;
	private ArrayList<NearestNeighbor> nearestNeighbors;
	public ShowPanel(Point x,ArrayList<ClusterModel> clusters,ArrayList<NearestNeighbor> nearestNeighbors,int a) {
		this.clusters=clusters;
		this.x=x;
		this.nearestNeighbors=nearestNeighbors;
		this.dataPoints=null;
	}
	public ShowPanel(Point x, ArrayList<Point> points,ArrayList<NearestNeighbor> nearestNeighbors) {
		this.clusters=null;
		this.x=x;
		this.dataPoints=points;
		this.nearestNeighbors=nearestNeighbors;
	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setPaint(Color.orange);
		g2.fill(x.get2DShape(12));
		if(clusters!=null){
			
			if(nearestNeighbors!=null){
				g2.setPaint(Color.BLACK);
				for (NearestNeighbor nn : nearestNeighbors) {
					g2.fill(nn.getPoint().get2DShape(8));
				}
			}
			
			for(int i=0;i<clusters.size();++i){
				int number=clusters.get(i).points.size();
				g2.setPaint(COLORS[i]);
				g2.fill(clusters.get(i).center.get2DShape(10));
				for(int j=0;j<number;++j){
					g2.fill(clusters.get(i).points.get(j).get2DShape());
				}
				
				
			}
		}
		else{
			for (Point point : dataPoints) {
				g2.fill(point.get2DShape());
			}
			g2.setPaint(Color.red);
			g2.fill(x.get2DShape(10));
			g2.setPaint(Color.BLUE);
			for (NearestNeighbor nn : nearestNeighbors) {
				g2.fill(nn.getPoint().get2DShape(8));
			}
		}
		
	}

}
