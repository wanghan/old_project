/**
 * 
 */
package audr.text.lucene.distributed.indexer;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.filesystem.Shard;
import org.apache.hadoop.mapreduce.Reducer;


/**
 * @author wanghan
 *
 */
public class IndexCombiner extends Reducer<Shard, IndexIntermediateForm, Shard, IndexIntermediateForm>{

	private static final Log LOG = LogFactory.getLog(IndexCombiner.class);
	
	@Override
	protected void reduce(Shard key, Iterable<IndexIntermediateForm> values,
			Context context) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		

		
		IndexIntermediateForm form = new IndexIntermediateForm();
		
		for (IndexIntermediateForm intermediateForm : values) {
			form.process(intermediateForm);
		}
		form.closeWriter();
		LOG.info("Closed the form writer for " + key + ", form = " + form);
		context.write(key, form);
		
	}
}
