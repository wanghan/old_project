package server;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JTabbedPane;

import ui.ScreenPanel;
import utils.Command;

/**
 * @author wanghan
 *
 */
public class ScreenSpyServerHandler implements Runnable{

	private JTabbedPane tabbedPane;
	private Socket socket;
	private ScreenPanel component;
	public ScreenSpyServerHandler(JTabbedPane tabbedPane,ScreenPanel com,Socket client) {
		// TODO Auto-generated constructor stub
		this.socket=client;
		this.component=com;
		this.tabbedPane=tabbedPane;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			ObjectInputStream ois=null;
			try {
				//���������
				ois=new ObjectInputStream(socket.getInputStream());
				while(true){
					int message=ois.readInt();
					//Э�飬��һ������ʱ��Ļ����
					if(message==Command.ONLINE){
						
						
						int len=ois.readInt();
						
						//��ȡ�ֽ���
						byte[] data=new byte[len];
						data=(byte[])ois.readObject();
						
						//��ԭBufferedImage����
						ByteArrayInputStream bis=new ByteArrayInputStream(data);
						BufferedImage image=ImageIO.read(bis);
						//ͼ������ţ���Ӧϵͳ��ʾ
						Image newImage=image.getScaledInstance(800, 600,Image.SCALE_SMOOTH);
						component.setImage(newImage);
					}
					//Э�飬�Ͽ�����
					else if(message==Command.CLOSE){
						ois.close();
						tabbedPane.remove(component);
						return ;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					if(ois!=null)
						ois.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
}
