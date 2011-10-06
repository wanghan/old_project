/**
 * 
 */
package audr.text.extractor.lf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import audr.text.extractor.util.InfoUtil;
import audr.text.extractor.util.Utils;

/**
 * @author wanghan
 *
 */
public class LFExtractorOfHTML {
	public String ExtractLF(){
		return content;
	}
	
	private String content="";
	
	public LFExtractorOfHTML(File htmlFile) throws Exception {
		try {
			// TODO Auto-generated constructor stub
			FileReader fileReader = null; 
			BufferedReader reader = null ;
			StringBuffer text = new StringBuffer();
			fileReader = new FileReader( htmlFile );
			reader = new BufferedReader( fileReader );
			while( reader.ready() )
			{
				text.append( reader.readLine());
			}
			
			this.content = text.toString().toLowerCase();
			content = Utils.removeBetweenter( content, "<!--", "-->");
			content = Utils.removeBetweenter( content, "<meta", "</meta>");
			content = Utils.removeBetweenter( content, "<script", "</script>");
			content = Utils.removeBetweenter( content, "<style", "</style>");
			content = Utils.removeSpecSymbol( content, "nsp;");
			content = Utils.removeSpecSymbol( content, "copy;");
			content = Utils.removeSpecSymbol( content, "nbsp");
			content = InfoUtil.removeBetweenter( content, "<title", "</title>");
			content = InfoUtil.removeSpecSymbol(content);
			content = content.replaceAll("&gt", "");
			
			if( null != reader )
				reader.close();
			if( null != fileReader )
				fileReader.close();
		} catch (Exception e) {
			// TODO: handle exception
			content="";
			throw e;
		}
		
	}
}
