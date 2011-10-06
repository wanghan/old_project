package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

import utils.Constants;

public class ChatClient {
	private Socket chatsocket=null;
	private ChatClientSendHandler sendthread;
	private ChatClientReceiveHandler receivethread;
	private JTextArea terminal;
	private String serverAddr=Constants.SERVER_IP;
	public ChatClient(JTextArea terminal,String serverAddr) {
		// TODO Auto-generated constructor stub
		this.terminal=terminal;
		this.serverAddr=serverAddr;
	}
	
	public void connect() throws InterruptedException, UnknownHostException, IOException{
		chatsocket=new Socket(serverAddr,Constants.CHAT_SERVER_PORT);
		sendthread=new ChatClientSendHandler(chatsocket);
		sendthread.start();
		receivethread=new ChatClientReceiveHandler(chatsocket,terminal,Constants.SERVER_IP);
		receivethread.start();
	}
	public void sendMessage(String message){
		sendthread.setMessage(message);
	}
	public void close() throws InterruptedException, IOException{
		sendthread.setRunning(false);
		sendthread.join();
		chatsocket.close();
	}
}
