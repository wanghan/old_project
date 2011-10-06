package client;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

import utils.Command;
import utils.Constants;

public class ScreenSpyClientHandler extends Thread{
	
	private Socket client;
	private boolean isRunning;
	public ScreenSpyClientHandler(Socket client) {
		// TODO Auto-generated constructor stub
		this.client=client;
		this.isRunning=true;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Toolkit tool=Toolkit.getDefaultToolkit();
			ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());
			while(isRunning){
				
				oos.writeInt(Command.ONLINE);
				oos.flush();
				//抓取屏幕
				BufferedImage image=new Robot().createScreenCapture(new Rectangle(tool.getScreenSize()));
				ByteArrayOutputStream bos=new ByteArrayOutputStream();
				//封装成字节流
				ImageIO.write(image, "jpg", bos);
			
				byte[] data=bos.toByteArray();
				//发送字节流长度
				oos.writeInt(data.length);
				oos.flush();
				//发送字节流
				oos.writeObject(data);
				oos.flush();
				//发送线程休眠1s
				Thread.sleep(Constants.REFRESH_RATE);
			}
			oos.writeInt(Command.CLOSE);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
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

}
