/**
 * 
 */
package audr.text.lucene.distributed.indexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/**
 * @author wanghan
 *
 */
public class TextInputFormat extends FileInputFormat<IntWritable, TextInputSplit>{
	
	public static int MAX_SPLIT_NUM=5;
	public static HashMap<String,String> IDFilePathMap;
	public static HashMap<String,String> IDOriFileNameMap;
	public static HashMap<String,String> IDOriFilePathMap;
	private static final Log LOG = LogFactory.getLog(TextInputFormat.class);
	
	@Override
	public RecordReader<IntWritable, TextInputSplit> createRecordReader(
			InputSplit split, TaskAttemptContext context) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		return new TextRecordReader();
	}
	@Override
	public List<InputSplit> getSplits(JobContext job) throws IOException {
		// TODO Auto-generated method stub
		// generate splits
		List<InputSplit> splitList = new ArrayList<InputSplit>();
    
		ArrayList<Path> paths=new ArrayList<Path>();
		ArrayList<String> IDs=new ArrayList<String>();
		ArrayList<String> filenames=new ArrayList<String>();
		ArrayList<String> filepaths=new ArrayList<String>();
		
		int k=0;
		
		for (FileStatus file: listStatus(job)) {
			
			
			Path path = file.getPath();
			String id = IDFilePathMap.get(path.getName());
			String orifilename = IDOriFileNameMap.get(path.getName());
			String orifilepath = IDOriFilePathMap.get(path.getName());
			
			
			paths.add(path);
			IDs.add(id);
			filenames.add(orifilename);
			filepaths.add(orifilepath);
			
			if(IDs.size()<MAX_SPLIT_NUM){
				;
			}
			else{
				TextInputSplit newSplit = new TextInputSplit(k,IDs,paths,filenames,filepaths);
				splitList.add(newSplit);
				paths=new ArrayList<Path>();
				IDs=new ArrayList<String>();
				filenames=new ArrayList<String>();
				filepaths=new ArrayList<String>();
				k++;
			}
			
			
		}
		if(IDs.size()>0){
			
			TextInputSplit newSplit = new TextInputSplit(k,IDs,paths,filenames,filepaths);
			splitList.add(newSplit);
		}
		if(splitList.size()>0){
			LOG.info("Splits generate successful");
		}
		return splitList;
	}
}
