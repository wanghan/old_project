package audr.text.extractor.bf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import audr.text.extractor.util.InfoUtil;
import audr.text.extractor.util.Utils;


/**
 * 
 * @author wanghan
 *
 */
public class BFExtractorOfHTML{
	

	public String ExtractTitle(){
		return getHtmlTitle(content);
	}
	public String ExtractTextType(){
		
		return "html";
	}
	
	
	private String content;

	
	public BFExtractorOfHTML(File htmlFile) throws Exception{
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
		
		
		if( null != reader )
			reader.close();
		if( null != fileReader )
			fileReader.close();
	}
	
	/**
	 * 获取html中的title标签
	 * @param text
	 * @return
	 * @throws Exception
	 */
	private String getHtmlTitle( String text )
	{
		String title = "";
		try{
			String regex = "(<title.*?</title>)";
			text = Utils.removeByRegex( text, regex );
			title = InfoUtil.removeSpecSymbol( text );
		}catch( Exception e )
		{
			System.err.println(e.getMessage());
			return "";
		}
		return title;
	}
}
