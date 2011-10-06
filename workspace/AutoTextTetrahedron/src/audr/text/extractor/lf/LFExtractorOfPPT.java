/**
 * 
 */
package audr.text.extractor.lf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

/**
 * @author wanghan
 *
 */
public class LFExtractorOfPPT {
	public String ExtractLF(){
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
	
	private HSLFSlideShow document=null;
	public LFExtractorOfPPT(File pptFile) throws IOException {
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
