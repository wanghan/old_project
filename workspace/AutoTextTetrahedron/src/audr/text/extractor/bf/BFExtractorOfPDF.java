package audr.text.extractor.bf;

import java.io.File;
import java.io.FileInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;



/**
 * 
 * @author wanghan
 *
 */
public class BFExtractorOfPDF{
	
	public String ExtractAuthor(){
		if(pdfInfo!=null){
			return pdfInfo.getAuthor();
		}
		return null;
	}
	public String ExtractTitle(){
		if(pdfInfo!=null){
			return pdfInfo.getTitle();
		}
		return null;
	}
	public String ExtractTextType(){
		if(pdfInfo!=null){
			return "pdf";
		}
		return null;
	}
	public String ExtractSubject(){
		if(pdfInfo!=null){
			return pdfInfo.getSubject();
		}
		return null;
	}

	private PDDocumentInformation pdfInfo=null;
	
	public BFExtractorOfPDF(File pdfFile) throws Exception{
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
		pdfInfo = document.getDocumentInformation();
		document.close();
	}
}
