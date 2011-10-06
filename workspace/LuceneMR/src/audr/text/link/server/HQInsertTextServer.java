package audr.text.link.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sys.HQ;

public class HQInsertTextServer {
	private ServerSocket serverSocket;
	private ExecutorService servicePool;// 线程池

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			HQInsertTextServer server = new HQInsertTextServer(
					sys.SystemInstances.HQLuceneInsertServPort,
					sys.SystemInstances.POOL_SIZE);
			server.service();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HQInsertTextServer(int port, int poolSize) throws IOException {
		// TODO Auto-generated constructor stub

		serverSocket = new ServerSocket(port);
		// servicePool=Executors.newFixedThreadPool(poolSize);
		servicePool = Executors.newCachedThreadPool();

	}

	public void service() {
		// TODO Auto-generated method stub
		int i = 0;
		while (true) {
			try {
				Socket client = serverSocket.accept();
				servicePool.execute(new FileHandler(client));
				System.out.println("User " + i + " connecting");
				i++;
			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
				servicePool.shutdown();
			}
		}
	}
}

class FileHandler implements Runnable {
	private Socket socket;

	public FileHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());

			String[] basicfeature = null;
			String[] lfFilePath = null;
			String semanticfeature = null;
			String textType = null;
			int len2 = dis.readInt();
			basicfeature = new String[len2];
			for (int k = 0; k < len2; ++k) {
				basicfeature[k] = dis.readUTF();
			}
			semanticfeature = dis.readUTF();
			int len1 = dis.readInt();
			String[] url = new String[len1];

			for (int i = 0; i < len1; ++i) {
				url[i] = dis.readUTF();
			}
			int len3 = dis.readInt();
			System.out.println("the len3 is " + len3);
			lfFilePath = new String[len3];
			for (int j = 0; j < len3; j++) {
				lfFilePath[j] = dis.readUTF();
			}
			textType = dis.readUTF();

			String message = HQ.receiveInsertInfo(url, basicfeature,
					semanticfeature, lfFilePath,textType); // 总控端需要扩展...

			DataOutputStream dos = new DataOutputStream(socket
					.getOutputStream());
			dos.writeUTF(message);

			dis.close();
			dos.close();
			// socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
