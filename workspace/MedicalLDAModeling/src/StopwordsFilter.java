

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * @author wanghan
 *
 */
public class StopwordsFilter {

	private HashSet<String> stopwordSet;
	private static StopwordsFilter instance=null;
	
	public static StopwordsFilter GetInstance(){
		if(instance==null){
			instance=new StopwordsFilter();
		}
		return instance;
	}
	
	private StopwordsFilter() {
		// TODO Auto-generated constructor stub
		this.stopwordSet=new HashSet<String>();
		
		try {
			BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream("Resource/StopWordListE.txt")));
			while(true){
				String line=reader.readLine();
				if(line==null)
					break;
				stopwordSet.add(line.trim());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public boolean IsStopword(String s){
		if(s.length()==0){
			return true;
		}
		return stopwordSet.contains(s);
	}
}
