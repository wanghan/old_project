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

import sys.Lire;

/**
 * server, runs at the Lire, used for receiving the insert operations and handling them
 * @author wanghan
 *
 */
public class LireInsertServer {

	private ServerSocket serverSocket;
	private ExecutorService servicePool;//Ïß³Ì³Ø
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			LireInsertServer server=new LireInsertServer(sys.SystemInstances.LireInsertServPort, sys.SystemInstances.POOL_SIZE);
			server.service();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public LireInsertServer(int port,int poolSize) throws IOException {
		// TODO Auto-generated constructor stub

		serverSocket=new ServerSocket(port);
//		servicePool=Executors.newFixedThreadPool(poolSize);
		servicePool=Executors.newCachedThreadPool();
		
	}
	public void service() {
		// TODO Auto-generated method stub
		int i=0;
		while(true){
			try {
				Socket client=serverSocket.accept();
				servicePool.execute(new Handler(client));
				System.out.println("User "+i+" connecting");
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
				int len1=dis.readInt();
				String []url=new String[len1];
				String [] existID=null;
				for(int i=0;i<len1;++i){
					url[i]=dis.readUTF();
				}

				int len2=dis.readInt();
				existID=new String[len2];
				for(int k=0;k<len2;++k){
					existID[k]=dis.readUTF();
				}

				File files[]=new File[url.length];
				for(int i=0;i<url.length;++i){
					files[i]=new File(url[i]);
				}
				String imageType=dis.readUTF();
				String result=Lire.insertImage(files,existID,imageType);
				
				DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
				dos.writeUTF(result);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
}
