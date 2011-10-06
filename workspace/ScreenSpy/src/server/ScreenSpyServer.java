/**
 * 
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JTabbedPane;

import ui.ScreenPanel;
/**
 * @author wanghan
 *
 */
public class ScreenSpyServer extends Thread{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private ServerSocket serverSocket;
	private ExecutorService servicePool;//Ïß³Ì³Ø
	private JTabbedPane tabbedPane;
	
	public ScreenSpyServer(int port,int poolSize,JTabbedPane tabbedPane) throws IOException {
		// TODO Auto-generated constructor stub
		serverSocket=new ServerSocket(port);
		servicePool=Executors.newFixedThreadPool(poolSize);
		this.tabbedPane=tabbedPane;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i=0;
		while(true){
			try {
				Socket client=serverSocket.accept();
				client.setKeepAlive(true);
				String clientAddr=client.getInetAddress().toString();
				ScreenPanel panel=new ScreenPanel(clientAddr);
				tabbedPane.add(panel);
				servicePool.execute(new ScreenSpyServerHandler(tabbedPane,panel,client));
				System.out.println("User "+i+" is connecting to ScreenSpyServer");
				i++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				servicePool.shutdown();
			}
		}

	}
		
}
