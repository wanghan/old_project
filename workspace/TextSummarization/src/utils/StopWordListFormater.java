package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class StopWordListFormater {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file=new File("./stopwordlist_c.txt");
		
		try {
			BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			FileWriter writer=new FileWriter(new File("./stopwordlist1.txt"));
			while(true){
				String line=reader.readLine();
			
				if(line==null){
					break;
				}
				StringTokenizer st=new StringTokenizer(line,", ");
				while(st.hasMoreTokens()){
					writer.append(st.nextToken());
					writer.append("\n");
					writer.flush();
				}
			}
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
