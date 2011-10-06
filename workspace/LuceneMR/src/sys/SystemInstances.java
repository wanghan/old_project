/**
 * 定义一些常用的全局变量..
 */
package sys;

/**
 * @author wanghan
 *
 */
public interface SystemInstances {
	
public static int POOL_SIZE=1000;	//线程池大小
	
	//1、图像部分：
	public static int LireInsertServPort=7101;	        //lire端 insert服务的端口
	public static int HQUserInsertServPort=7101;	    //总控端监听用户insert服务的端口
	public static int LireQueryServPort=7102;	        //lire端 query服务的端口
	public static int HQUserQueryServPort=7102;    //总控端监听用户query服务的端口
	public static int LireClusterServPort = 7103;    //lire端 cluster服务的端口
	public static int HQUserClusterServPort=7103;	//总控端监听用户cluster服务的端口
	public static int LireClassificationServPort = 7104;  //lire端classification服务端口
	public static int HQUserClassificationServPort=7104;	//总控端监听用户classification服务的端口

	//2、文本部分：
	public static int LuceneInsertServPort=7201;       //Lucene端 insert服务的端口
	public static int HQLuceneInsertServPort=7201;   //总控端监听用户insert服务的端口
	public static int LuceneQueryServPort=7202;      //lucene端 query服务的端口 
	public static int HQLuceneQueryServPort=7202;  //总控端监听用户query服务的端口

	//3、视频部分：
	public static int VideoInserServPort=7301;       //视频服务器端 insert服务的端口
	public static int HQVideoInsertServPort=7301;    //总控端监听用户insert服务的端口
	public static int VideoQueryServPort=7302;      //视频服务器端 query服务的端口 
	public static int HQVideoQueryServPort=7302;   //总控端监听用户query服务的端口
	public static int VideoReceiveServPort=7303;    //视频服务器端 receive服务的端口
	public static int HQVideoReceiveSerPort=7303;  //总控端监听用户receive服务的端口

	//4、音频部分：
	public static int AudioInsertSerPort=7401;           //audio端insert服务的端口
	public static int HQAudioInsertServPort=7401;    //HQ端监听用户insert服务的端口
	public static int AudioQuerySerPort=7402;           //audio端query服务的端口
	public static int HQAudioQueryServPort=7402;   //HQ端监听用户query服务的端口

	//5、更新功能端口：
	public static int XmlUpdateServPort=7501;   //总控端监听用户modify服务的端口
	public static int HQTypeEvoSerPort=7502;   //总控端监听用户type版本演变的端口

	//6、AQL查询端口
	public static int AqlQuerySerPort=7601;  		//总控端监听aql查询端口

	//7、文件传输端口：
	public static int IMAGE_FILE_SERV_PORT=7701;  //总控端和底层image文件传输端口
	public static int TEXT_FILE_SERV_PORT=7702;    //总控端和底层text文件传输端口
	public static int VIDEO_FILE_SERV_PORT=7703;   //总控端和底层video文件传输端口 
	public static int AUDIO_FILE_SERV_PORT=7704;  //总控端和底层audio文件传输端口
	public static int DATA_SERV_PORT=7705;              //数据传递端口
	public static int FILE_TRNS_PORT=7706;              //文件接收端口

	//8、各个服务器真实环境下的ip：
	public static String HQ_IP="192.168.13.16";	    			//真实总控服务器ip
	public static String AQL_IP="192.168.13.16";	    			//真实AQL服务器ip
	public static String LIRE_IP="192.168.13.15";	    			//真实图像服务器 ip
	public static String LUCENE_IP="192.168.13.15";  			//真实文本服务器ip
	public static String VIDEO_IP="192.168.13.15";          //真实视频服务器ip
	public static String AUDIO_IP="192.168.13.15";          //真实音频服务器ip
	
	
	
	public static int BUFFERSIZE=8192;			        //字节buffer大小
	public static String HQ_USER_FILE_CACHE_DIR=".\\temphq_user\\";  //HQ端临时文件的保存位置
	public static String HQ_LIRE_FILE_CACHE_DIR=".\\temphq_lire\\";  //HQ端临时文件的保存位置
	public static String LIRE_FILE_CACHE_DIR=".\\templire\\";        //LIRE端临时文件的保存位置
	
	public static String LUCENE_FILE_CACHE_DIR=".\\temphq_lucene\\";  //LUCENE端临时文件的保存位置
	public static String USER_FILE_CACHE_DIR=".\\tempuser\\";         //用户机器上的临时文件的保存位置
	
	public static String HQ_VIDEO_FILE_CACHE_DIR=".\\temphq_video\\";  //HQ端临时保存视频文件的位置
	public static String VIDEO_FILE_CACH_DIR=".\\tempvideo\\";        //视频服务器端临时文件保存位置
	
	public static String HQ_AUDIO_FILE_CACHE_DIR=".\\temphq_audio\\";  //HQ端临时保存音频的位置
	public static String AUDIO_FILE_CACH_DIR=".\\tempaudio\\";        //音频服务器端临时文件保存位置
	public static long SENDING_DELAY=5;
	
	public static String NULL_FILE_NAME="NOTHING";
	
	//query type
	public static int QUERY_NORMAL=1;
	public static int QUERY_BY_LIREID=2;
	public static int QUERY_IMAGE_FEATURE=3;
	
}
