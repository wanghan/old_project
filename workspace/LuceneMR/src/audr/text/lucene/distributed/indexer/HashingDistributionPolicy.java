package audr.text.lucene.distributed.indexer;

import org.apache.hadoop.io.Text;

/**
 * @author wanghan
 */
public class HashingDistributionPolicy {

	private int numShards;

	public HashingDistributionPolicy(int num) {
		// TODO Auto-generated constructor stub
		this.numShards = num;
	}

	public int chooseShardForInsert(Text key) {
		int hashCode = key.hashCode();
		return hashCode >= 0 ? hashCode % numShards : (-hashCode) % numShards;
	}

	public int chooseShardForDelete(Text key) {
		int hashCode = key.hashCode();
		return hashCode >= 0 ? hashCode % numShards : (-hashCode) % numShards;
	}

}
