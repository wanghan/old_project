package audr.text.link.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sys.SystemInstances;

/**
 * server used for receive files
 * @author wanghan
 *
 */
public class TextFileServer {


	private ServerSocket serverSocket;
	private ExecutorService servicePool;//Ïß³Ì³Ø
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new TextFileServer(SystemInstances.TEXT_FILE_SERV_PORT, SystemInstances.POOL_SIZE).service();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public TextFileServer(int port,int poolSize) throws IOException {
		// TODO Auto-generated constructor stub

		serverSocket=new ServerSocket(port);
		servicePool=Executors.newFixedThreadPool(poolSize);
//		servicePool=Executors.newCachedThreadPool();
		
	}
	public void service() {
		// TODO Auto-generated method stub
		int i=0;
		while(true){
			try {
				Socket client=serverSocket.accept();
				servicePool.execute(new Handler(client));
				i++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				servicePool.shutdown();
			}
		}
	}
	/**
	 * Thread
	 * Receive the file path and file Stream,
	 * then write the file stream into local path where the received file path indicates
	 * @author wanghan
	 *
	 */
	class Handler implements Runnable{
	    private Socket socket;
	    public Handler(Socket socket){
	        this.socket=socket;
	    }

	    public void run(){
	        try {
				DataInputStream dis=new DataInputStream(socket.getInputStream());
				DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
				String filepath=dis.readUTF();
				long remains=dis.readLong();
				File file=new File(filepath);
				System.out.println("receive file:"+filepath);
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
				}
				FileOutputStream fos=new FileOutputStream(file);
				byte [] buf=new byte[SystemInstances.BUFFERSIZE];
				int byteNumber=0;
				while(remains!=0){
					
					/*
					if(byteNumber!=-1){
						dos.writeInt(1);
						dos.flush();
						fos.write(buf,0,byteNumber);
						fos.flush();
						total+=byteNumber;
					}
					*/
					if(remains<SystemInstances.BUFFERSIZE){
						byteNumber=dis.read(buf,0,(int)remains);
						if(byteNumber==-1)
							continue;
						fos.write(buf,0,byteNumber);
						remains-=byteNumber;
					}
					else{
						byteNumber=dis.read(buf);
						if(byteNumber==-1)
							continue;
						fos.write(buf,0,byteNumber);
						remains-=byteNumber;
					}
					
				}
				
				dos.writeInt(1);
		//		dos.write(1);
				
				dos.flush();
				
				dis.close();
				dos.close();
				fos.close();
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
}
