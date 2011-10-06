/**
 * 
 */
package sys;

/**
 * @author wanghan
 *
 */
public interface SystemInstances {
	
	public static int POOL_SIZE=1000;	//线程池大小
//	public static String HQ_IP="192.168.16.166";	//HQ ip
	public static String HQ_IP="127.0.0.1";	//HQ ip
	public static String LIRE_IP="127.0.0.1";	//lire ip
//	public static String LIRE_IP="192.168.16.13";	//lire ip
	public static int LireInsertServPort=7106;	//lire端 insert服务的端口
	public static int LireQueryServPort=7101;	//lire端 query服务的端口
	public static int HQUserInsertServPort=7102;	//HQ端 监听用户insert服务的端口
	public static int HQUserQueryServPort=7103;		//HQ端 监听用户query服务的端口
	public static int HQLireInsertServPort=7109;	//HQ端 监听用户insert服务的端口
	public static int HQLireQueryServPort=7110;		//HQ端 监听用户query服务的端口
//	public static int HQFileServPort=7104;
//	public static int LireFileServPort=7105;
//	public static int UserFileServPort=7107;
	
	public static int FILE_SERV_PORT=7108;		//文件接收服务的端口
		
	public static int BUFFERSIZE=8192;			//字节buffer大小
	
	public static String HQ_USER_FILE_CACHE_DIR="./temphq_user/";  //HQ端临时文件的保存位置
	public static String HQ_LIRE_FILE_CACHE_DIR="./temphq_lire/";  //HQ端临时文件的保存位置
	public static String LIRE_FILE_CACHE_DIR="./templire/"; 	//LIRE端临时文件的保存位置
	public static String USER_FILE_CACHE_DIR="./tempuser/";		//用户机器上的临时文件的保存位置
	
	public static long SENDING_DELAY=5;
	
	public static String NULL_FILE_NAME="NOTHING";
	
	//query type
	public static int QUERY_NORMAL=1;
	public static int QUERY_BY_LIREID=2;
}
