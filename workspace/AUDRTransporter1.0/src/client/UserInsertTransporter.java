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

/**
 * interfaces invoked by user point for insert operation
 * 
 * @author wanghan
 *
 */
public class UserInsertTransporter {

	/// 3 kinds interfaces.
	public String sendInsertInfo(File[] url,String imageType){
		return sendInsertInfo(url,null,null,imageType);
	}
	public String sendInsertInfo(File[] url, String [] basicfeature,String imageType){
		return sendInsertInfo(url,basicfeature,null,imageType);
	}
	public String sendInsertInfo(File[] url, File semanticfeature,String imageType){
		return sendInsertInfo(url, null, semanticfeature,imageType);
	}
	public String sendInsertInfo(File[] url, String [] basicfeature, File semanticfeature,String imageType){
		InsertSenderThread thread=null;
		try {
			Socket client=new Socket(sys.SystemInstances.HQ_IP,sys.SystemInstances.HQUserInsertServPort);
			
			if(basicfeature==null&&semanticfeature==null){
				thread=new InsertSenderThread(url,null,null,imageType,client);
			}
			else if(basicfeature==null){
				String filepath=new FileTransporter().sendFile(sys.SystemInstances.HQ_IP,sys.SystemInstances.FILE_SERV_PORT,semanticfeature,sys.SystemInstances.HQ_USER_FILE_CACHE_DIR);
				thread=new InsertSenderThread(url,null,filepath,imageType,client);
			}
			else if(semanticfeature==null){
				thread=new InsertSenderThread(url,basicfeature,null,imageType,client);
			}
			else{
				String filepath=new FileTransporter().sendFile(sys.SystemInstances.HQ_IP,sys.SystemInstances.FILE_SERV_PORT,semanticfeature,sys.SystemInstances.HQ_USER_FILE_CACHE_DIR);		
				thread=new InsertSenderThread(url,basicfeature,filepath,imageType,client);
			}
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
		return thread.resultMessage;
	}
	/**
	 * thread used for sending the insert info the HQ
	 * @author wanghan
	 *
	 */
	class InsertSenderThread extends Thread{
		private File[] url;
		private String[] basicfeature;
		private String semanticFeatureFilePath;
		private Socket client;
		private String imageType;
		public  String resultMessage;  //returned message
		public InsertSenderThread(File[] url, String [] basicfeature, String filePath,String imageType,Socket socket) {
			// TODO Auto-generated constructor stub
			this.basicfeature=basicfeature;
			this.semanticFeatureFilePath=filePath;
			this.url=url;
			this.imageType=imageType;
			this.client=socket;
		}
		@Override
		public void run(){
			// TODO Auto-generated method stub
			try {
				DataOutputStream dos=new DataOutputStream(client.getOutputStream());
				
				String filepaths[]=new FileTransporter().sendFiles(sys.SystemInstances.HQ_IP,sys.SystemInstances.FILE_SERV_PORT,url,sys.SystemInstances.HQ_USER_FILE_CACHE_DIR);
				int len1=filepaths.length;
				dos.writeInt(len1);
				dos.flush();
				
				for(int i=0;i<len1;++i){
					dos.writeUTF(filepaths[i]);
					dos.flush();
				}
				if(basicfeature==null){
					//send \0 means this parameter is null
					dos.writeChar('\0');
					dos.flush();
				}
				else{
					//send \1 means this parameter is not null
					dos.writeChar('\1');
					int len2=basicfeature.length;
					dos.writeInt(len2);
					dos.flush();
					for(int k=0;k<len2;++k){
						dos.writeUTF(basicfeature[k]);
						dos.flush();
					}
				}
				if(semanticFeatureFilePath==null){
					//send \0 means this parameter is null
					dos.writeChar('\0');
					dos.flush();
				}
				else{
					//send \1 means this parameter is not null
					dos.writeChar('\1');
					dos.writeUTF(semanticFeatureFilePath);
					dos.flush();
				}
				
				dos.writeUTF(imageType);
				dos.flush();
				
				//waiting and receive the result
				
				DataInputStream dis=new DataInputStream(client.getInputStream());
				this.resultMessage=dis.readUTF();
				dis.close();
				dos.close();
				client.close();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}
}
