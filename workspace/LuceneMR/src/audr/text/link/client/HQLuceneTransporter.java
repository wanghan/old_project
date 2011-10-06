/*
 * lucene和hq端连接的客户端...
 */
package audr.text.link.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import audr.text.lucene.distributed.searcher.TextQueryResult;
import audr.text.lucene.fields.TextCategoryFields;

public class HQLuceneTransporter {
	public static void main(String args[]) { // 测试类。。。
//		String[] id = { "001", "002" };
//		String[] lfFilePath = { "D:\\Data\\20\\2010031817075212689032722034.xml", "D:\\Data\\20\\2010031817075212689032722185.xml" };
//		String[] originalFilePath = { "D:\\Data\\20\\2010031817075212689032721401.xml", "D:\\Data\\20\\2010031817075212689032721872.xml" };
		TextQueryResult s[];
		try {
			s = new HQLuceneTransporter().queryByString("的", TextCategoryFields.GOVERNMENT);
			for (TextQueryResult textQueryResult : s) {
			//	System.out.println(textQueryResult);
			}
			System.out.println(s.length);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// 把文件插入到lucene中
	public String insertAll(String[] id, String[] lfFilePath,
			String[] originalFilePath, String textType) {
		InsertSenderFileThread thread = null;
		Socket client; // 测试sys.SystemInstances.LUCENE_IP
		try {
			client = new Socket(sys.SystemInstances.LUCENE_IP,
					sys.SystemInstances.LuceneQueryServPort);

			File oriFiles[] = new File[originalFilePath.length]; // /
																	// 需要修改.....
			File lfFiles[] = new File[lfFilePath.length]; // / 需要修改.....
			for (int i = 0; i < originalFilePath.length; i++) {
				oriFiles[i] = new File(originalFilePath[i]);
			}
			for (int i = 0; i < lfFilePath.length; i++) {
				lfFiles[i] = new File(lfFilePath[i]);
			}
			String oriFilepaths[] = new FileTransporter().sendFiles(
					sys.SystemInstances.LUCENE_IP,
					sys.SystemInstances.DATA_SERV_PORT, oriFiles,
					sys.SystemInstances.LUCENE_FILE_CACHE_DIR);

			String lfFilepaths[] = new FileTransporter().sendFiles(
					sys.SystemInstances.LUCENE_IP,
					sys.SystemInstances.TEXT_FILE_SERV_PORT, lfFiles,
					sys.SystemInstances.LUCENE_FILE_CACHE_DIR);
			thread = new InsertSenderFileThread(id, lfFilepaths, oriFilepaths,
					textType, client);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thread.getResult();
	}

	// 按关键字查询...
	public TextQueryResult[] queryByString(String keywords, String textType)
			throws IOException {
		QueryByStringThread thread = null;
		try { // sys.SystemInstances.LIRE_IP
			Socket client = new Socket(sys.SystemInstances.LUCENE_IP,
					sys.SystemInstances.LuceneQueryServPort);
			thread = new QueryByStringThread(keywords, textType, client);
			thread.start();
			thread.join();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return thread.getResult();
	}

	// 按id查询...
	public TextQueryResult[] queryById(String[] id) {
		QueryByIdThread1 thread = null;
		try {
			Socket client = new Socket(sys.SystemInstances.LUCENE_IP,
					sys.SystemInstances.LuceneQueryServPort);
			thread = new QueryByIdThread1(id, client);
			thread.start();
			thread.join();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thread.getResult();

	}
}

// ID查找的线程
class QueryByIdThread1 extends Thread {
	private String id[];
	private Socket client;
	private TextQueryResult[] result;

	public QueryByIdThread1(String id[], Socket socket) {
		this.id = id;
		this.client = socket;
	}

	public TextQueryResult[] getResult() {
		return result;
	}

	public void run() {
		DataOutputStream dos;
		try {
			// /下述代码是总控端发给lucene的参数...
			dos = new DataOutputStream(client.getOutputStream());
			dos.writeInt(0);// 整数0代表按id查询！
			dos.flush();
			dos.writeInt(id.length);
			dos.flush();
			for (int i = 0; i < id.length; i++) {
				dos.writeUTF(id[i]);
				dos.flush();
			}
			// /下述代码是总控接收总控端发送的结果集...
//			DataInputStream dis = new DataInputStream(client.getInputStream());
//			int len3 = dis.readInt();
//			for (int i = 0; i < len3; ++i) {
//				result[i] = new TextQueryResult();
//				result[i].id = dis.readUTF();
//				System.out.println(result[i].id);
//				result[i].originalFileName = dis.readUTF();
//				result[i].score = dis.readDouble();
//				String filepath = dis.readUTF();
//				result[i].originalFilePath = filepath;
//			}
//			result = tresult;
//			dos.close();
			
			DataInputStream dis=new DataInputStream(client.getInputStream());
			int len3 = dis.readInt();
			
			// File originalFile[]=new File[len3];
			// /接收Lucene端传过来的filepaths....
			result=new TextQueryResult[len3];
			for (int i = 0; i < len3; ++i) {
				result[i]=new TextQueryResult();
				result[i].id = dis.readUTF();
				result[i].originalFileName = dis.readUTF();
				result[i].originalFilePath = dis.readUTF();
				result[i].snippet=dis.readUTF();
				result[i].score = dis.readDouble();
				

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

// 字符查询的线程
class QueryByStringThread extends Thread {
	private String keywords;
	private Socket client;
	private TextQueryResult[] result;
	private String textType;

	public QueryByStringThread(String keywords, String textType, Socket socket) {
		this.keywords = keywords;
		this.client = socket;
		this.textType = textType;
	}

	public TextQueryResult[] getResult() {
		return result;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			DataOutputStream dos = new DataOutputStream(client
					.getOutputStream());

			// send 1 first means query by url
			dos.writeInt(1);
			dos.flush();

			// send paras
			dos.writeUTF(keywords);
			System.out.println("the keywors is " + keywords);
			dos.flush();
			dos.writeUTF(textType);
			dos.flush();
			// waiting and receive result

			// /下述代码是总控接收总控端发送的结果集...
			DataInputStream dis = new DataInputStream(client.getInputStream());
			int len3 = dis.readInt();
			
			// File originalFile[]=new File[len3];
			// /接收Lucene端传过来的filepaths.....
			result=new TextQueryResult[len3];
			for (int i = 0; i < len3; ++i) {
				result[i]=new TextQueryResult();
				result[i].id = dis.readUTF();
				result[i].originalFileName = dis.readUTF();
				result[i].originalFilePath = dis.readUTF();
				result[i].snippet=dis.readUTF();
				result[i].score = dis.readDouble();
				

			}

			dos.close();
			dos.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}

// 线程插入文件的线程
class InsertSenderFileThread extends Thread {
	private String[] id;
	private Socket client;
	private String lfFilePath[];
	private String originalFilePath[];
	private String textType;
	private String result = null;

	public InsertSenderFileThread(String[] id, String[] lfFilePath,
			String[] originalFilePath, String textType, Socket socket) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.client = socket;
		this.lfFilePath = lfFilePath;
		this.originalFilePath = originalFilePath;
		this.textType = textType;
	}

	public String getResult() {
		return result;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataOutputStream dos = new DataOutputStream(client
					.getOutputStream());
			// send ids
			int len1 = id.length;
			dos.writeInt(len1);
			dos.flush();
			for (int i = 0; i < len1; ++i) {
				dos.writeUTF(id[i]);
				dos.flush();
			}
			// send lfFilePath
			int len2 = lfFilePath.length;
			dos.writeInt(len2);
			dos.flush();
			for (int i = 0; i < len2; ++i) {
				dos.writeUTF(lfFilePath[i]);
				dos.flush();
			}
			// send originalFilePath
			int len3 = originalFilePath.length;
			dos.writeInt(len3);
			dos.flush();
			for (int j = 0; j < len3; j++) {
				dos.writeUTF(originalFilePath[j]);
				dos.flush();
			}
			
			//send TextType
			dos.writeUTF(textType);
			dos.flush();

			// wait lire return insert result

			DataInputStream dis = new DataInputStream(client.getInputStream());
			// receive the result length first
			result = dis.readUTF();
			dis.close();
			dos.close();
			client.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
