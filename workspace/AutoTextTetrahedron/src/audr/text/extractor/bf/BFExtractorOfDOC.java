package audr.text.extractor.bf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.HWPFDocument;


/**
 * 
 * @author wanghan
 *
 */
public class BFExtractorOfDOC{
	
	public String ExtractAuthor(){
		if(sumInfo!=null){
			return sumInfo.getAuthor();
		}
		return null;
	}
	public String ExtractTitle(){
		if(sumInfo!=null){
			return sumInfo.getTitle();
		}
		return null;
	}
	public String ExtractTextType(){
		if(sumInfo!=null){
			return "doc";
		}
		return null;
	}
	public String ExtractSubject(){
		if(sumInfo!=null){
			return sumInfo.getSubject();
		}
		return null;
	}
	public Integer ExtractWordCount(){
		if(sumInfo!=null){
			return sumInfo.getWordCount();
		}
		return null;
	}
	private DocumentSummaryInformation docInfo=null;
	private SummaryInformation sumInfo=null;
	
	public BFExtractorOfDOC(File docFile) throws IOException{
		// TODO Auto-generated constructor stub
		FileInputStream input = null;
		HWPFDocument document = null;
		try{
			input = new FileInputStream( docFile );
			document = new HWPFDocument( input );
			docInfo = document.getDocumentSummaryInformation();
			sumInfo = document.getSummaryInformation();
			
		}catch( Exception e)
		{
			System.err.println( docFile.getCanonicalPath() + "\t无法读取文档或文档已损坏" );
		}finally{
			if( null != input )
				input.close();
		}
	}
}
