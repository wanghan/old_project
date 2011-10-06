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
public class BFAttributeConfigReader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Vector<BFAttribute> aa=new BFAttributeConfigReader("configs/bf_doc.xml").GetAttributes();
			System.out.println(1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private String FilePath;
	
	public BFAttributeConfigReader(String filepath){
		FilePath=filepath;
	}
	
	public Vector<BFAttribute> GetAttributes() throws FileNotFoundException, JDOMException, IOException{
		Vector<BFAttribute> results=new Vector<BFAttribute>();
		
		SAXBuilder builder=new SAXBuilder();
		Document doc=builder.build(new FileInputStream(FilePath));
		Element root = doc.getRootElement();

		List<Element> algList = root.getChildren();

		for (Element element : algList) {
			BFAttribute attribute=new BFAttribute();
			attribute.setName(element.getAttributeValue("name"));
			attribute.setType(element.getAttributeValue("type"));
			attribute.setMethod(element.getAttributeValue("method"));
			attribute.setClassName(element.getAttributeValue("classname"));
			results.add(attribute);
		}
		
		return results;
		
	}
	
}
