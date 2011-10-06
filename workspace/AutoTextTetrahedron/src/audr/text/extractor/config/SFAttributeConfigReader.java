/**
 * 
 */
package audr.text.extractor.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


/**
 * @author wanghan
 *
 */
public class SFAttributeConfigReader {

	private String FilePath;
	
	public SFAttributeConfigReader(String filepath){
		FilePath=filepath;
	}
	
	public Vector<SFAttribute> GetAttributes() throws FileNotFoundException, JDOMException, IOException{
		Vector<SFAttribute> results=new Vector<SFAttribute>();
		
		SAXBuilder builder=new SAXBuilder();
		Document doc=builder.build(new FileInputStream(FilePath));
		Element root = doc.getRootElement();

		List<Element> algList = root.getChildren();

		for (Element element : algList) {
			SFAttribute attribute=new SFAttribute();
			attribute.setName(element.getAttributeValue("name"));
			attribute.setType(element.getAttributeValue("type"));
			attribute.setMethod(element.getAttributeValue("method"));
			attribute.setClassName(element.getAttributeValue("classname"));
			results.add(attribute);
		}
		
		return results;
		
	}
	
}
