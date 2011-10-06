package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


import utils.Constants;

public class ScreenSpyClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ScreenSpyClient client=new ScreenSpyClient(Constants.SERVER_IP);
		try {
			client.connect();
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
	}
	private ScreenSpyClientHandler thread;
	private Socket socket=null;
	private String serverAddr;
	public ScreenSpyClient(String serverAddr) {
		// TODO Auto-generated constructor stub
		this.serverAddr=serverAddr;
	}
	
	public void connect() throws InterruptedException, UnknownHostException, IOException{
		socket=new Socket(serverAddr,Constants.SCREEN_SERVER_PORT);
		thread=new ScreenSpyClientHandler(socket);
		thread.start();

	}
	public void close() throws InterruptedException, IOException{
		thread.setRunning(false);
		thread.join();
		socket.close();
	}
}
