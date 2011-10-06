package audr.text.summarizer.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

public class StopWordFilter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	private static String STOPWORD_LIST_FILEPATH="./Resources/stopwordlist.txt";
	

	private static StopWordFilter instance=null;
	private HashSet<String> stopwordList;
	public static StopWordFilter getInstance(){
		if(instance==null){
			instance=new StopWordFilter();
		}
		return instance;
	}
	
	public StopWordFilter() {
		// TODO Auto-generated constructor stub
		File file=new File(STOPWORD_LIST_FILEPATH);
		stopwordList=new HashSet<String>();
		try {
			BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			while(true){
				String line=reader.readLine();
				if(line==null){
					break;
				}
				else if(line.length()>0){
					stopwordList.add(line.trim());
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	public boolean isStopWord(String s){
		return stopwordList.contains(s);
	}
}
