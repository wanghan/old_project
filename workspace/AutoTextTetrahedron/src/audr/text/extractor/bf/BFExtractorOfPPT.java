package audr.text.extractor.bf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hslf.HSLFSlideShow;


/**
 * 
 * @author wanghan
 *
 */
public class BFExtractorOfPPT{
	
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
			return "ppt";
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
//	private DocumentSummaryInformation docInfo=null;
	private SummaryInformation sumInfo=null;
	
	public BFExtractorOfPPT(File pptFile) throws IOException{
		// TODO Auto-generated constructor stub
		FileInputStream input = null;
		HSLFSlideShow document = null;
		try{
			input = new FileInputStream( pptFile );
			document = new HSLFSlideShow( input );
//			docInfo = document.getDocumentSummaryInformation();
			sumInfo = document.getSummaryInformation();
			
		}catch( Exception e)
		{
			if(!pptFile.exists()){
				System.err.println( pptFile.getCanonicalPath() + "文档不存在");
			}
			else{
				System.err.println( pptFile.getCanonicalPath() + "\t无法读取文档或文档已损坏" );
			}
			e.printStackTrace();
			
		}finally{
			if( null != input )
				input.close();
		}
		

		

	}
}
