/**
 * 
 */
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import utils.Constants;




/**
 * @author wanghan
 * use to transport file(s)
 */
public class FileTransporter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	   
	}
	
	/**
	 * 生成一个路径唯一的目录
	 * @param base 被生成目录的父目录
	 * @return 生成目录的路径
	 */
	public String generateDirPath(String base){
		 return base+"dir_"+String.valueOf(System.currentTimeMillis())+"/";
		 
	}
	
	/**
	 * 发送文件方法
	 * @param serverIP 接受文件的服务器IP 
	 * @param serverPort 接受文件的服务端口号
	 * @param file 被传输的文件
	 * @param tempDisPath 保存被传输文件的目录路径
	 * @return 返回文件在服务器端得存储路径
	 */
	
	public String sendFile(String serverIP,int serverPort,File file,String tempDisPath){
		String filepath=tempDisPath;
		
		Socket client;
		try {
			client = new Socket(serverIP,serverPort);
			FileSenderThread thread=new FileSenderThread(filepath,file,client);
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
		return filepath;
	}
	/**
	 * 利用socket传输文件的线程
	 * @author wanghan
	 *
	 */
	class FileSenderThread extends Thread{
		private String filePath;
		private File file;
		private Socket client;
		public FileSenderThread(String filePath, File file,Socket socket) {
			// TODO Auto-generated constructor stub
			this.filePath=filePath;
			this.file=file;
			this.client=socket;
		}
		@Override
		public void run(){
			// TODO Auto-generated method stub
			try {
				DataOutputStream dos=new DataOutputStream(client.getOutputStream());
				DataInputStream dis=new DataInputStream(client.getInputStream());
				//发送文件路径和长度
				dos.writeUTF(filePath);		
				dos.writeLong(file.length());
				//发送文件字节流
				int flag=1;
				byte [] buf=new byte[Constants.BUFFERSIZE];
				FileInputStream fis=new FileInputStream(file);
				while(flag!=-1){
					flag=fis.read(buf);
					if(flag!=-1){
						dos.write(buf,0,flag);
						dos.flush();
					}
					
				}
				fis.close();
				
				//发送完毕读取对方接收完毕信息，之后断开连接
				dis.readInt();
				dos.close();
				dis.close();
				client.close();
				
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			finally{
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
