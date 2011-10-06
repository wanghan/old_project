/**
 * 
 */
package audr.text.extractor.sf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import audr.text.extractor.util.InfoUtil;
import audr.text.extractor.util.Utils;
import audr.text.summarizer.algorithm.LatentSemanticAnalysis;
import audr.text.summarizer.algorithm.RelevanceMeasure;
import audr.text.summarizer.document.ChineseDocumentHandler;
import audr.text.summarizer.document.Document;

/**
 * @author wanghan
 *
 */
public class SFExtractorOfHTML {
	
	private String GetRawText(){
		return content;
	}
	
	public String ExtractLSASummary(){
		String text = GetRawText();
		
		Document doc = new ChineseDocumentHandler(text).getDocument();

		ArrayList<String> summary=new LatentSemanticAnalysis().getSummary(doc);
		
		StringBuffer sb=new StringBuffer();
		for (String string : summary) {
			sb.append(string.trim()+"\n\r");
		}
		return sb.toString();
	}
	public String ExtractRMSummary(){
		String text = GetRawText();
		
		Document doc = new ChineseDocumentHandler(text).getDocument();

		ArrayList<String> summary=new RelevanceMeasure().getSummary(doc);
		
		StringBuffer sb=new StringBuffer();
		for (String string : summary) {
			sb.append(string.trim()+"\n\r");
		}
		return sb.toString();
	}
	
	private String content="";
	
	public SFExtractorOfHTML(File htmlFile) throws Exception {
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
