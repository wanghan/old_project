/**
 * 
 */
package audr.text.extractor.sf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

import audr.text.summarizer.algorithm.LatentSemanticAnalysis;
import audr.text.summarizer.algorithm.RelevanceMeasure;
import audr.text.summarizer.document.ChineseDocumentHandler;
import audr.text.summarizer.document.Document;

/**
 * @author wanghan
 *
 */
public class SFExtractorOfPPT {
	private String GetRawText(){
		StringBuffer pptContent = new StringBuffer();
		try{
			SlideShow slideShow = new SlideShow( document );
			Slide [] slides = slideShow.getSlides();
			int slidesLength = slides.length;
			for( int i = 0;i < slidesLength; i++ )
			{
				pptContent.append( slides[i].getTitle() );
				TextRun [] runs = slides[i].getTextRuns();
				int runsLength = runs.length;
				for( int j = 0; j < runsLength; j++ )
				{
					pptContent.append( runs[j].getText() );
					pptContent.append(" ");
				}
				
			}
			return pptContent.toString();
		}
		catch (Exception e) {
			// TODO: handle exception
			System.err.println(e.getMessage());
			return "";
		}
	}
	
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
	
	private HSLFSlideShow document=null;
	public SFExtractorOfPPT(File pptFile) throws IOException {
		// TODO Auto-generated constructor stub
		FileInputStream input = null;

		try{
			input = new FileInputStream( pptFile );
			document = new HSLFSlideShow( input );
			
		}catch( Exception e)
		{
			System.err.println( pptFile.getCanonicalPath() + "\t无法读取文档或文档已损坏" );
		}finally{
			if( null != input )
				input.close();
		}
	}
}
