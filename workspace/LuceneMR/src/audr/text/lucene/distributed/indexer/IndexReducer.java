/**
 * 
 */
package audr.text.lucene.distributed.indexer;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.filesystem.Shard;
import org.apache.filesystem.ShardWriter;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


/**
 * @author wanghan
 *
 */
public class IndexReducer extends Reducer<Shard, IndexIntermediateForm, Shard, Text> {
	private static final Log LOG = LogFactory.getLog(IndexReducer.class);
	
	public String mapredTempDir;
	private static boolean ISINITIAL=false;
	public static final Text DONE = new Text("done");
	@Override
	protected void reduce(Shard key, Iterable<IndexIntermediateForm> value,
			Context context) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		LOG.info("Construct a shard writer for " + key);
		
		FileSystem fs=FileSystem.get(context.getConfiguration());
		
		mapredTempDir=context.getConfiguration().get("mapred.temp.dir"); 
		
		//copy original index of this shard
		String temp2=null;
		if(!ISINITIAL){
			temp2 =
		        Shard.normalizePath(mapredTempDir) + Path.SEPARATOR + "shard_" + System.currentTimeMillis();
			Path perm = new Path(key.getDirectory());
			FileStatus[] fileStatus = fs.listStatus(perm);
			LOG.info("Count: "+fileStatus.length);
			for (FileStatus fileStatus2 : fileStatus) {
				LOG.info(fileStatus2.getPath().getName());
				fs.copyToLocalFile(fileStatus2.getPath(), new Path(temp2,fileStatus2.getPath().getName()));
			}
		}
		
		
		String temp =
	        Shard.normalizePath(mapredTempDir) + Path.SEPARATOR + "shard_" + System.currentTimeMillis();
		final ShardWriter writer = new ShardWriter(fs, key, temp,temp2, context.getConfiguration());

	    // update the shard
		if(writer!=null){
			for (IndexIntermediateForm intermediateForm : value) {
				writer.process(intermediateForm);
			}
		}
		
		if (writer != null) {
            writer.close();
         }
		context.write(key, DONE);
	}
	
	
}
