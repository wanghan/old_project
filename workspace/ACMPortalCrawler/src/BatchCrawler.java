import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.StringTokenizer;

import webutils.WebpageDownloader;

/**
 * 
 */

/**
 * @author wanghan
 *
 */
public class BatchCrawler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		try {
			
	//		Thread.sleep(6000000);
			
			Scanner scanner=new Scanner(new File("ACMLinks.txt"));
			
			while(scanner.hasNext()){
				String line=scanner.nextLine();
				StringTokenizer st=new StringTokenizer(line," ");
				String firstToken=st.nextToken();
				if(firstToken.equals("#")){
					continue;
				}
				int year=Integer.parseInt(firstToken);
				int month=Integer.parseInt(st.nextToken());
				String confName=st.nextToken().trim();
				String link=st.nextToken().trim();
				
				System.out.println("Downloading: "+confName);
				
				WebpageDownloader downloader=new WebpageDownloader();
				downloader.setWebAddress(link);
				downloader.setDestFile("link/"+confName+".htm");
				downloader.download();
				
				Conference conf=new Conference();
				conf.setName(confName);
				Calendar calendar=Calendar.getInstance();
				calendar.set(year, month-1, 1);
				conf.setDate(calendar.getTime());
				new SIGConferenceCrawler("link/"+confName+".htm",conf).crawling();
				
//				Thread.sleep(300000);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
