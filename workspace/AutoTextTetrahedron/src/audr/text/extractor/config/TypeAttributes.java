/**
 * 
 */
package audr.text.extractor.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
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
public class TypeAttributes {

	public static TypeAttributes instance=null;
	private HashMap<String, Vector<BFAttribute>> bfAttributesMap;
	private HashMap<String, Vector<SFAttribute>> sfAttributesMap;
	private HashMap<String, LFAttribute> lfAttributesMap;
	
	private TypeAttributes() {
		// TODO Auto-generated constructor stub
		String FilePath="configs/file_type.xml";
		
		bfAttributesMap=new HashMap<String, Vector<BFAttribute>>();
		lfAttributesMap=new HashMap<String, LFAttribute>();
		sfAttributesMap=new HashMap<String, Vector<SFAttribute>>();
		SAXBuilder builder=new SAXBuilder();
		Document doc=null;
		try {
			doc = builder.build(new FileInputStream(FilePath));
			Element root = doc.getRootElement();
			List<Element> children = root.getChildren();
			for (Element element : children) {
				Vector<BFAttribute> bfas=new BFAttributeConfigReader(element.getAttributeValue("bfconfig")).GetAttributes();
				bfAttributesMap.put(element.getAttributeValue("name"), bfas);
				
				Vector<SFAttribute> sfas=new SFAttributeConfigReader(element.getAttributeValue("sfconfig")).GetAttributes();
				sfAttributesMap.put(element.getAttributeValue("name"), sfas);
				
				LFAttribute lfas=new LFAttributeConfigReader(element.getAttributeValue("lfconfig")).GetAttributes();
				lfAttributesMap.put(element.getAttributeValue("name"), lfas);
			}
			
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
	
	public static TypeAttributes getInstance(){
		
		if(instance==null){
			instance=new TypeAttributes();
		}
		return instance;
	}
	
	public Vector<BFAttribute> GetBFAttributes(String type){
		if(type==null)
			return null;
		type=type.toLowerCase().trim();
		if(bfAttributesMap.containsKey(type)){
			return bfAttributesMap.get(type);
		}
		else{
			return null;
		}
	}
	
	public Vector<SFAttribute> GetSFAttributes(String type){
		if(type==null)
			return null;
		type=type.toLowerCase().trim();
		if(sfAttributesMap.containsKey(type)){
			return sfAttributesMap.get(type);
		}
		else{
			return null;
		}
	}
	public LFAttribute GetLFAttributes(String type){
		if(type==null)
			return null;
		type=type.toLowerCase().trim();
		if(lfAttributesMap.containsKey(type)){
			return lfAttributesMap.get(type);
		}
		else{
			return null;
		}
	}
}
