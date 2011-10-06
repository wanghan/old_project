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
				//创建输出流
				ois=new ObjectInputStream(socket.getInputStream());
				while(true){
					int message=ois.readInt();
					//协议，下一个数据时屏幕数据
					if(message==Command.ONLINE){
						
						
						int len=ois.readInt();
						
						//读取字节流
						byte[] data=new byte[len];
						data=(byte[])ois.readObject();
						
						//还原BufferedImage对象
						ByteArrayInputStream bis=new ByteArrayInputStream(data);
						BufferedImage image=ImageIO.read(bis);
						//图像的缩放，适应系统显示
						Image newImage=image.getScaledInstance(800, 600,Image.SCALE_SMOOTH);
						component.setImage(newImage);
					}
					//协议，断开连接
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
