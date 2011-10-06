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
public class LFAttributeConfigReader {
	private String FilePath;
	
	public LFAttributeConfigReader(String filepath){
		FilePath=filepath;
	}
	
	public LFAttribute GetAttributes() throws FileNotFoundException, JDOMException, IOException{
		Vector<LFAttribute> results=new Vector<LFAttribute>();
		
		SAXBuilder builder=new SAXBuilder();
		Document doc=builder.build(new FileInputStream(FilePath));
		Element root = doc.getRootElement();

		List<Element> algList = root.getChildren();

		for (Element element : algList) {
			LFAttribute attribute=new LFAttribute();
			attribute.setMethod(element.getAttributeValue("method"));
			attribute.setClassName(element.getAttributeValue("classname"));
			results.add(attribute);
		}
		
		return results.get(0);
		
	}
}
