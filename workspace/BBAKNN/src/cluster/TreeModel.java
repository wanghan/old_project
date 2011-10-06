/**
 * 
 */
package cluster;

import java.util.ArrayList;

/**
 * @author wanghan
 * 
 */
public class TreeModel {

	ClusterModel node;
	ArrayList<TreeModel> chilren;
	int level;

	public TreeModel(ClusterModel node, int level) {
		// TODO Auto-generated constructor stub
		this.node = node;
		chilren = new ArrayList<TreeModel>();
		this.level = level;
	}

	public TreeModel() {
		// TODO Auto-generated constructor stub
		chilren = new ArrayList<TreeModel>();
		this.level = 0;
	}

	public ClusterModel getNode() {
		return node;
	}

	public ArrayList<TreeModel> getChilren() {
		return chilren;
	}

	public int getLevel() {
		return level;
	}

}
