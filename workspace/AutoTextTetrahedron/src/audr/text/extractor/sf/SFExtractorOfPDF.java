/**
 * 
 */
package audr.text.extractor.sf;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import audr.text.summarizer.algorithm.LatentSemanticAnalysis;
import audr.text.summarizer.algorithm.RelevanceMeasure;
import audr.text.summarizer.document.ChineseDocumentHandler;
import audr.text.summarizer.document.Document;


/**
 * @author wanghan
 *
 */
public class SFExtractorOfPDF {
	private String GetRawText(){
		content=content.replace("\r", "");
		content=content.replace("\n", "");
		content=content.replace(" ", "");
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
	
	
	private String content = "";
	
	public SFExtractorOfPDF(File pdfFile) throws Exception {
		// TODO Auto-generated constructor stub
		// TODO Auto-generated constructor stub
		FileInputStream input = null;
		PDDocument document = null;
		try{
			input = new FileInputStream( pdfFile );
			document = PDDocument.load( input );
		}catch( Exception e)
		{
			System.err.println( pdfFile + "       文件无法打开或已损坏" );
			if( null != document )
				document.close();
			if( null != input )
				input.close();
			throw e;
		}

		if( document.isEncrypted()) 
		{	
			System.err.println( pdfFile + "        加密文件" );
			if( null != document )
				document.close();
			if( null != input )
				input.close();
			throw new Exception(pdfFile + "        加密文件");
		}
		
		PDFTextStripper text = new PDFTextStripper();
		this.content=text.getText(document);
		document.close();
	}
}
