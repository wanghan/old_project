/**
 * 
 */
package audr.text.lucene.distributed.indexer;


/**
 * @author wanghan
 *
 */
public class ModingDistributionPolicy {
	  private int numShards;

	  public ModingDistributionPolicy(int num) {
		// TODO Auto-generated constructor stub
		  this.numShards=num;
	  }
	  
	  public int chooseShardForInsert(int key) {
		  return key%numShards;
	  }

	  public int chooseShardForDelete(int key) {
		  return key%numShards;
	  }

}
