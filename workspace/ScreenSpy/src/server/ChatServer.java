/**
 * 
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JTextArea;

/**
 * @author wanghan
 *
 */
public class ChatServer extends Thread{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	private ServerSocket serverSocket;
	private ExecutorService servicePool;//Ïß³Ì³Ø
	private Vector<Socket> clients;
	private JTextArea terminal;
	private String message=null;
	
	public ChatServer(int port,int poolSize,JTextArea terminal) throws IOException {
		// TODO Auto-generated constructor stub
		serverSocket=new ServerSocket(port);
		servicePool=Executors.newFixedThreadPool(poolSize);
		this.terminal=terminal;
		clients=new Vector<Socket>();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i=0;
		while(true){
			try {
				Socket client=serverSocket.accept();
				client.setKeepAlive(true);
				clients.add(client);
				
				String clientAddr=client.getInetAddress().toString();
				terminal.append("User "+clientAddr+" connect \n");
				servicePool.execute(new ChatServerReceiveHandler(terminal,client));
				servicePool.execute(new ChatServerSendHandler(terminal,this));
				System.out.println("User "+i+" is connecting to ChatServer \n");
				i++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				servicePool.shutdown();
			}
		}

	}

	public Vector<Socket> getClients() {
		return clients;
	}

	public void setClients(Vector<Socket> clients) {
		this.clients = clients;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
