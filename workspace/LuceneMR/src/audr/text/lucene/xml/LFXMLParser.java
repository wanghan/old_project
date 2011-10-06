/**
 * 
 */
package audr.text.lucene.xml;

import java.util.Iterator;

import org.apache.hadoop.fs.FSDataInputStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author wanghan
 *
 */
public class LFXMLParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	public static String parse(FSDataInputStream inStream){
		SAXReader reader=new SAXReader();
		Document doc;
		try {
			doc = reader.read(inStream);
			Element root=doc.getRootElement();
			for ( Iterator<Element> i = root.elementIterator(); i.hasNext(); ) {
				Element element = i.next();
				if(element.getName()=="contents"){
					return element.getText();
				}
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return null;
	}

}
