package audr.text.link.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sys.HQ;

import audr.text.link.client.FileTransporter;
import audr.text.link.client.UserQueryTextTransporter;
import audr.text.lucene.distributed.searcher.TextQueryResult;

public class HQTextQueryServer {

	private ServerSocket serverSocket;
	private ExecutorService servicePool;// 线程池

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			HQTextQueryServer server = new HQTextQueryServer(
					sys.SystemInstances.LuceneQueryServPort,
					sys.SystemInstances.POOL_SIZE);
			server.service();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HQTextQueryServer(int port, int poolSize) throws IOException {
		// TODO Auto-generated constructor stub
		serverSocket = new ServerSocket(port);
		servicePool = Executors.newFixedThreadPool(poolSize);
		// servicePool=Executors.newCachedThreadPool();

	}

	public void service() {
		// TODO Auto-generated method stub
		int i = 0;
		while (true) {
			try {
				Socket client = serverSocket.accept();
				client.setKeepAlive(true);
				servicePool.execute(new Handler1(client));
				System.out.println("User " + i
						+ " is connecting to HQUserQuertServer");
				i++;
			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
				servicePool.shutdown();
			}
		}
	}
}

class Handler1 implements Runnable {
	private Socket socket;

	public Handler1(Socket socket) {
		this.socket = socket;
	}

	public void run() {

		// DataOutputStream dos;
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			
			int flag = dis.readInt();// /读一个标记判断查询的方式：其中0为关键字查询，1为id查询！
			
			System.out.println("the flag is " + flag);
			if (flag == 0) // 关键字查询...
			{
				TextQueryResult[] result = null;
				String bf[] = null;
				String sf[] = null;
				String id[] = null;
				
				String keywords = dis.readUTF();
				System.out.println("the keywords is " + keywords);
				int len1 = dis.readInt();
				bf = new String[len1];
				for (int i = 0; i < len1; i++) {
					bf[i] = dis.readUTF();
				}
				int len2 = dis.readInt();
				sf = new String[len2];
				for (int j = 0; j < len2; j++) {
					sf[j] = dis.readUTF();
				}
				String userPath=dis.readUTF();
				String textType=dis.readUTF();
				/*
				 * 调用总控接口获得总控的结果集！
				 */
				result = HQ.Querybyall(keywords, bf, sf);

				// String filepaths[]=new
				// FileTransporter().sendFiles(sys.SystemInstances.LUCENE_IP,sys.SystemInstances.FILE_SERV_PORT,result.getOriginalFile(),sys.SystemInstances.LUCENE_FILE_CACHE_DIR);
				// ObjectOutputStream oos = new
				// ObjectOutputStream(socket.getOutputStream());
				// oos.writeObject(result);
				// oos.flush();

				String newPath[] = new String[result.length];
				String remoteDir=new FileTransporter().generateDirPath(userPath);
				for (int i = 0; i < newPath.length; ++i) {
					newPath[i] = remoteDir+result[i].originalFileName;
				}

				dos.writeInt(result.length);
				for (int i = 0; i < result.length; ++i) {
					dos.writeUTF(result[i].getId());
					dos.writeUTF(result[i].getOriginalFileName());
					dos.writeUTF(newPath[i]);
					dos.writeUTF(result[i].getSnippet());
					dos.writeDouble(result[i].score);
				}

				// send original text...
				
				int length=result.length;
				for(int i=0;i<length;++i){
					int sendNumber=1;
					byte [] buf=new byte[sys.SystemInstances.BUFFERSIZE];
					dos.writeLong(new File(result[i].getOriginalFilePath()).length());
					FileInputStream fis=new FileInputStream(new File(result[i].getOriginalFilePath()));
					while(sendNumber!=-1){
						sendNumber=fis.read(buf);
						if(sendNumber!=-1){
							dos.write(buf,0,sendNumber);
							dos.flush();
						}
						
					}
					
					fis.close();
				}
			}
			// 当flag==1时调用按id查询的线程
			else if (flag == 1) {
				TextQueryResult[] result = null;
				
				int len1 = dis.readInt();
				String id2[] = new String[len1];
				for (int i = 0; i < id2.length; i++) {
					id2[i] = dis.readUTF();
				}
				String userPath=dis.readUTF();
				
				// result = HQ.queryById(id); //调用总控端得
				result = HQ.Querybyall("aa", id2, id2);
				String newPath[] = new String[result.length];
				String remoteDir=new FileTransporter().generateDirPath(userPath);
				for (int i = 0; i < newPath.length; ++i) {
					newPath[i] = remoteDir+result[i].originalFileName;
				}

				dos.writeInt(result.length);
				for (int i = 0; i < result.length; ++i) {
					dos.writeUTF(result[i].getId());
					dos.writeUTF(result[i].getOriginalFileName());
					dos.writeUTF(newPath[i]);
					dos.writeUTF(result[i].getSnippet());
					dos.writeDouble(result[i].score);
				}

				// send original text...
				
				int length=result.length;
				for(int i=0;i<length;++i){
					int sendNumber=1;
					byte [] buf=new byte[sys.SystemInstances.BUFFERSIZE];
					dos.writeLong(new File(result[i].getOriginalFilePath()).length());
					FileInputStream fis=new FileInputStream(new File(result[i].getOriginalFilePath()));
					while(sendNumber!=-1){
						sendNumber=fis.read(buf);
						if(sendNumber!=-1){
							dos.write(buf,0,sendNumber);
							dos.flush();
						}
						
					}
					
					fis.close();
				}
			}
			else if(flag==UserQueryTextTransporter.QUERY_BF){
				String input =dis.readUTF();
				String result=HQ.queryBF(input);
				dos.writeUTF(result);
			}
			else if(flag==UserQueryTextTransporter.QUERY_SF){
				String input =dis.readUTF();
				String result=HQ.querySF(input);
				dos.writeUTF(result);
			}
			else if(flag==UserQueryTextTransporter.QUERY_BFS){
				int len=dis.readInt();
				String input[]=new String[len];
				for(int i=0;i<len;++i){
					input[i]=dis.readUTF();
				}
				String result[]=HQ.queryBFs(input);
				
				dos.writeInt(result.length);
				for(int i=0;i<result.length;++i){
					dos.writeUTF(result[i]);
				}
			}
			else if(flag==UserQueryTextTransporter.QUERY_SFS){
				int len=dis.readInt();
				String input[]=new String[len];
				for(int i=0;i<len;++i){
					input[i]=dis.readUTF();
				}
				String result[]=HQ.querySFs(input);
				
				dos.writeInt(result.length);
				for(int i=0;i<result.length;++i){
					dos.writeUTF(result[i]);
				}
			}
			dos.close();
			dis.close();
			// socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
