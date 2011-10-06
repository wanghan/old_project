package client;

import java.awt.HeadlessException;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


import utils.Command;

public class ChatClientSendHandler extends Thread{
	private Socket client;
	private boolean isRunning;
	private String message;
	public ChatClientSendHandler(Socket client) {
		// TODO Auto-generated constructor stub
		this.client=client;
		this.isRunning=true;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			OutputStream output=client.getOutputStream();
			PrintWriter out=new PrintWriter(output,true);
			while(isRunning){
				if(message!=null){
					//��ö���������ֹͬ����������
					synchronized (message) {
						//������Ϣ
						out.println(message);
						message=null;
					}
					
				}
				else{
					
				}
				
			}
			out.println(Command.CLOSE);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	public  String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
