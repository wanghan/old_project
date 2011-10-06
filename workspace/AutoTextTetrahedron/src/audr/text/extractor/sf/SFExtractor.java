package audr.text.extractor.sf;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import audr.text.extractor.config.BFAttribute;
import audr.text.extractor.config.SFAttribute;
import audr.text.extractor.config.TypeAttributes;
import audr.text.extractor.util.Utils;

/**
 * 
 * @author wanghan
 *
 */
public class SFExtractor {
	
	private TypeAttributes typeAttributes;
	
	public SFExtractor() {
		// TODO Auto-generated constructor stub
		typeAttributes=TypeAttributes.getInstance();
	}
	
	
	public void Extract(File file, String bfPath) throws Exception{
		String filetype=Utils.getFileType(file.getCanonicalPath());
		Vector<SFAttribute> attributes=typeAttributes.GetSFAttributes(filetype);
		
		Document doc=new Document();
		Element root=new Element("SFs");
		doc.setRootElement(root);
		
		Element sfelement=new Element("SF");
		for (SFAttribute attribute : attributes) {
			
			Class extractClass=Class.forName(attribute.getClassName());
			Constructor cons=extractClass.getConstructor(new Class[]{file.getClass()});
			Method  extractMethod=extractClass.getMethod(attribute.getMethod(), new Class[]{});
			
			Object value= extractMethod.invoke(cons.newInstance(file), new Object[]{});
			
			Element element=new Element(attribute.getName());
			if(value==null){
				element.setText(null);
			}
			else{
				
				element.setText(value.toString());
			}
			
			sfelement.addContent(element);
			
		}
		if(sfelement!=null)
			root.addContent(sfelement);
		//output bf file 
		XMLOutputter outputter = null;
	    Format format = Format.getCompactFormat();
	    format.setEncoding("utf-8"); 
	    outputter = new XMLOutputter(format);
        outputter.output(doc, new FileOutputStream(bfPath)); 
        
	}
	
	
	public void Extract(File[] files, String bfPath){
		
		
		Document doc=new Document();
		Element root=new Element("SFs");
		doc.setRootElement(root);
		
		for (File file : files) {
			Element sfelement=null;
			try {
				String filetype=Utils.getFileType(file.getCanonicalPath());
				Vector<SFAttribute> attributes=typeAttributes.GetSFAttributes(filetype);
				
				sfelement=new Element("SF");
				sfelement.setAttribute("file", file.getName());
				for (SFAttribute attribute : attributes) {
					
					Class extractClass=Class.forName(attribute.getClassName());
					Constructor cons=extractClass.getConstructor(new Class[]{file.getClass()});
					Method  extractMethod=extractClass.getMethod(attribute.getMethod(), new Class[]{});

					Object value= extractMethod.invoke(cons.newInstance(file), new Object[]{});
					
					Element element=new Element(attribute.getName());
					if(value==null){
						element.setText(null);
					}
					else{
						
						element.setText(value.toString());
					}
					
					sfelement.addContent(element);
					
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println(file.getAbsolutePath());
				e.printStackTrace(System.err);
			}
			
			if(sfelement!=null){
				root.addContent(sfelement);
			}

			
		}
		//output bf file 
		XMLOutputter outputter = null;
	    Format format = Format.getCompactFormat();
	    format.setEncoding("utf-8");
	    format.setIndent(" ");
	    outputter = new XMLOutputter(format);
	    try {
	    	outputter.output(doc, new FileOutputStream(bfPath)); 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        
	}
}
