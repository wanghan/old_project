/**
 * 
 */
package audr.text.lucene.distributed.indexer;

import java.io.IOException;

import org.apache.filesystem.Shard;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


/**
 * @author wanghan
 * 
 */
public class TextOutputFormat extends FileOutputFormat<Shard, Text> {
	@Override
	public RecordWriter<Shard, Text> getRecordWriter(TaskAttemptContext job)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		final Path perm = getOutputPath(job);
		final FileSystem fs = FileSystem.get(job.getConfiguration());

		return new RecordWriter<Shard, Text>() {
			public void write(Shard key, Text value) throws IOException {
				assert (IndexReducer.DONE.equals(value));

				String shardName = key.getDirectory();
				shardName = shardName.replace("/", "_");

				Path doneFile = new Path(perm, IndexReducer.DONE + "_"
						+ shardName);
				if (!fs.exists(doneFile)) {
					fs.createNewFile(doneFile);
				}
			}

			@Override
			public void close(TaskAttemptContext context) throws IOException,
					InterruptedException {
				// TODO Auto-generated method stub

			}
		};
	}

}
