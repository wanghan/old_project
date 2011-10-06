package audr.text.lucene.distributed.indexer;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.filesystem.FileSystemDirectory;
import org.apache.filesystem.LuceneUtil;
import org.apache.filesystem.Shard;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import audr.text.lucene.fields.TextCategoryFields;
import audr.text.utils.Constants;

public class IndexRestorer {
	

	private static final Log LOG = LogFactory.getLog(IndexMRDriver.class);
	public int  StartJob(String hadoopInputPath, String hadoopLFInputPath,Configuration conf,String category) throws Exception {
		
		// 用Map保存fileName 和 HBaseID的对应关系
		HashMap<String,String> IDFileNameMap = new HashMap<String,String>();
		HashMap<String,String> OriFileNameMap=new HashMap<String, String>();
		HashMap<String,String> OriFilePathMap=new HashMap<String, String>();
		//创建Job并执行
	    
	    FileSystem fs=FileSystem.get(conf);
		
    	
		String hadoopOutputPath = Constants.OUTPUT_PATH + String.valueOf(System.currentTimeMillis())+"/";
		

    	int num=0;
		Path oridir=new Path(hadoopInputPath);
		for (FileStatus orifile : fs.listStatus(oridir)) {
			
			
			String oriFileName = orifile.getPath().getName();
			String id=oriFileName.substring(0,oriFileName.lastIndexOf('.'));
			String lffilename=hadoopLFInputPath+"/"+id+".txt";
			if(fs.exists(new Path(lffilename))){
				num++;
	    		IDFileNameMap.put(id+".txt",id);
	    		OriFileNameMap.put(id+".txt", oriFileName);
	    		OriFilePathMap.put(id+".txt", hadoopInputPath+"/"+oriFileName);
			}

		}
    	
    	TextInputFormat.IDFilePathMap=IDFileNameMap;
    	TextInputFormat.IDOriFileNameMap=OriFileNameMap;
    	TextInputFormat.IDOriFilePathMap=OriFilePathMap;
    	
    	TextInputFormat.MAX_SPLIT_NUM=num/Constants.DIVIDER;
    	
		String[] indexDirs=Shard.getIndexShardsByCategory(category);
		
		Shard[] shards=new Shard[indexDirs.length];
		for(int i=0;i<shards.length;++i){
			shards[i]=new Shard(-1,indexDirs[i],-1);
		}
		
    	Path[] inputpaths=new Path[1];
    	inputpaths[0]=new Path(hadoopLFInputPath);
    	
    	

	    Shard.setIndexShards(conf, shards);
	    conf.set("OutputPath", hadoopOutputPath);
	    LOG.info("Create Index Job");
	    Job job = createIndexJob(conf, inputpaths,new Path(hadoopOutputPath),shards);
	    job.waitForCompletion(true);
	   
	    System.out.println(num);
	    return num;
	}
	
	/**
	   * Sets up the actual job.
	   * 
	   * @param conf  The current configuration.
	   * @param args  The command line parameters.
	   * @return The newly created job.
	   * @throws IOException When setting up the job fails.
	   */
	private Job createIndexJob(Configuration conf, Path[] inputPaths, Path outputPath,Shard[] shards) throws IOException {
	    
		setShardGeneration(conf, shards);
		
		Job job=new Job(conf,this.getClass().getName() + "_"+ System.currentTimeMillis());
	   

	    
	    job.setInputFormatClass(TextInputFormat.class);
	    TextInputFormat.setInputPaths(job, inputPaths);
	    
	    
	    job.setOutputFormatClass(TextOutputFormat.class);
	    TextOutputFormat.setOutputPath(job, outputPath);
	    job.setMapperClass(IndexMapper.class);

	    job.setOutputKeyClass(Shard.class);
	    job.setOutputValueClass(Text.class);

	    job.setNumReduceTasks(shards.length);
	    
	    job.setMapOutputKeyClass(Shard.class);
	    job.setMapOutputValueClass(IndexIntermediateForm.class);
	  
	    job.setCombinerClass(IndexCombiner.class);
	    job.setReducerClass(IndexReducer.class);

	    
	    return job;
	}
	void setShardGeneration(Configuration conf, Shard[] shards)
			throws IOException {
		FileSystem fs = FileSystem.get(conf);

		for (int i = 0; i < shards.length; i++) {
			Path path = new Path(shards[i].getDirectory());
			long generation = -1;

			if (fs.exists(path)) {
				FileSystemDirectory dir = null;

				try {
					dir = new FileSystemDirectory(fs, path, false, conf);
					generation = LuceneUtil.getCurrentSegmentGeneration(dir);
				} finally {
					if (dir != null) {
						dir.close();
					}
				}
			}

			if (generation != shards[i].getGeneration()) {
				// set the starting generation for the shard
				shards[i] = new Shard(shards[i].getVersion(), shards[i]
						.getDirectory(), generation);
			}
		}
	}
	public static void main(String[] args) {
		try {
			String category=TextCategoryFields.SPORT;
			Configuration conf=new Configuration();  
			String hadoopLFInputPath = Constants.INPUT_PATH_LF.replace("%Category%", category) ;
	    	String hadoopInputPath = Constants.INPUT_PATH.replace("%Category%", category) ;
	    	Path oriroot=new Path(hadoopInputPath);
	    	FileSystem fs=FileSystem.get(conf);
	    	int total=0;
	    	for (FileStatus dir: fs.listStatus(oriroot)) {
				if(dir.isDir()){
					
					total+=new IndexRestorer().StartJob(dir.getPath().toString(),hadoopLFInputPath+dir.getPath().getName(),new Configuration(),category);
					Thread.sleep(15*60*1000);
				}
				System.out.println(total);
	    	}
	    	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
