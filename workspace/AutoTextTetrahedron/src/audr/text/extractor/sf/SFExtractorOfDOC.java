/**
 * 
 */
package audr.text.extractor.sf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import audr.text.summarizer.algorithm.LatentSemanticAnalysis;
import audr.text.summarizer.algorithm.RelevanceMeasure;
import audr.text.summarizer.document.ChineseDocumentHandler;
import audr.text.summarizer.document.Document;

/**
 * @author wanghan
 *
 */
public class SFExtractorOfDOC {
	public String ExtractLSASummary(){
		if(document!=null){
			String text = GetRawText();
			
			Document doc = new ChineseDocumentHandler(text).getDocument();

			ArrayList<String> summary=new LatentSemanticAnalysis().getSummary(doc);
			
			StringBuffer sb=new StringBuffer();
			for (String string : summary) {
				sb.append(string.trim()+"\n\r");
			}
			return sb.toString();
		}

		return "";
	}
	public String ExtractRMSummary(){
		if(document!=null){
			String text = GetRawText();
			
			Document doc = new ChineseDocumentHandler(text).getDocument();

			ArrayList<String> summary=new RelevanceMeasure().getSummary(doc);
			
			StringBuffer sb=new StringBuffer();
			for (String string : summary) {
				sb.append(string.trim()+"\n\r");
			}
			return sb.toString();
		}

		return "";
	}
	private String GetRawText() {
		Range range = document.getRange();
		String text = "";
		text = range.text().trim();
		return text;
	}
	
	
	private HWPFDocument document=null;
	public SFExtractorOfDOC(File docFile) throws IOException {
		// TODO Auto-generated constructor stub
		FileInputStream input = null;

		try{
			input = new FileInputStream( docFile );
			document = new HWPFDocument( input );
			
		}catch( Exception e)
		{
			System.err.println( docFile.getCanonicalPath() + "\t无法读取文档或文档已损坏" );
		}finally{
			if( null != input )
				input.close();
		}
	}
}
