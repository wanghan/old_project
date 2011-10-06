package audr.text.lucene.distributed.indexer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.filesystem.Shard;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.lucene.document.Document;
import org.wltea.analyzer.lucene.IKAnalyzer;


public class IndexMapper extends org.apache.hadoop.mapreduce.Mapper<IntWritable, TextInputSplit, Shard, IndexIntermediateForm> {

	private ModingDistributionPolicy distributionPolicy;
	private Shard[] shards;
	private static final Log LOG = LogFactory.getLog(IndexMapper.class);
	
	@Override
	protected void map(IntWritable key, TextInputSplit value, Context context)
			throws IOException, InterruptedException {
		
		LOG.info("Begin index Mapping");
		Configuration conf=context.getConfiguration();
		
		shards=Shard.getIndexShards(conf);
		distributionPolicy=new ModingDistributionPolicy(shards.length);
		ArrayList<Path> path = value.getPath();
		ArrayList<String> ID = value.getID();
		ArrayList<String> filename=value.getOrifilename();
		ArrayList<String> filepath=value.getOrifilepath();
		IndexIntermediateForm form = new IndexIntermediateForm();
		LOG.info("ID sizes:"+ID.size());
		for(int i=0;i<ID.size();++i){
			FileSystem fs = FileSystem.get(context.getConfiguration());
			
			Document doc=DocumentMaker.Document(
					ID.get(i),
					filename.get(i),
					filepath.get(i),
					new InputStreamReader(fs.open(path.get(i)),Charset.forName("GBK")),
					path.get(i).toString());
			LOG.info(path.get(i).toString());
	        form.process(doc, new IKAnalyzer());
		}
	        
		
        form.closeWriter();

        int chosenShard = distributionPolicy.chooseShardForInsert(value.getIndex());
        if (chosenShard >= 0) {
            // insert into one shard
        	context.write(shards[chosenShard], form);
        } 
        else {
        	throw new IOException("Chosen shard for insert must be >= 0");
        }
	}
}
