package webutils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebpageDownloader{

	private String webAddress = "";
	private String destFile = "";
	
	private int BUFFER_SIZE=1024;
	private byte[] buffer;

	public WebpageDownloader() {
		// TODO Auto-generated constructor stub
		this.buffer=new byte[BUFFER_SIZE];
	}
	
	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public void setDestFile(String destFile) {
		this.destFile = destFile;
	}

	public boolean download() throws IOException, InterruptedException {

		HttpURLConnection httpConn = null;

		try {
			URL url = new URL(webAddress);

			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn
					.setRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14");
			InputStream in = httpConn.getInputStream();
			DataInputStream input=new DataInputStream(in);
			FileOutputStream out = new FileOutputStream(new File(destFile));

			int len=-1;
			while ((len=input.read(buffer)) != -1) {
				out.write(buffer, 0, len);
				out.flush();
			}

			out.close();
			input.close();

		} catch (Exception ex) {
			System.out.println(ex.toString());
		} finally {
			httpConn.disconnect();
		}
		return true;
	}
	
	public static void main(String[] args) {
		WebpageDownloader downloader=new WebpageDownloader();
		downloader.setWebAddress("http://portal.acm.org/citation.cfm?id=1559845&CFID=12823830&CFTOKEN=49010301&preflayout=flat");
		downloader.setDestFile("link/SIGMOD_09.htm");
		try {
			downloader.download();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}