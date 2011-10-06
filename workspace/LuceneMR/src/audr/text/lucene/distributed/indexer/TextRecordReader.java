package audr.text.lucene.distributed.indexer;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class TextRecordReader extends RecordReader<IntWritable, TextInputSplit> {

	private static int id = 1;
	private boolean readOver = false;
	private TextInputSplit split = null;
	private IntWritable key = null;
	private TextInputSplit value = null;

	private static final Log LOG = LogFactory.getLog(TextRecordReader.class);

	@Override
	public void initialize(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {
		this.readOver = false;
		this.split = (TextInputSplit) split;
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		if (readOver) {
			return false;
		}

		value = (TextInputSplit) split;
		key = new IntWritable(id++);

		readOver = true;

		LOG.info("Next Key:" + key + ",value:" + value);
		return true;
	}

	@Override
	public IntWritable getCurrentKey() throws IOException, InterruptedException {
		LOG.info("return key:" + key);
		return key;
	}

	@Override
	public TextInputSplit getCurrentValue() throws IOException,
			InterruptedException {
		LOG.info("return value:" + value);
		return value;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		if (readOver) {
			return 1.0f;
		} else {
			return 0.0f;
		}
	}

	@Override
	public void close() throws IOException {
		// do nothing
	}
}