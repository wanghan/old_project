/**
 * 
 */
package sys;

/**
 * @author wanghan
 *
 */
public interface SystemInstances {
	
	public static int POOL_SIZE=1000;	//�̳߳ش�С
//	public static String HQ_IP="192.168.16.166";	//HQ ip
	public static String HQ_IP="127.0.0.1";	//HQ ip
	public static String LIRE_IP="127.0.0.1";	//lire ip
//	public static String LIRE_IP="192.168.16.13";	//lire ip
	public static int LireInsertServPort=7106;	//lire�� insert����Ķ˿�
	public static int LireQueryServPort=7101;	//lire�� query����Ķ˿�
	public static int HQUserInsertServPort=7102;	//HQ�� �����û�insert����Ķ˿�
	public static int HQUserQueryServPort=7103;		//HQ�� �����û�query����Ķ˿�
	public static int HQLireInsertServPort=7109;	//HQ�� �����û�insert����Ķ˿�
	public static int HQLireQueryServPort=7110;		//HQ�� �����û�query����Ķ˿�
//	public static int HQFileServPort=7104;
//	public static int LireFileServPort=7105;
//	public static int UserFileServPort=7107;
	
	public static int FILE_SERV_PORT=7108;		//�ļ����շ���Ķ˿�
		
	public static int BUFFERSIZE=8192;			//�ֽ�buffer��С
	
	public static String HQ_USER_FILE_CACHE_DIR="./temphq_user/";  //HQ����ʱ�ļ��ı���λ��
	public static String HQ_LIRE_FILE_CACHE_DIR="./temphq_lire/";  //HQ����ʱ�ļ��ı���λ��
	public static String LIRE_FILE_CACHE_DIR="./templire/"; 	//LIRE����ʱ�ļ��ı���λ��
	public static String USER_FILE_CACHE_DIR="./tempuser/";		//�û������ϵ���ʱ�ļ��ı���λ��
	
	public static long SENDING_DELAY=5;
	
	public static String NULL_FILE_NAME="NOTHING";
	
	//query type
	public static int QUERY_NORMAL=1;
	public static int QUERY_BY_LIREID=2;
}
