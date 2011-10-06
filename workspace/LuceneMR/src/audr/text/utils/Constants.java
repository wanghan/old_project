/**
 * 
 */
package audr.text.utils;

/**
 * @author wanghan
 *
 */
public interface Constants {
	public int RETURN_NUM=50;
	public int SHARD_RETURN_NUM=25;
	
	public int DIVIDER=5;
	
	public String TEMP_DIR="D:/Data/temp/";
	
	public String[] INDEX_SHARDS={
			"/user/audr/text/input/%Category%/index1/",
			"/user/audr/text/input/%Category%/index2/",
			"/user/audr/text/input/%Category%/index3/",
	};

	
	public static final String INPUT_PATH_LF = "/user/audr/text/input/%Category%/lf/";
	public static final String INPUT_PATH = "/user/audr/text/input/%Category%/originalFiles/";
	public static final String OUTPUT_PATH = "/user/audr/text/output/%Category%/";
	
}
