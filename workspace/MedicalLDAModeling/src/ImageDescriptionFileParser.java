
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class ImageDescriptionFileParser {
	private static final String TAG_NAME_ARTICLE = "article";
	private static final String TAG_NAME_FIGURES = "figures";
	private static final String TAG_NAME_FIGURE = "figure";
	private static final String TAG_NAME_CAPTION = "caption";
	private static final String ATTR_NAME_IRI = "iri";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//System.out.println(hasImage("1297-9686-41-32-8.jpg"));
			parser("articles.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * 解析xml文件
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws FileNotFoundException 
	 */
	public static synchronized Map<String,String> parser(String fileName) throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {
		Map<String,String> map = new HashMap<String,String>();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document xmlDoc = builder.parse(new InputSource(new FileInputStream(fileName)));
        Element root = xmlDoc.getDocumentElement(); 
        NodeList articlesNodeList = root.getChildNodes();
        for (int i = 0; i < articlesNodeList.getLength(); i++){
        	String name = articlesNodeList.item(i).getNodeName().toString();
        	if(name.equals(TAG_NAME_ARTICLE)) {
        		NodeList figuresNodeList = articlesNodeList.item(i).getChildNodes();
        		for (int j = 0; j < figuresNodeList.getLength(); j++) {
        			name = figuresNodeList.item(j).getNodeName().toString();
        			if (name.equals(TAG_NAME_FIGURES)) {
        				NodeList figureNodeList = figuresNodeList.item(j).getChildNodes();
        				for (int k = 0; k < figureNodeList.getLength(); k++) {
        					name = figureNodeList.item(k).getNodeName().toString();
        					if (name.equals(TAG_NAME_FIGURE)) {
		        				Node figureNode = figureNodeList.item(k);
		        				String iri = figureNode.getAttributes().getNamedItem(ATTR_NAME_IRI).getNodeValue();
		  //      				System.out.println(iri);
		        				NodeList captionNodeList = figureNode.getChildNodes();
		        				for (int m = 0; m < captionNodeList.getLength(); m++) {
		        					name = captionNodeList.item(m).getNodeName().toString();
		        					if (name.equals(TAG_NAME_CAPTION) && captionNodeList.item(m).hasChildNodes()) {
		        						String caption = captionNodeList.item(m).getFirstChild().getNodeValue();
		 //       						System.out.println(caption);
		        						map.put(iri, caption);
		        					}
		        				}
        					}
        				}
        			}
        		}
        	}
        }
        return map;
	}
}
