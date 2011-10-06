package audr.text.link.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sys.SystemInstances;

import audr.text.lucene.distributed.indexer.Indexer;

public class LuceneInsertServer {

	private ServerSocket serverSocket;
	private ExecutorService servicePool;// 线程池

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			LuceneInsertServer server = new LuceneInsertServer(
					SystemInstances.LuceneInsertServPort,
					SystemInstances.POOL_SIZE);
			server.service();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LuceneInsertServer(int port, int poolSize) throws IOException {
		// TODO Auto-generated constructor stub

		serverSocket = new ServerSocket(port);
		 servicePool=Executors.newFixedThreadPool(poolSize);
		//servicePool = Executors.newCachedThreadPool();

	}

	public void service() {
		// TODO Auto-generated method stub
		int i = 0;
		while (true) {
			try {
				Socket client = serverSocket.accept();
				servicePool.execute(new Handler(client));
				System.out.println("User " + i + " connecting");
				i++;
			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
				servicePool.shutdown();
			}
		}
	}

	class Handler implements Runnable {
		private Socket socket;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				
				
				
			
				//get ids
				int len1 = dis.readInt();
				String[] id = new String[len1];
				for (int i = 0; i < len1; ++i) {
					id[i] = dis.readUTF();
				}
				//get lfFilePaths
				int len2 = dis.readInt();
				String[] lfFilePath = null;
				lfFilePath = new String[len2];
				for (int k = 0; k < len2; ++k) {
					lfFilePath[k] = dis.readUTF();
				}
				//get originalFilePaths
				int len3 = dis.readInt();
				String[] originalFilePath = null;
				originalFilePath = new String[len3];
				for (int j = 0; j < len3; j++) {
					originalFilePath[j] = dis.readUTF();
				}
				//get Type
				String textType = dis.readUTF(); // 文本的类型
				System.out.println(textType);
				// String result=Lucene.insertAll(id, lfFilePath,
				// originalFilePath);
				String result = "Error";
				try {
					result = new Indexer().insertAll(id, lfFilePath,
							originalFilePath,textType);// 需要添加参数textType
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DataOutputStream dos = new DataOutputStream(socket
						.getOutputStream());
				dos.writeUTF(result);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
