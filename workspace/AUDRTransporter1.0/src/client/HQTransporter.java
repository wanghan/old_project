/**
 * 
 */
package client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


import sys.QueryResult;

/**
 * including all interfaces invoked by HQ
 * @author wanghan
 *
 */
public class HQTransporter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File f1=new File("./Chocolate Cake.bmp");
		File f2=new File("./Chocolate Cake.bmp");
		File files[]=new File[2];
		files[0]=f1;
		files[1]=f2;
		System.out.println(1);

	}
	/**
	 * query method, by lire ID
	 * @param lireid  Lire ID
	 * @param tableName Table Name
	 * @return
	 */
	public QueryResult queryByLireID(String[] lireid,String[] tableName){
		
		QueryByLireIDSenderThread thread=null;
		try {
			Socket client=new Socket(sys.SystemInstances.LIRE_IP,sys.SystemInstances.LireQueryServPort);
			thread=new QueryByLireIDSenderThread(lireid,tableName,client);
			thread.start();
			thread.join();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return thread.result;
	}
	
	/**
	 * the api invoked by the HQ
	 * query image from lire
	 * @param URL
	 * @param type
	 * @return quert result
	 */
	public QueryResult searchFromLire(File URL, String type,String imageType){
		QueryByURLSenderThread thread=null;
		try {
			Socket client=new Socket(sys.SystemInstances.LIRE_IP,sys.SystemInstances.LireQueryServPort);
			String filepath=new FileTransporter().sendFile(sys.SystemInstances.LIRE_IP,sys.SystemInstances.FILE_SERV_PORT,URL,sys.SystemInstances.LIRE_FILE_CACHE_DIR);		
			thread=new QueryByURLSenderThread(filepath,type,imageType,client);
			thread.start();
			thread.join();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return thread.result;
	}
	/**
	 *  the api invoked by the HQ
	 *  insert a image into the lire
	 * @param URL
	 * @param existID
	 * @return
	 */
	public String insertImage(File[] URL,String existID[],String imageType){
		InsertSenderThread thread=null;
		try {
			Socket client=new Socket(sys.SystemInstances.LIRE_IP,sys.SystemInstances.LireInsertServPort);
			String filepaths[]=new FileTransporter().sendFiles(sys.SystemInstances.LIRE_IP,sys.SystemInstances.FILE_SERV_PORT,URL,sys.SystemInstances.LIRE_FILE_CACHE_DIR);
			thread=new InsertSenderThread(filepaths,existID,imageType,client);
			thread.start();
			thread.join();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return thread.getResult();
	}
	/**
	 * the thread used for send insert infomation to Lire
	 * 
	 * @author wanghan
	 */
	class InsertSenderThread extends Thread{
		private String[] url;
		private Socket client;
		private String existID[];
		private String imageType;
		private String result=null;
		public InsertSenderThread(String[] URL,String[] existid,String imageType,Socket socket) {
			// TODO Auto-generated constructor stub
			this.url=URL;
			this.client=socket;
			this.existID=existid;
			this.imageType=imageType;
		}
		public String getResult(){
			return result;
		}
		@Override
		public void run(){
			// TODO Auto-generated method stub
			try {
				DataOutputStream dos=new DataOutputStream(client.getOutputStream());
				//send urls
				//send array length first
				int len1=url.length;
				dos.writeInt(len1);
				dos.flush();
				for(int i=0;i<len1;++i){
					dos.writeUTF(url[i]);
					dos.flush();
				}
				//send existID
				//send array length first
				int len2=existID.length;
				dos.writeInt(len2);
				dos.flush();
				for(int i=0;i<len2;++i){
					dos.writeUTF(existID[i]);
					dos.flush();
				}
				dos.writeUTF(imageType);
				dos.flush();
				
				//wait lire return insert result
				
				DataInputStream dis=new DataInputStream(client.getInputStream());
				//receive the result length first
				result=dis.readUTF();

				dis.close();
				dos.close();
				client.close();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}
	/**
	 * the thread used for send query information
	 * 
	 * @author wanghan
	 */
	class QueryByURLSenderThread extends Thread{
		private String url;
		private String type;
		private Socket client;
		private String imageType;
		public  QueryResult result;// return to the caller as the query operation result 
		public QueryByURLSenderThread(String URL, String type,String imageType,Socket socket) {
			// TODO Auto-generated constructor stub
			this.type=type;
			this.url=URL;
			this.client=socket;
			this.imageType=imageType;
			result=null;
		}
		@Override
		public void run(){
			// TODO Auto-generated method stub
			try {
				DataOutputStream dos=new DataOutputStream(client.getOutputStream());
				
				//send 1 first means query by url
				dos.writeInt(1);
				dos.flush();
				
				//send paras
				dos.writeUTF(url);
				dos.writeUTF(type);
				dos.writeUTF(imageType);
				dos.flush();
				//waiting and receive result
				
				DataInputStream dis=new DataInputStream(client.getInputStream());
				int len3=dis.readInt();
				File images[]=new File[len3];
				File fullimages[]=new File[len3];
				
				for(int i=0;i<len3;++i){
					//receive the file path , then find the file at the local machine
					String smallfilepath=dis.readUTF();
					String fullfilepath=dis.readUTF();
					images[i]=new File(smallfilepath);
					fullimages[i]=new File(fullfilepath);
					
				}
				
				int len4=dis.readInt();
				String lireID[]=new String[len4];
				for(int i=0;i<len4;++i){
					lireID[i]=dis.readUTF();
				}
				result=new QueryResult(images,fullimages,lireID);
				dos.close();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * the thread used for send query(by exist) information
	 * 
	 * @author wanghan
	 */
	class QueryByLireIDSenderThread extends Thread{
		private String[] lireID;
		private Socket client;
		private String[] tableName;
		public  QueryResult result;// return to the caller as the query operation result 
		public QueryByLireIDSenderThread(String [] lireID,String[] tableName,Socket socket) {
			// TODO Auto-generated constructor stub
			this.lireID=lireID;
			this.tableName=tableName;
			this.client=socket;
			result=null;
		}
		@Override
		public void run(){
			// TODO Auto-generated method stub
			try {
				DataOutputStream dos=new DataOutputStream(client.getOutputStream());
				
				//send 0 first means query by exist
				dos.writeInt(0);
				dos.flush();
				//send array , first send length
				dos.writeInt(lireID.length);
				for(int i=0;i<lireID.length;++i){
					dos.writeUTF(lireID[i]);
					dos.flush();
				}
				dos.writeInt(tableName.length);
				for(int i=0;i<tableName.length;++i){
					dos.writeUTF(tableName[i]);
					dos.flush();
				}
				//waiting and receive result
				
				DataInputStream dis=new DataInputStream(client.getInputStream());
				int len3=dis.readInt();
				File images[]=new File[len3];
				File fullimages[]=new File[len3];
				
				for(int i=0;i<len3;++i){
					//receive the file path , then find the file at the local machine
					String smallfilepath=dis.readUTF();
					String fullfilepath=dis.readUTF();
					images[i]=new File(smallfilepath);
					fullimages[i]=new File(fullfilepath);
					
				}
				
				
				int len4=dis.readInt();
				String lireID[]=new String[len4];
				for(int i=0;i<len4;++i){
					lireID[i]=dis.readUTF();
				}
				result=new QueryResult(images,fullimages,lireID);
				dos.close();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}
}
