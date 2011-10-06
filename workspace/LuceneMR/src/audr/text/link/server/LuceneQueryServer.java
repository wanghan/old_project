package audr.text.link.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sys.SystemInstances;
import audr.text.ext.cache.TextRetrievalCache;
import audr.text.link.client.FileTransporter;
import audr.text.lucene.concurrent.searcher.Searcher;
import audr.text.lucene.distributed.searcher.TextQueryResult;

public class LuceneQueryServer {
	private final ServerSocket serverSocket;
	private final ExecutorService servicePool;// 线程池

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			LuceneQueryServer server = new LuceneQueryServer(
					SystemInstances.LuceneQueryServPort,
					SystemInstances.POOL_SIZE);
			server.service();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LuceneQueryServer(int port, int poolSize) throws IOException {
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
				servicePool.execute(new Handler(client));
				// System.out.println("User "+i+" is connecting to
				// LireQuertServer");
				i++;
			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
				servicePool.shutdown();
			}
		}
	}

	class Handler implements Runnable {
		private final Socket socket;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				DataInputStream dis = new DataInputStream(socket
						.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket
						.getOutputStream());
				int queryType = dis.readInt();
				// receive 0 first means query by id
				if (queryType == 0) {

					int len = dis.readInt();
					String id[] = new String[len];
					for (int i = 0; i < len; i++) {
						id = new String[len];
						id[i] = dis.readUTF();

					}
					// add by yilong
					// 上述为id查询时Lucene端接收的id值......
					TextQueryResult[] result = new Searcher().queryById(id);

					File resultFiles[] = new File[result.length];
					for (int i = 0; i < result.length; ++i) {
						resultFiles[i] = new File(result[i]
								.getOriginalFilePath());
					}
					String romoteFilePaths[] = new FileTransporter().sendFiles(
							SystemInstances.HQ_IP,
							SystemInstances.TEXT_FILE_SERV_PORT, resultFiles,
							SystemInstances.LUCENE_FILE_CACHE_DIR);

					dos.writeInt(result.length);
					for (int i = 0; i < result.length; ++i) {
						result[i].setOriginalFilePath(romoteFilePaths[i]);

						dos.writeUTF(result[i].getId());
						dos.writeUTF(result[i].getOriginalFileName());
						dos.writeUTF(result[i].getOriginalFilePath());
						dos.writeUTF(result[i].getSnippet());
						dos.writeDouble(result[i].getScore());
					}

					// int len3 = result.length;
					// dos.writeInt(len3);
					// dos.flush();
					// String id1[] = new String[len3];
					// String fileName[] = new String[len3];
					// double score[] = new double[len3];
					// for (int i = 0; i < len3; i++) {
					// id1[i] = result[i].getId();
					// dos.writeUTF(id1[i]);
					// dos.flush();
					// fileName[i] = result[i].getOriginalFileName();
					// dos.writeUTF(fileName[i]);
					// dos.flush();
					// score[i] = result[i].getScore();
					// dos.writeDouble(score[i]);
					// dos.flush();
					// File file[] = new File[len3];
					// file[i] = new File(result[i].getOriginalFilePath());
					// String filepath = new FileTransporter().sendFile(
					// SystemInstances.HQ_IP,
					// SystemInstances.FILE_SERV_PORT, file[i],
					// SystemInstances.LUCENE_FILE_CACHE_DIR);
					// dos.writeUTF(filepath);
					// dos.flush();
					//
					// }

				}
				// receive 1 first means query by string
				else if (queryType == 1) {
					long startTime = new Date().getTime();
					boolean bUseCache = false;

					String keywords = dis.readUTF();
					System.out.println("the keywords is " + keywords);

					String type = dis.readUTF();
					TextQueryResult result[] = null;
					System.out.println(type);

					// added by yilong.
					// 1.
					TextRetrievalCache cache = new TextRetrievalCache();
					String key = type + "$" + keywords;
					Object obj = cache.getData(key);
					if (obj != null) {
						bUseCache = true;
						result = (TextQueryResult[]) obj;
					} else {
						if (type.equals("text")) {
							result = new Searcher().queryByString(keywords);

						} else {
							result = new Searcher(type).queryByString(keywords);
						}
						cache.putData(key, result);
					}
					System.out.println(result.length + "the server len3 is");

					File resultFiles[] = new File[result.length];
					for (int i = 0; i < result.length; ++i) {
						resultFiles[i] = new File(result[i]
								.getOriginalFilePath());
					}
					String romoteFilePaths[] = new FileTransporter().sendFiles(
							SystemInstances.HQ_IP,
							SystemInstances.TEXT_FILE_SERV_PORT, resultFiles,
							SystemInstances.LUCENE_FILE_CACHE_DIR);
					dos.writeInt(result.length);
					for (int i = 0; i < result.length; ++i) {
						result[i].setOriginalFilePath(romoteFilePaths[i]);

						dos.writeUTF(result[i].getId());
						dos.writeUTF(result[i].getOriginalFileName());
						dos.writeUTF(result[i].getOriginalFilePath());
						if (result[i].getSnippet() == null) {
							dos.writeUTF(" ");
						} else {
							dos.writeUTF(result[i].getSnippet());
						}

						dos.writeDouble(result[i].getScore());
					}

					// int len3 = result.length;
					//
					// dos.writeInt(len3);
					// dos.flush();
					// String id1[] = new String[len3];
					// String fileName[] = new String[len3];
					// double score[] = new double[len3];
					// for (int i = 0; i < len3; i++) {
					// id1[i] = result[i].getId();
					// dos.writeUTF(id1[i]);
					// dos.flush();
					// fileName[i] = result[i].getOriginalFileName();
					// dos.writeUTF(fileName[i]);
					// dos.flush();
					// score[i] = result[i].getScore();
					// dos.writeDouble(score[i]);
					// dos.flush();
					// File file[] = new File[len3];
					// file[i] = new File(result[i].getOriginalFilePath());
					// String filepath = new FileTransporter().sendFile(
					// SystemInstances.HQ_IP,
					// SystemInstances.FILE_SERV_PORT, file[i],
					// SystemInstances.LUCENE_FILE_CACHE_DIR);
					// System.out.println("the filepath--" + filepath);
					// dos.writeUTF(filepath);
					// dos.flush();
					// }
					//					
					// dos.writeInt(1);
					System.err.println("[CACHE DIAG] "
							+ (bUseCache ? "使用缓存" : "不使用缓存") + "，耗时（ms）："
							+ (new Date().getTime() - startTime));
				}

				dos.close();
				dis.close();

				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
