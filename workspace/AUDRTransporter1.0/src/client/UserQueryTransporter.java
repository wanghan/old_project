/**
 * 
 */
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import sys.QueryResult;
import sys.SystemInstances;


/**
 *interfaces invoked by user point for query operation
 * @author wanghan
 *
 */
public class UserQueryTransporter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url[]={"http://www.google.cn/search?hl=zh-CN&client=aff-cs-maxthon&newwindow=1&q=java+%E5%BD%93%E5%89%8D%E8%BF%90%E8%A1%8C%E6%97%B6%E9%97%B4+%E6%AF%AB%E7%A7%92&aq=f&oq="
				,"http://www.xker.com/page/e2008/0424/52309.html"
				,"http://www.patent-cn.com/2007jun/CN1975680.shtml"
		};
		
		String [] basicfeature={"111.jpg","222.jpg","333.jpg"};
//		File f1=new File("./Chocolate Cake.bmp");
//		File f2=new File("./Water lilies.jpg");
//		File f3=new File("./Da Vinci.bmp");

		for(int i=0;i<5;++i){
			UserQueryTransporter tt=new UserQueryTransporter();
			QueryResult re=tt.QuerybylireID(url);
			System.out.println(new File(basicfeature[0]).exists());
	//		String re=new UserTransporter().sendInsertInfo(files, basicfeature, f4);

	//		String re=new UserTransporter().sendInsertInfo(url, basicfeature, f1);
			System.out.println(re);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	

	}

	private Socket socket=null;
	private UserTransporter transporter=null;
	
	private long fullfileLength[]=null;  //length array of all full files
	private String fullFilePaths[]=null;//path array of all full files
	private String fullFileNames[]=null;//name array of all full files
	public UserQueryTransporter() {
		try {
			transporter=new UserTransporter();
			socket=new Socket(sys.SystemInstances.HQ_IP,sys.SystemInstances.HQUserQueryServPort);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public QueryResult Querybylire(File url, String type,String imageType) {
		QueryResult  re= transporter.QueryByAll(url, type,null,null,imageType,socket);
		this.fullfileLength=re.getFullFileLength();
		this.fullFileNames=re.getFullFileNames();
		this.fullFilePaths=new String[re.getFullFile().length];
		for(int i=0;i<re.getFullFile().length;++i){
			this.fullFilePaths[i]=re.getFullFile()[i].getAbsolutePath();
		}
		return re;
	}
	public QueryResult Querybyexist(String[] basicfeature,String[] semanticfeature,String imageType){
		QueryResult  re= transporter.QueryByAll(null, null, basicfeature, semanticfeature,imageType,socket);
		this.fullfileLength=re.getFullFileLength();
		this.fullFileNames=re.getFullFileNames();
		this.fullFilePaths=new String[re.getFullFile().length];
		for(int i=0;i<re.getFullFile().length;++i){
			this.fullFilePaths[i]=re.getFullFile()[i].getAbsolutePath();
		}
		return re;
	}
	public QueryResult QueryByAll(File url, String type, String[] basicfeature, String [] semanticfeature,String imageType){
		QueryResult  re= transporter.QueryByAll(url, type, basicfeature, semanticfeature, imageType, socket);
		this.fullfileLength=re.getFullFileLength();
		this.fullFileNames=re.getFullFileNames();
		this.fullFilePaths=new String[re.getFullFile().length];
		for(int i=0;i<re.getFullFile().length;++i){
			this.fullFilePaths[i]=re.getFullFile()[i].getAbsolutePath();
		}
		return re;
	}
	public String[] getFullFiles(){
		return transporter.getFullFiles(fullFilePaths,fullFileNames,fullfileLength, socket);
	}
	public QueryResult QuerybylireID(String [] lireID){
		return transporter.QuerybylireID(lireID, socket);
	}
}
/**
 * Sub class to implement the query operation
 * @author wanghan
 *
 */
class UserTransporter {
	/**
	 * get full files from HQ
	 * @param filepaths file locations at HQ point
	 * @param filenames file names at HQ
	 * @param filelength file lengths of full files be transported
	 * @param socket
	 * @return
	 */
	public String[] getFullFiles(String [] filepaths,String []filenames,long filelength[],Socket socket){
		GetFullFilesThread thread=null;
		try {		
			thread=new GetFullFilesThread(filepaths,filenames,filelength,socket.getInputStream(),socket.getOutputStream());
			thread.start();
			thread.join();
			
			
		}catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thread.result;
	}
	public QueryResult QuerybylireID(String [] lireID,Socket socket){
		QueryByLireIDThread thread=null;
		try {		
			thread=new QueryByLireIDThread(lireID,socket.getInputStream(),socket.getOutputStream());
			thread.start();
			thread.join();			
		}catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thread.result;
		
	}

	public QueryResult QueryByAll(File url, String type, String[] basicfeature, String [] semanticfeature,String imageType,Socket client){
		QuerySenderThread thread=null;
		try {		
			thread=new QuerySenderThread(url,type,basicfeature,semanticfeature,imageType,client.getInputStream(),client.getOutputStream());
			thread.start();
			thread.join();
			
			
		}catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thread.result;
	}
	/**
	 * 获得原图的客户端线程
	 * @author wanghan
	 *
	 */
	class GetFullFilesThread extends Thread{
		private String[] filepath;
		private InputStream	 in;
		private OutputStream out;
		private String result[];
		private long[] filelength;
		private String[] filenames;
		public GetFullFilesThread(String [] paths,String[] names,long filelength[], InputStream	 in,OutputStream out) {
			// TODO Auto-generated constructor stub
			this.filepath=paths;
			this.in=in;
			this.out=out;
			this.filenames=names;
			this.filelength=filelength;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//send signal
			try {
				
				DataOutputStream dos=new DataOutputStream(out);
				dos.writeInt(1);
		//		dos.writeInt(filepath.length);
		//		for(int i=0;i<filepath.length;++i){
		//			dos.writeUTF(filepath[i]);
		//			dos.flush();
		//		}
				
				DataInputStream dis=new DataInputStream(in);

				result=new String[filepath.length];
				String filedir=new FileTransporter().generateDirPath(SystemInstances.USER_FILE_CACHE_DIR);
				for(int i=0;i<filepath.length;++i){
					result[i]=filedir+filenames[i];
					long file_length=filelength[i];
					File ftemp=new File(result[i]);
					if(!ftemp.exists()){
						ftemp.getParentFile().mkdirs();
					}
					FileOutputStream fos=new FileOutputStream(ftemp);
					byte [] buf=new byte[sys.SystemInstances.BUFFERSIZE];
					long remains=file_length;
					while(remains!=0){
						if(remains<=sys.SystemInstances.BUFFERSIZE){
							int readlen=dis.read(buf,0,(int)remains);
							if(readlen==-1)
								continue;
							fos.write(buf, 0, readlen);
							fos.flush();
							remains-=readlen;
						}
						else{
							int readlen=dis.read(buf);
							if(readlen==-1)
								continue;
							fos.write(buf, 0, readlen);
							fos.flush();
							remains-=readlen;
							
						}
					}
					fos.close();
				}
				
				dis.close();
				dos.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
			}
		}
	}
	class QueryByLireIDThread extends Thread{
		private String[] lireID;
		private InputStream	 in;
		private OutputStream out;
		private QueryResult result;
		public QueryByLireIDThread(String [] lireID,InputStream	 in,OutputStream out) {
			// TODO Auto-generated constructor stub
			this.lireID=lireID;
			this.in=in;
			this.out=out;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//send signal
			try {
				
				DataOutputStream dos=new DataOutputStream(out);
				
				//send query type
				dos.writeInt(SystemInstances.QUERY_BY_LIREID);
				dos.writeInt(lireID.length);
				for(int i=0;i<lireID.length;++i){
					dos.writeUTF(lireID[i]);
					dos.flush();
				}
				
				ObjectInputStream ois=new ObjectInputStream(in);
				result=(QueryResult)ois.readObject();
				
				//receive small images
				int len3=result.getImage().length;
				
				DataInputStream dis=new DataInputStream(in);
				String filedir=new FileTransporter().generateDirPath(SystemInstances.USER_FILE_CACHE_DIR);
				for(int i=0;i<len3;++i){
					String filepath=filedir+result.getImage()[i].getName();
					long file_length=result.getSmallFileLength()[i];
					result.getImage()[i]=new File(filepath);
					if(!result.getImage()[i].getParentFile().exists()){
						result.getImage()[i].getParentFile().mkdirs();
					}
					FileOutputStream fos=new FileOutputStream(result.getImage()[i]);
					byte [] buf=new byte[sys.SystemInstances.BUFFERSIZE];
					long remains=file_length;
					while(remains!=0){
						if(remains<=sys.SystemInstances.BUFFERSIZE){
							int readlen=dis.read(buf,0,(int)remains);
							if(readlen==-1)
								continue;
							fos.write(buf, 0, readlen);
							fos.flush();
							remains-=readlen;
						}
						else{
							int readlen=dis.read(buf);
							if(readlen==-1)
								continue;
							fos.write(buf, 0, readlen);
							fos.flush();
							remains-=readlen;
							
						}
					}
					fos.close();
				}
				//receive full files.
				len3=result.getFullFile().length;
				for(int i=0;i<len3;++i){
					String filepath=filedir+result.getFullFile()[i].getName();
					long file_length=result.getFullFileLength()[i];
					result.getFullFile()[i]=new File(filepath);
					if(!result.getFullFile()[i].getParentFile().exists()){
						result.getFullFile()[i].getParentFile().mkdirs();
					}
					FileOutputStream fos=new FileOutputStream(result.getFullFile()[i]);
					byte [] buf=new byte[sys.SystemInstances.BUFFERSIZE];
					long remains=file_length;
					while(remains!=0){
						if(remains<=sys.SystemInstances.BUFFERSIZE){
							int readlen=dis.read(buf,0,(int)remains);
							if(readlen==-1)
								continue;
							fos.write(buf, 0, readlen);
							fos.flush();
							remains-=readlen;
						}
						else{
							int readlen=dis.read(buf);
							if(readlen==-1)
								continue;
							fos.write(buf, 0, readlen);
							fos.flush();
							remains-=readlen;
							
						}
					}
					fos.close();
				}
				
				dis.close();
				dos.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
			}
		}
	}
	/**
	 * thread used for sending the insert info the HQ
	 * @author wanghan
	 *
	 */
	class QuerySenderThread extends Thread{
		private File url;
		private String type;
		private String[] basicfeature;
		private String[] semanticfeature;
		private QueryResult result;
		private String imageType;
		private InputStream in;
		private OutputStream out;
		public QuerySenderThread(File url, String type, String[] basicfeature, String [] semanticfeature,String imageType,InputStream in,OutputStream out) {
			// TODO Auto-generated constructor stub
			this.basicfeature=basicfeature;
			this.semanticfeature=semanticfeature;
			this.url=url;
			this.type=type;
			this.imageType=imageType;
			this.in=in;
			this.out=out;
		}
		@Override
		public void run(){
			// TODO Auto-generated method stub
			try {
				DataOutputStream dos=new DataOutputStream(out);
				//send query type
				dos.writeInt(sys.SystemInstances.QUERY_NORMAL);
				//Querybylire send 1 first 
				if(url==null){
					dos.writeInt(0);
					dos.flush();
				}
				else{
					dos.writeInt(1);
					dos.flush();
					String filepath=new FileTransporter().sendFile(sys.SystemInstances.HQ_IP,sys.SystemInstances.FILE_SERV_PORT,url,sys.SystemInstances.HQ_USER_FILE_CACHE_DIR);
					dos.writeUTF(filepath);
					dos.writeUTF(type);
				}
				
				if(basicfeature==null){
					dos.writeInt(2);
					dos.flush();
				}
				else{
					dos.writeInt(3);
					dos.flush();
					// send length first
					int len1=basicfeature.length;
					dos.writeInt(len1);
					dos.flush();
					for(int i=0;i<len1;++i){
						dos.writeUTF(basicfeature[i]);
						dos.flush();
					}
					
					// send length first
					int len2=semanticfeature.length;
					dos.writeInt(len2);
					dos.flush();
					for(int i=0;i<len2;++i){
						dos.writeUTF(semanticfeature[i]);
						dos.flush();
					}
				}
				
				dos.writeUTF(imageType);
				
				//transport query result to the users
				
				ObjectInputStream ois=new ObjectInputStream(in);
				result=(QueryResult)ois.readObject();
				int len3=result.getImage().length;
				
				DataInputStream dis=new DataInputStream(in);
				String filedir=new FileTransporter().generateDirPath(SystemInstances.USER_FILE_CACHE_DIR);
				for(int i=0;i<len3;++i){
					String filepath=filedir+result.getImage()[i].getName();
					long file_length=result.getSmallFileLength()[i];
					result.getImage()[i]=new File(filepath);
					if(!result.getImage()[i].getParentFile().exists()){
						result.getImage()[i].getParentFile().mkdirs();
					}
					FileOutputStream fos=new FileOutputStream(result.getImage()[i]);
					byte [] buf=new byte[sys.SystemInstances.BUFFERSIZE];
					long remains=file_length;
					while(remains!=0){
						if(remains<=sys.SystemInstances.BUFFERSIZE){
							int readlen=dis.read(buf,0,(int)remains);
							if(readlen==-1)
								continue;
							fos.write(buf, 0, readlen);
							fos.flush();
							remains-=readlen;
						}
						else{
							int readlen=dis.read(buf);
							if(readlen==-1)
								continue;
							fos.write(buf, 0, readlen);
							fos.flush();
							remains-=readlen;
							
						}
					}
					fos.close();
				}
				dis.close();
				dos.close();
			} 
			catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				// TODO: handle exception
			}
		}
	}
	
}
