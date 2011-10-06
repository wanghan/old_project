package audr.text.extractor.bf;

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
import audr.text.extractor.config.TypeAttributes;
import audr.text.extractor.util.Utils;

/**
 * 
 * @author wanghan
 *
 */
public class BFExtractor {
	
	private TypeAttributes typeAttributes;
	
	public BFExtractor() {
		// TODO Auto-generated constructor stub
		typeAttributes=TypeAttributes.getInstance();
	}
	
	
	public void Extract(File file, String bfPath) throws Exception{
		String filetype=Utils.getFileType(file.getCanonicalPath());
		Vector<BFAttribute> attributes=typeAttributes.GetBFAttributes(filetype);
		
		Document doc=new Document();
		Element root=new Element("BFs");
		doc.setRootElement(root);
		
		Element bfelement=new Element("BF");
		for (BFAttribute attribute : attributes) {
			
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
			
			bfelement.addContent(element);
			
		}
		if(bfelement!=null)
			root.addContent(bfelement);
		//output bf file 
		XMLOutputter outputter = null;
	    Format format = Format.getCompactFormat();
	    format.setEncoding("utf-8"); 
	    outputter = new XMLOutputter(format);
        outputter.output(doc, new FileOutputStream(bfPath)); 
        
	}
	
	
	public void Extract(File[] files, String bfPath){
		
		
		Document doc=new Document();
		Element root=new Element("BFs");
		doc.setRootElement(root);
		
		for (File file : files) {
			Element bfelement=null;
			try {
				String filetype=Utils.getFileType(file.getCanonicalPath());
				Vector<BFAttribute> attributes=typeAttributes.GetBFAttributes(filetype);
				
				bfelement=new Element("BF");
				bfelement.setAttribute("file", file.getName());
				for (BFAttribute attribute : attributes) {
					
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
					
					bfelement.addContent(element);
					
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println(e.getMessage());
				bfelement=null;
			}
			
			if(bfelement!=null){
				root.addContent(bfelement);
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
