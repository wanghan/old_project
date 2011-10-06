/**
 * 
 */
package audr.text.link.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import sys.HQ;
import sys.QueryResult;

/**
 * server, runs at the HQ, used for receiving the query operations and handling them
 * @author wanghan
 *
 */
public class HQQueryServer {

	private ServerSocket serverSocket;
	private ExecutorService servicePool;//Ïß³Ì³Ø
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			HQQueryServer server=new HQQueryServer(sys.SystemInstances.HQUserQueryServPort, sys.SystemInstances.POOL_SIZE);
			server.service();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public HQQueryServer(int port,int poolSize) throws IOException {
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
				client.setKeepAlive(true);
				servicePool.execute(new Handler(client));
				System.out.println("User "+i+" is connecting to HQUserQuertServer");
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
				
				QueryResult result=null;
				
				//queryType=QUERY_NORMAL indicates Normal Query
				//queryType=QUERY_BY_LIREID indicates Query By lire ID
				int queryType=dis.readInt();
				if(queryType==sys.SystemInstances.QUERY_NORMAL){
					String url=null;
					String type=null;
					String basicfeature[]=null;
					String semanticfeature[]=null;
					
					int flag1=dis.readInt();
					if(flag1==0){ //no url and type
						;
					}
					else if(flag1==1){
						url=dis.readUTF();
						type=dis.readUTF();
					}
					int flag2=dis.readInt();
					if(flag2==2){ //no bf and sf
						
					}
					else{
						int len1=dis.readInt();
						basicfeature=new String[len1];
						for(int i=0;i<len1;++i){
							basicfeature[i]=dis.readUTF();
						}
						
						int len2=dis.readInt();
						semanticfeature=new String[len2];
						for(int i=0;i<len2;++i){
							semanticfeature[i]=dis.readUTF();
						}
					}
					String imageType=dis.readUTF();
					
					if(flag1==1&&flag2==3){
	//					result=new Query().Querybyall(url, type, basicfeature, semanticfeature,imageType);
					}
					else if(flag1==1&&flag2==2){
	//					result=new Query().Querybylire(url, type,imageType);
					}
					else if(flag1==0&&flag2==3){
	//					result=new Query().Querybyexist(basicfeature, semanticfeature,imageType);
					}
					
					
					
					ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			//		result.genLength();
					oos.writeObject(result);
					oos.flush();
					
					DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
					
					int length=result.getImage().length;
					for(int i=0;i<length;++i){
						int flag=1;
						byte [] buf=new byte[sys.SystemInstances.BUFFERSIZE];
						
						
						FileInputStream fis=new FileInputStream(result.getImage()[i]);
						while(flag!=-1){
							flag=fis.read(buf);
							if(flag!=-1){
								dos.write(buf,0,flag);
								dos.flush();
							}
							
						}
						
						fis.close();
					}		
					
					//receive 1 get Full Files
					dis.readInt();
					
					//send full files
					int len=result.getFullFile().length;
					File[] files=result.getFullFile();
					
					for(int i=0;i<len;++i){
						int flag=1;
						byte [] buf=new byte[sys.SystemInstances.BUFFERSIZE];
						
						if(files[i].exists()){
							FileInputStream fis=new FileInputStream(files[i]);
							while(flag!=-1){
								flag=fis.read(buf);
								if(flag!=-1){
									dos.write(buf,0,flag);
									dos.flush();
								}
								
							}
							
							fis.close();
						}
						
					}
				}
				else if(queryType==sys.SystemInstances.QUERY_IMAGE_FEATURE){
					String tableName=dis.readUTF();
					String lireID=dis.readUTF();
					Map<String, String> features=HQ.getFeature(tableName, lireID);
					ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(features);
				}
				else if(queryType==sys.SystemInstances.QUERY_BY_LIREID){
					String lireID[]=null;
					int lireID_len=dis.readInt();
					lireID=new String[lireID_len];
					for(int i=0;i<lireID_len;++i){
						lireID[i]=dis.readUTF();
					}
	//				new Query();
					//replace by Real HQ interface
	//				result=Query.queryById(lireID);
					
					ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
					//		result.genLength();
					oos.writeObject(result);
					oos.flush();
					
					DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
					//send small files
					int length=result.getImage().length;
					for(int i=0;i<length;++i){
						int flag=1;
						byte [] buf=new byte[sys.SystemInstances.BUFFERSIZE];
						
						
						FileInputStream fis=new FileInputStream(result.getImage()[i]);
						while(flag!=-1){
							flag=fis.read(buf);
							if(flag!=-1){
								dos.write(buf,0,flag);
								dos.flush();
							}
							
						}
						
						fis.close();
					}
					//send Full files
					length=result.getFullFile().length;
					for(int i=0;i<length;++i){
						int flag=1;
						byte [] buf=new byte[sys.SystemInstances.BUFFERSIZE];
						
						
						FileInputStream fis=new FileInputStream(result.getFullFile()[i]);
						while(flag!=-1){
							flag=fis.read(buf);
							if(flag!=-1){
								dos.write(buf,0,flag);
								dos.flush();
							}
							
						}
						
						fis.close();
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
}
