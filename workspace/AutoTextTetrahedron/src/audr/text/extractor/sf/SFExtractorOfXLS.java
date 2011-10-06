/**
 * 
 */
package audr.text.extractor.sf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import audr.text.summarizer.algorithm.LatentSemanticAnalysis;
import audr.text.summarizer.algorithm.RelevanceMeasure;
import audr.text.summarizer.document.ChineseDocumentHandler;
import audr.text.summarizer.document.Document;

/**
 * @author wanghan
 *
 */
public class SFExtractorOfXLS {
	private String GetRawText(){
		StringBuffer xlsContent = new StringBuffer();
		try{
			int total = document.getNumberOfSheets();
			for( int i = 0; i < total; i++ )
			{
				HSSFSheet sheet = document.getSheetAt( i );
				if( null == sheet )
					continue;
				int r = sheet.getLastRowNum() + 1;
				for( int j = 0; j < r; j++ )
				{
					HSSFRow row = sheet.getRow( j );
					if( null == row )
						continue;
					int c = row.getLastCellNum() + 1;
					for( int k = 0; k < c; k++ )
					{
						HSSFCell cell = row.getCell( k );
						if( null == cell )
							continue;
						xlsContent.append( cell.toString() );
						xlsContent.append(" ");
					}
					xlsContent.append(" ");
				}
			}
			return xlsContent.toString();
		}
		catch (Exception e) {
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
	
	
	private HSSFWorkbook document=null;
	
	public SFExtractorOfXLS(File xlsFile) throws IOException {
		// TODO Auto-generated constructor stub
		FileInputStream input = null;

		try{
			input = new FileInputStream( xlsFile );
			document = new HSSFWorkbook( input );
			
		}catch( Exception e)
		{
			System.err.println( xlsFile.getCanonicalPath() + "\t无法读取文档或文档已损坏" );
		}finally{
			if( null != input )
				input.close();
		}
	}
}
