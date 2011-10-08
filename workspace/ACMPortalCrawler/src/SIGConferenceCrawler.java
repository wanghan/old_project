import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.util.NodeList;

/**
 * 
 */

/**
 * @author wanghan
 * 
 */
public class SIGConferenceCrawler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Conference conf=new Conference();
		conf.setName("SIGMOD_09");
		Calendar calendar=Calendar.getInstance();
		calendar.set(2010, 6, 1);
		conf.setDate(calendar.getTime());
		new SIGConferenceCrawler("C:\\Users\\wanghan\\workspace\\ACMPortalCrawler\\link\\SIGMOD_09.htm",conf).crawling();
	}

	private String pageLink =null;
	private Conference conference=null;
	
	public SIGConferenceCrawler(String link,Conference conf) {
		// TODO Auto-generated constructor stub
		this.pageLink=link;
		this.conference=conf;
	}

	public void crawling() {
		try {
			System.out.println(conference.getDate().toString());
			Parser parser = new Parser(pageLink);
			NodeList nodes = parser.parse(null);
			travelAllTDNodes(nodes);

			Paper paper = null;
			ArrayList<Paper> papers = new ArrayList<Paper>();
			try {
				for (int i = 0; i < tdNodes.size(); ++i) {
					TableColumn node = tdNodes.elementAt(i);
					if (node.getAttribute("colspan") != null
							&& node.getAttribute("colspan").equals("1")) {
						paper = new Paper();
						linkNodes.clear();
						travelAllLinkNodes(node.getChildren());
						String title = node.toPlainTextString().trim();
						
						paper.setSource("http://portal.acm.org/"+linkNodes.get(0).getAttribute("href"));
						paper.setTitle(title);
						i += 2;

						node = tdNodes.elementAt(i);
						Vector<Author> authors=extractAuthors(node);
						paper.setAuthors(authors);
						i+=2;
						
						paper.setPages(tdNodes.elementAt(i).toPlainTextString().trim());
						i+=2;
						
						this.linkNodes.clear();
						travelAllLinkNodes(tdNodes.elementAt(i).getChildren());
						if(linkNodes.size()==0){
							continue;
						}
						paper.setDoi(linkNodes.get(0).toPlainTextString().trim());
						paper.setDoiLink(linkNodes.get(0).getAttribute("href").trim());
						i+=2;
						
						this.linkNodes.clear();
						travelAllLinkNodes(tdNodes.elementAt(i).getChildren());
						if(linkNodes.size()==0){
							continue;
						}
						if(linkNodes.get(0).getAttribute("name")==null){
							continue;
						}
						paper.setLink("http://portal.acm.org/"+linkNodes.get(0).getAttribute("href"));
						i+=2;
						
						if(tdNodes.elementAt(i).toPlainTextString().trim().startsWith("Other formats")){
							i+=2;

						}
						i+=2;
						spanNodes.clear();
						travelAllSpanNodes(tdNodes.elementAt(i).getChildren());
						for (Span span : spanNodes) {
							if(span.getAttribute("id").startsWith("toHide")){
								paper.setAbstractContent(span.toPlainTextString().trim());
								break;
							}
						}
							
						if(paper.getAbstractContent()!=null){
							paper.setConference(conference);
							papers.add(paper);
						}
						
					}
					else if(node.getAttribute("colspan") != null){
						continue;
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				// TODO: handle exception
				
			}
			
			
			Document xmlDocument = DocumentHelper.createDocument();
			Element root = xmlDocument.addElement("metadata");
			for (Paper paper2 : papers) {
				
				root.add(paper2.toXMLElement());
			}
			
	//		OutputStreamWriter out=new   OutputStreamWriter(new   FileOutputStream( "metadata/"+conference.getName()+".xml"), "UTF-8");
			
			FileWriter out = new FileWriter("metadata/"+conference.getName()+".xml");
			OutputFormat format=new OutputFormat();
			format.setNewlines(true);
			format.setEncoding("UTF-8");
			XMLWriter  writer=new XMLWriter(out,format);
			writer.write(xmlDocument);
			
			writer.flush();
			writer.close();
			out.close();
			
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testReadXMLFile() throws DocumentException {
		//test read xml file
		SAXReader reader=new SAXReader();
		Document xmldoc=reader.read(new File("metadata/SIGMOD 10.xml"));
		Element root1=xmldoc.getRootElement();
		for (Object elem : root1.elements()) {
			Paper ppp=Paper.xmlToInstance((Element)elem);
			System.out.println(ppp.getTitle());
		}
		System.out.println(tdNodes.size());
	}

	private Vector<TableColumn> tdNodes = new Vector<TableColumn>();
	private Vector<LinkTag> linkNodes = new Vector<LinkTag>();
	private Vector<Span> spanNodes=new Vector<Span>();
	
	private void travelAllSpanNodes(NodeList nodes) {
		for (Node node : nodes.toNodeArray()) {
			if (node instanceof Span) {
				spanNodes.add((Span) node);
			} else if (node.getChildren() != null) {
				travelAllSpanNodes(node.getChildren());
			}
		}
	}

	private void travelAllLinkNodes(NodeList nodes) {
		for (Node node : nodes.toNodeArray()) {
			if (node instanceof LinkTag) {
				linkNodes.add((LinkTag) node);
			} else if (node.getChildren() != null) {
				travelAllLinkNodes(node.getChildren());
			}
		}
	}

	private void travelAllTDNodes(NodeList nodes) {
		for (Node node : nodes.toNodeArray()) {
			if (node instanceof TableColumn) {
				tdNodes.add((TableColumn) node);
			} else if (node.getChildren() != null) {
				travelAllTDNodes(node.getChildren());
			}
		}
	}

	private Vector<Author> extractAuthors(TableColumn node){
		linkNodes.clear();
		Vector<Author> result=new Vector<Author>();
		travelAllLinkNodes(node.getChildren());
		for (LinkTag link : linkNodes) {
			Author author=new Author();
			author.setName(link.toPlainTextString().trim());
			author.setLink("http://portal.acm.org/"+link.getAttribute("href").trim());
			result.add(author);
		}
		return result;
	}
	
	

}
