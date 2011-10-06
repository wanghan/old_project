package ui;

import java.awt.EventQueue;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import utils.DatasetGenerator;

import cluster.ClusterModel;
import cluster.NearestNeighbor;
import cluster.Point;
import cluster.TreeModel;
import cluster.TreeSearch;

public class ShowFrame {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	private int WIDTH=800;
	private int HIGHTH=800;
	private ArrayList<ClusterModel> clusters;
	private Point x;
	private ArrayList<Point> dataPoints;
	private ArrayList<NearestNeighbor> nearestNeighbors;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
//					int pts = 1000;
//			        int ptDims = 2;
//			        int clsts = 25;
//
//			        ISODATAClustering isodataAlgorithm = new ISODATAClustering();
//			 
//			        double dataPts[][] = new double[ pts ][ ptDims ];
//			        Point points[]=new Point[pts];
//			        for ( int i = 0; i < dataPts.length; i++ ) {
//			            for ( int j = 0; j < ptDims; j++ ) {
//			                dataPts[ i ][ j ] = Math.random()*800;
//			                dataPts[ i ][ j ] = Math.random()*800;
//			            }
//			            points[i]=new Point(dataPts[i]);
//			        }
//			 
//			        // Print data pts
//			        for ( int j = 0; j < dataPts[ 0 ].length; j++ ) {
//			            for ( int i = 0; i < dataPts.length; i++ ) {
//			 
//			                System.out.print( "" + dataPts[ i ][ j ] + " " );
//			 
//			            }
//			            System.out.print( "\n" );
//			        }
//			        System.out.print( "\n" );
//			        
//			        final ArrayList<ClusterModel> results=isodataAlgorithm.clustering(points, clsts);
//					ShowFrame window = new ShowFrame(results);
//					window.frame.setVisible(true);
					
					System.out.println(DatasetGenerator.Norm_rand(0, 1));
					int pts = 1000;
			        int ptDims = 2;
			        float dataPts[][] = new float[ pts ][ ptDims ];
			        ArrayList<Point> points=new ArrayList<Point>();
			        for ( int i = 0; i < dataPts.length; i++ ) {
			            for ( int j = 0; j < ptDims; j++ ) {
			                dataPts[ i ][ j ] = (float)Math.random()*800;
			                dataPts[ i ][ j ] = (float)Math.random()*800;
			            }
			            points.add(new Point(dataPts[i]));
			        }
			        Point x=new Point(new float[]{(float)Math.random()*800,(float)Math.random()*800});
					TreeSearch search=new TreeSearch(5);
					search.search(points,x);
					ShowFrame window_result = new ShowFrame(x,search.getRoot().getChilren(),1);
					window_result.frame.setVisible(true);
					
//					BruteKNN bruteKNN=new BruteKNN(5);
//					bruteKNN.search(points,x);
//					
//					ShowFrame window_result2 = new ShowFrame(x,points,bruteKNN.getNNs());
//					window_result2.frame.setName("2");
//					window_result2.frame.setVisible(true);
//					
					
//					TreeModel root=search.getRoot();
//					traverse(root);
					
			//		ShowFrame window_cluster = new ShowFrame(x,points,search);
			//		window_cluster.frame.setVisible(true);
				} catch (Exception e) { 
					e.printStackTrace();
				}
			}
		});
	}
//	public static void traverse(TreeModel root){
//		if(root==null)
//			return;
//		
//		ArrayList<ClusterModel> clustersInThisLevel=new ArrayList<ClusterModel>();
//		for (TreeModel child : root.getChilren()) {
//			if(child==null)
//				return ;
//			clustersInThisLevel.add(child.getNode());
//			traverse(child);
//		}
//		ShowFrame window_result = new ShowFrame(clustersInThisLevel);
//		window_result.frame.setVisible(true);
//	}
	/**
	 * Create the application.
	 */
	public ShowFrame(Point x, ArrayList<TreeModel> points,ArrayList<NearestNeighbor> nearestNeighbors) {
		this.x=x;
		this.clusters=new ArrayList<ClusterModel>();
		for (TreeModel treeModel : points) {
			this.clusters.add(treeModel.getNode());
		}
		this.nearestNeighbors=nearestNeighbors;
		initialize();
	}
	
	
	public ShowFrame(Point x,ArrayList<ClusterModel> clusters) {
		this.clusters=clusters;
		this.x=x;
		this.dataPoints=null;
		initialize();
	}
	public ShowFrame(Point x,ArrayList<TreeModel> cluster,int a) {
		this.clusters=new ArrayList<ClusterModel>();
		for (TreeModel treeModel : cluster) {
			this.clusters.add(treeModel.getNode());
		}
		this.x=x;
		this.dataPoints=null;
		this.nearestNeighbors=null;

		initialize();
	}
	public ShowFrame() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, WIDTH, HIGHTH);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel;
		if(clusters==null){
			panel=new ShowPanel(x,dataPoints,nearestNeighbors);
		}
		else{
			panel = new ShowPanel(x,clusters,nearestNeighbors,1);
		}
		
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		
	}

	public JFrame getFrame() {
		return frame;
	}
	
	

}
