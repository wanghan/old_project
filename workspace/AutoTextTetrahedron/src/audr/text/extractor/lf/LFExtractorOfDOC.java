/**
 * 
 */
package audr.text.extractor.lf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

/**
 * @author wanghan
 *
 */
public class LFExtractorOfDOC {
	public String ExtractLF(){
		if(document!=null){
			Range range = document.getRange();
			String text = "";
			text = range.text();
			return text;
		}

		return "";
	}
	
	private HWPFDocument document=null;
	public LFExtractorOfDOC(File docFile) throws IOException {
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
