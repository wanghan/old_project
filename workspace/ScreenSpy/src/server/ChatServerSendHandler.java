package server;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class ChatServerSendHandler implements Runnable{

	ChatServer father;
	private JTextArea terminal;
	
	public ChatServerSendHandler(JTextArea tt,ChatServer fa) {
		// TODO �Զ����ɹ��캯�����
		terminal=tt;
		father=fa;
	}
	public void run() {
		while(true){
			while(father.getMessage()==null){
				try{
					Thread.sleep(100);
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			terminal.append("You say��"+father.getMessage()+"\n");
			try{
				//���������ӿͻ��˽���Ⱥ��
				for(int i=0;i<father.getClients().size();++i){
					Socket s=father.getClients().get(i);
					OutputStream output=s.getOutputStream();
					PrintWriter out=new PrintWriter(output,true);		
					out.println(father.getMessage());
				}
				father.setMessage(null);
				Thread.sleep(100);
			}

			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
}