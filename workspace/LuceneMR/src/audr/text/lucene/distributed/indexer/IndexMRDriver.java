package audr.text.lucene.distributed.indexer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.filesystem.FileSystemDirectory;
import org.apache.filesystem.LuceneUtil;
import org.apache.filesystem.Shard;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import audr.text.utils.Constants;
import audr.text.utils.FileUtils;

public class IndexMRDriver {
	

	private static final Log LOG = LogFactory.getLog(IndexMRDriver.class);
	public String  StartJob(String[] ID,String[] lfFilePath,String[] oriFilepath,String category) throws Exception {
		
		// 用Map保存fileName 和 HBaseID的对应关系
		HashMap<String,String> IDFileNameMap = new HashMap<String,String>();
		HashMap<String,String> OriFileNameMap=new HashMap<String, String>();
		HashMap<String,String> OriFilePathMap=new HashMap<String, String>();
		// 上传原始数据到HDFS
		int num = lfFilePath.length;
    	String hadoopLFInputPath = Constants.INPUT_PATH_LF.replace("%Category%", category) + String.valueOf(System.currentTimeMillis())+"/";
    	String hadoopInputPath = Constants.INPUT_PATH.replace("%Category%", category) + String.valueOf(System.currentTimeMillis())+"/";
		String hadoopOutputPath = Constants.OUTPUT_PATH + String.valueOf(System.currentTimeMillis())+"/";
		
    	//10000 	
  //  	String hadoopLFInputPath = INPUT_PATH_LF + "1269871589560/";
  //  	String hadoopInputPath = INPUT_PATH +"1269871589561/";
		
		//80000
	//	String hadoopLFInputPath = Constants.INPUT_PATH_LF + "1269957221438/";
	//	String hadoopInputPath = Constants.INPUT_PATH +"1269957221438/";
    	
		
		System.out.println("Input path: "+hadoopInputPath);
    	long before=System.currentTimeMillis();
    	
    	for(int i=0;i<num;i++) {
    		File file=new File(lfFilePath[i]);
    		String srcPath = file.getAbsolutePath();
    		if(i%500==0){
    			System.out.println("Upload "+i+" File");
    		}
    		
    		String lfFileName = file.getName();
    		String orifilename=new File(oriFilepath[i]).getName(); 
    		String lfsuffix=lfFileName.substring(lfFileName.lastIndexOf('.'));
    		String orifilesuffix=orifilename.substring(orifilename.lastIndexOf('.'));
    		
    		IDFileNameMap.put(ID[i]+lfsuffix,ID[i]);
    		OriFileNameMap.put(ID[i]+lfsuffix, ID[i]+orifilesuffix);
    		OriFilePathMap.put(ID[i]+lfsuffix, hadoopInputPath+ID[i]+orifilesuffix);
    		FileUtils.uploadFile2HDFS(srcPath, hadoopLFInputPath + ID[i]+lfsuffix);
    		FileUtils.uploadFile2HDFS(new File(oriFilepath[i]).getAbsolutePath(), hadoopInputPath+  ID[i]+orifilesuffix);
    	}
    	
    	TextInputFormat.IDFilePathMap=IDFileNameMap;
    	TextInputFormat.IDOriFileNameMap=OriFileNameMap;
    	TextInputFormat.IDOriFilePathMap=OriFilePathMap;
    	
    	TextInputFormat.MAX_SPLIT_NUM=num/Constants.DIVIDER;
    	
    	long after=System.currentTimeMillis();
    	System.out.println("Upload Time:"+(after-before));
    	
		String[] indexDirs=Shard.getIndexShardsByCategory(category);
		
		Shard[] shards=new Shard[indexDirs.length];
		for(int i=0;i<shards.length;++i){
			shards[i]=new Shard(-1,indexDirs[i],-1);
		}
		
    	Path[] inputpaths=new Path[1];
    	inputpaths[0]=new Path(hadoopLFInputPath);
    	
    	//创建Job并执行
	    Configuration conf=new Configuration();  
//	    conf.setStrings("hadoop.job.ugi","xlliu,audrhadoop");
	    Shard.setIndexShards(conf, shards);
	    conf.set("OutputPath", hadoopOutputPath);
	    LOG.info("Create Index Job");
	    Job job = createIndexJob(conf, inputpaths,new Path(hadoopOutputPath),shards);
	    job.waitForCompletion(true);
	    
	    long after_index=System.currentTimeMillis();
	    
	    
	    System.out.println("Index Time:"+(after_index-after));
	    System.out.println("Total Time:"+(after_index-before));
	    return "Insert "+num+" Files,Path: "+hadoopInputPath;
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
}
