/**
 * 
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import client.FileTransporter;


import sys.Lire;
import sys.QueryResult;

/**
 * server, runs at the Lire, used for receiving the query operations and handling them
 * @author wanghan
 *
 */
public class LireQueryServer {

	private ServerSocket serverSocket;
	private ExecutorService servicePool;//Ïß³Ì³Ø
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			LireQueryServer server=new LireQueryServer(sys.SystemInstances.LireQueryServPort, sys.SystemInstances.POOL_SIZE);
			server.service();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public LireQueryServer(int port,int poolSize) throws IOException {
		// TODO Auto-generated constructor stub
		serverSocket=new ServerSocket(port);
		servicePool=Executors.newFixedThreadPool(poolSize);
	//	servicePool=Executors.newCachedThreadPool();
		
	}
	public void service() {
		// TODO Auto-generated method stub
		int i=0;
		while(true){
			try {
				Socket client=serverSocket.accept();
				servicePool.execute(new Handler(client));
	//			System.out.println("User "+i+" is connecting to LireQuertServer");
				i++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				servicePool.shutdown();
			}
		}
	}
	class Handler implements Runnable{
	    private Socket socket;
	    public Handler(Socket socket){
	        this.socket=socket;
	    }

	    public void run(){
	        try {
				DataInputStream dis=new DataInputStream(socket.getInputStream());
				DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
				int queryType=dis.readInt();
				//receive 1 first means query by url
				if(queryType==1){
					// query by url
					String URL=dis.readUTF();
					String type=dis.readUTF();
					String imageType=dis.readUTF();
					File file=new File(URL);
					
					//change by Lire
					QueryResult result=Lire.searchFromLire(file, type,imageType);
					
					String filepaths[]=new FileTransporter().sendFiles(sys.SystemInstances.HQ_IP,sys.SystemInstances.FILE_SERV_PORT,result.getImage(),sys.SystemInstances.HQ_LIRE_FILE_CACHE_DIR);
					
					
					dos.writeInt(result.getImage().length);
					for(int i=0;i<result.getImage().length;++i){
						dos.writeUTF(filepaths[i]);
						dos.writeUTF(result.getLireID()[i]);
						dos.flush();
					}
				}
				//query by lire ID
				else if(queryType==0){
					int len1=dis.readInt();
					String lireID[]=new String[len1];
					for(int i=0;i<len1;++i){
						lireID[i]=dis.readUTF();
					}
					int len2=dis.readInt();
					String tableName[]=new String[len2];
					for(int i=0;i<len2;++i){
						tableName[i]=dis.readUTF();
					}
					//change by Lire
					QueryResult result=Lire.QuerybyLireID(lireID,tableName);
					String smallfilepaths[]=new FileTransporter().sendFiles(sys.SystemInstances.HQ_IP,sys.SystemInstances.FILE_SERV_PORT,result.getImage(),sys.SystemInstances.HQ_LIRE_FILE_CACHE_DIR);	
					String fullfilepaths[]=new FileTransporter().sendFiles(sys.SystemInstances.HQ_IP,sys.SystemInstances.FILE_SERV_PORT,result.getFullFile(),sys.SystemInstances.HQ_LIRE_FILE_CACHE_DIR);	
					
					dos.writeInt(result.getImage().length);
					for(int i=0;i<result.getImage().length;++i){
						dos.writeUTF(smallfilepaths[i]);
						dos.writeUTF(fullfilepaths[i]);
						dos.writeUTF(result.getLireID()[i]);
						dos.flush();
					}
				}
				
				
				dos.close();
				dis.close();
				
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
}
