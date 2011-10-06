/**
 * 
 */
package audr.text.extractor.lf;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import audr.text.extractor.config.LFAttribute;
import audr.text.extractor.config.TypeAttributes;
import audr.text.extractor.util.Utils;

/**
 * @author wanghan
 *
 */
public class LFExtractor {

private TypeAttributes typeAttributes;
	
	public LFExtractor() {
		// TODO Auto-generated constructor stub
		typeAttributes=TypeAttributes.getInstance();
	}
	
	public void Extract(File file, String lfPath){
		try {
			String filetype=Utils.getFileType(file.getCanonicalPath());
			LFAttribute attribute=typeAttributes.GetLFAttributes(filetype);
			
			Class extractClass=Class.forName(attribute.getClassName());
			Constructor cons=extractClass.getConstructor(new Class[]{file.getClass()});
			Method  extractMethod=extractClass.getMethod(attribute.getMethod(), new Class[]{});
			
			Object value= extractMethod.invoke(cons.newInstance(file), new Object[]{});
			
			FileWriter writer=new FileWriter(lfPath);
			
			
			if(value==null){
				writer.write("");
			}
			else{
				writer.write(value.toString());
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
