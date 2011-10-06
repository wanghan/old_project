package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JTextArea;

import utils.Command;

public class ChatServerReceiveHandler implements Runnable {
	
	
	private JTextArea terminal;
	private Socket client;
	
	public ChatServerReceiveHandler(JTextArea terminal,Socket client) {
		// TODO Auto-generated constructor stub
		this.client=client;
		this.terminal=terminal;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String clientAddr=client.getInetAddress().toString();
			InputStream input=client.getInputStream();
			Scanner in=new Scanner(input);
			while(in.hasNextLine()){
				String message=in.nextLine();
				if(message.equals(String.valueOf(Command.CLOSE))){
					//断开连接
					terminal.append(clientAddr+" disconnect");
					break;
					
				}
				else{
					//显示消息
					terminal.append(clientAddr+" says: "+message+"\n");
				}
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
