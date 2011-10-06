package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class StopWordHandler {
	
	private static StopWordHandler instance=null;
	private HashSet<String> stopwords;
	private StopWordHandler() {
		// TODO Auto-generated constructor stub
		
		
		File stopwordlistFile=new File("src/stopwordslist.txt");
		this.stopwords=new HashSet<String>();
		try {
			BufferedReader reader=new BufferedReader(
					new InputStreamReader(
							new FileInputStream(stopwordlistFile)));
			
			while(true){
				String line=reader.readLine();
				if(line==null){
					break;
				}
				else{
					this.stopwords.add(line.trim());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public boolean isStopWord(String word){
		return stopwords.contains(word.trim());
	}
	
	
	public static StopWordHandler getInstance(){
		if(instance==null){
			instance=new StopWordHandler();
		}
		return instance;
	}
}
