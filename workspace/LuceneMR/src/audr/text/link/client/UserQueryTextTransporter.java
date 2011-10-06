package audr.text.link.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import audr.text.lucene.distributed.searcher.TextQueryResult;

public class UserQueryTextTransporter {
	
	public static void main(String[] args) {
		String bf[]=new String[2];
		String sf[]=new String[2];
		bf[0]="1";
		bf[1]="2";
		sf[0]="3";
		sf[1]="4";
		TextQueryResult[] results=new UserQueryTextTransporter().queryById(sf, "D:\\");
	}
	
	public static int QUERY_BF=11;
	public static int QUERY_SF=12;
	public static int QUERY_BFS=13;
	public static int QUERY_SFS=14;
	
	
	// 按关键字的方式查询....
	public TextQueryResult[] querybyKeywords(String keywords, String[] bf,
			String[] sf,String textType, String localPath) {
		QueryByKeywordsThread thread = null;
		try {
			Socket client = new Socket(sys.SystemInstances.HQ_IP,
					sys.SystemInstances.LuceneQueryServPort);
			thread = new QueryByKeywordsThread(keywords, bf, sf, textType, localPath,
					client);
			thread.start();
			thread.join();
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thread.getResult();

	}
	/**
	 * 查询总控基本特征
	 * @param id
	 * @return
	 */
	public String queryBF(String id){
		OneStringParaLinkThread thread = null;
		try {
			Socket client = new Socket(sys.SystemInstances.HQ_IP,
					sys.SystemInstances.LuceneQueryServPort);
			thread = new OneStringParaLinkThread(QUERY_BF,id, client);
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
		return thread.getOutput();
	}
	/**
	 * 查询总控语义特征
	 * @param id
	 * @return
	 */
	public String querySF(String id){
		OneStringParaLinkThread thread = null;
		try {
			Socket client = new Socket(sys.SystemInstances.HQ_IP,
					sys.SystemInstances.LuceneQueryServPort);
			thread = new OneStringParaLinkThread(QUERY_SF,id, client);
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
		return thread.getOutput();
	}
	/**
	 * 批量查询语义特征
	 * @param id
	 * @return
	 */
	public String[] querySFs(String id[]){
		MultiStringsParaLinkThread thread = null;
		try {
			Socket client = new Socket(sys.SystemInstances.HQ_IP,
					sys.SystemInstances.LuceneQueryServPort);
			thread = new MultiStringsParaLinkThread(QUERY_SFS,id, client);
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
		return thread.getOutput();
	}
	/**
	 * 批量查询基本特征
	 * @param id
	 * @return
	 */
	public String[] queryBFs(String id[]){
		MultiStringsParaLinkThread thread = null;
		try {
			Socket client = new Socket(sys.SystemInstances.HQ_IP,
					sys.SystemInstances.LuceneQueryServPort);
			thread = new MultiStringsParaLinkThread(QUERY_BFS,id, client);
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
		return thread.getOutput();
	}
	// 按id的方式查询....
	public TextQueryResult[] queryById(String[] id, String localPath) {
		QueryByIdThread thread = null;
		try {
			Socket client = new Socket(sys.SystemInstances.HQ_IP,
					sys.SystemInstances.LuceneQueryServPort);
			thread = new QueryByIdThread(id, localPath, client);
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
class OneStringParaLinkThread extends Thread{
	String input;
	String output;
	Socket socket;
	int type;
	public OneStringParaLinkThread(int queryType,String input,Socket socket) {
		// TODO Auto-generated constructor stub
		this.input=input;
		this.socket=socket;
		this.type=queryType;
	}
	public String getOutput() {
		return output;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			dos.writeInt(type);
			dos.writeUTF(input);
			output=dis.readUTF();
			dos.close();
			dis.close();
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
}
class MultiStringsParaLinkThread extends Thread{
	String input[];
	String output[];
	Socket socket;
	int type;
	public MultiStringsParaLinkThread(int queryType,String input[],Socket socket) {
		// TODO Auto-generated constructor stub
		this.input=input;
		this.socket=socket;
		this.type=queryType;
	}
	public String[] getOutput() {
		return output;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			dos.writeInt(type);
			dos.writeInt(input.length);
			for(int i=0;i<input.length;++i){
				dos.writeUTF(input[i]);
			}
			int len=dis.readInt();
			for(int i=0;i<len;++i){
				output[i]=dis.readUTF();
			}
			
			dos.close();
			dis.close();
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
}
class QueryByKeywordsThread extends Thread {
	private String keywords;
	private String[] bf;
	private String[] sf;
	private String userPath;
	private Socket client;
	private String textType;
	private TextQueryResult[] result;

	public QueryByKeywordsThread(String keywords, String[] bf, String[] sf,
			String type,String userPath, Socket socket) {
		// TODO Auto-generated constructor stub
		this.keywords = keywords;
		this.userPath = userPath;
		this.bf = bf;
		this.sf = sf;
		this.textType=type;
		this.client = socket;
	}

	public TextQueryResult[] getResult() {
		return result;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// send signal
		try {

			DataOutputStream dos = new DataOutputStream(client
					.getOutputStream());
			dos.writeInt(0); // "0为关键字查询"
			dos.writeUTF(keywords);
			dos.flush();
			dos.writeInt(bf.length);
			dos.flush();
			for (int i = 0; i < bf.length; i++) {
				dos.writeUTF(bf[i]);
				dos.flush();
			}
			dos.writeInt(sf.length);
			dos.flush();
			for (int j = 0; j < sf.length; j++) {
				dos.writeUTF(sf[j]);
				dos.flush();
			}
			
			//send userPath
			dos.writeUTF(userPath);
			dos.flush();
			dos.writeUTF(textType);
			dos.flush();
			
			//写入结果集的部分代码..
			DataInputStream dis = new DataInputStream(client.getInputStream());
			int len3 = dis.readInt();
			result = new TextQueryResult[len3];
			for (int i = 0; i < len3; ++i) {
				result[i] = new TextQueryResult();
				result[i].id = dis.readUTF();
				System.out.println(result[i].id);
				result[i].originalFileName = dis.readUTF();
				result[i].originalFilePath = dis.readUTF();
				result[i].setSnippet(dis.readUTF());
				result[i].score = dis.readDouble();
			}
			for (int i = 0; i < len3; i++) {
				String filepath = result[i].getOriginalFilePath(); // 写入客户端的文件路径
				System.out.println(filepath + "the filepath");
				File file = new File(filepath);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				long remains = dis.readLong();
				FileOutputStream fos = new FileOutputStream(new File(filepath));
				byte[] buf = new byte[sys.SystemInstances.BUFFERSIZE];
				int byteNumber = 0;
				while (remains != 0) {
					if (remains < sys.SystemInstances.BUFFERSIZE) {
						byteNumber = dis.read(buf, 0, (int) remains);
						if (byteNumber == -1)
							continue;
						fos.write(buf, 0, byteNumber);
						fos.flush();
						remains -= byteNumber;
					} else {
						byteNumber = dis.read(buf);
						if (byteNumber == -1)
							continue;
						fos.write(buf, 0, byteNumber);
						fos.flush();
						remains -= byteNumber;
					}

				}
				fos.close();
			}
			dis.close();
			dos.close();
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("finally");
		}
	}
}

class QueryByIdThread extends Thread {

	private String[] id;
	private String userPath;
	private Socket client;
	private TextQueryResult[] result;

	public QueryByIdThread(String[] id, String userPath, Socket socket) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.userPath = userPath;
		this.client = socket;
	}

	public TextQueryResult[] getResult() {
		return result;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// send signal
		try {
			DataOutputStream dos = new DataOutputStream(client
					.getOutputStream());
			dos.writeInt(1); // "1 为用id查询"
			dos.flush();
			dos.writeInt(id.length);
			dos.flush();
			for (int i = 0; i < id.length; ++i) {
				dos.writeUTF(id[i]);
				dos.flush();
			}
			
			dos.writeUTF(userPath);
			dos.flush();
			// "写入总控返回的结果集!"
			DataInputStream dis = new DataInputStream(client.getInputStream());
			int len3 = dis.readInt();
			result = new TextQueryResult[len3];
			for (int i = 0; i < len3; ++i) {
				result[i] = new TextQueryResult();
				result[i].id = dis.readUTF();
				System.out.println(result[i].id);
				result[i].originalFileName = dis.readUTF();
				result[i].originalFilePath = dis.readUTF();
				result[i].setSnippet(dis.readUTF());
				result[i].score = dis.readDouble();
			}
			for (int i = 0; i < len3; i++) {
				String filepath = result[i].getOriginalFilePath(); // 写入客户端的文件路径
				System.out.println(filepath + "the filepath");
				File file = new File(filepath);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				long remains = dis.readLong();
				FileOutputStream fos = new FileOutputStream(new File(filepath));
				byte[] buf = new byte[sys.SystemInstances.BUFFERSIZE];
				int byteNumber = 0;
				while (remains != 0) {
					if (remains < sys.SystemInstances.BUFFERSIZE) {
						byteNumber = dis.read(buf, 0, (int) remains);
						if (byteNumber == -1)
							continue;
						fos.write(buf, 0, byteNumber);
						fos.flush();
						remains -= byteNumber;
					} else {
						byteNumber = dis.read(buf);
						if (byteNumber == -1)
							continue;
						fos.write(buf, 0, byteNumber);
						fos.flush();
						remains -= byteNumber;
					}

				}
				fos.close();
			}

			dis.close();
			dos.close();
			// client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}