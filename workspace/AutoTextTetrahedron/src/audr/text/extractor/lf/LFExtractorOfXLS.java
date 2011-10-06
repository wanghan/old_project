/**
 * 
 */
package audr.text.extractor.lf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author wanghan
 *
 */
public class LFExtractorOfXLS {
	public String ExtractLF(){
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
	
	private HSSFWorkbook document=null;
	
	public LFExtractorOfXLS(File xlsFile) throws IOException {
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
