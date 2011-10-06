package audr.text.link.client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

public class UserImageFeatureTransporter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url[]={"http://www.google.cn/search?hl=zh-CN&client=aff-cs-maxthon&newwindow=1&q=java+%E5%BD%93%E5%89%8D%E8%BF%90%E8%A1%8C%E6%97%B6%E9%97%B4+%E6%AF%AB%E7%A7%92&aq=f&oq="
				,"http://www.xker.com/page/e2008/0424/52309.html"
				,"http://www.patent-cn.com/2007jun/CN1975680.shtml"
		};
		
		String [] basicfeature={"111.jpg","222.jpg","333.jpg"};
		File f1=new File("./Chocolate Cake.bmp");
		File f2=new File("./Water lilies.jpg");
		File f3=new File("./Da Vinci.bmp");
		File f4=new File("./Dragon.bmp");
		
		File files[]={f1,f2,f3};
		
		int j=0;
		for(int i=0;i<5;++i){
			UserImageFeatureTransporter tt=new UserImageFeatureTransporter();
			Map<String,String> re=tt.getFeature(url[1], "222");
			System.out.println(new File(basicfeature[0]).exists());
	//		String re=new UserTransporter().sendInsertInfo(files, basicfeature, f4);

	//		String re=new UserTransporter().sendInsertInfo(url, basicfeature, f1);
	//		System.out.println(re);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	

	}
	public Map<String,String> getFeature(String tableName,String LireID){
		QueryImageFeatureThread thread = null;
		try {
			Socket socket=new Socket(sys.SystemInstances.HQ_IP,sys.SystemInstances.HQUserQueryServPort);
			thread = new QueryImageFeatureThread(tableName,LireID, socket);
			thread.start();
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thread.getOutput();
	}
	
	
}
class QueryImageFeatureThread extends Thread{
	String tableName;
	String lireID;
	Map<String,String> result;
	Socket socket;
	int type;
	public QueryImageFeatureThread(String tableName,String lireID,Socket socket) {
		// TODO Auto-generated constructor stub
		this.tableName=tableName;
		this.socket=socket;
		this.lireID=lireID;
	}
	public Map<String,String> getOutput() {
		return result;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			
			dos.writeInt(sys.SystemInstances.QUERY_IMAGE_FEATURE);
			dos.writeUTF(tableName);
			dos.writeUTF(lireID);
			
			ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());
			result=(Map<String,String>)dis.readObject();
			dos.close();
			dis.close();
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
}
