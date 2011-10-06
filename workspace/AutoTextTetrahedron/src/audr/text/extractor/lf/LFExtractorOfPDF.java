/**
 * 
 */
package audr.text.extractor.lf;

import java.io.File;
import java.io.FileInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;


/**
 * @author wanghan
 *
 */
public class LFExtractorOfPDF {
	public String ExtractLF(){
		return content;
	}
	
	private String content = "";
	
	public LFExtractorOfPDF(File pdfFile) throws Exception {
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
