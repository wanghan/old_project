package client;

import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JTextArea;

public class ChatClientReceiveHandler extends Thread {
	Socket client;
	String serverAddr;
	private JTextArea terminal;
	public ChatClientReceiveHandler(Socket s,JTextArea tt,String fa) {
		client=s;
		terminal=tt;
		serverAddr=fa;
	}
	public void run() {
		// TODO 自动生成方法存根
		try{
			InputStream input=client.getInputStream();
			Scanner in=new Scanner(input);
			while(in.hasNextLine()){
				//显示从服务器端接收到的消息
				terminal.append(serverAddr+"："+in.nextLine()+"\n");
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
