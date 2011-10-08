import java.util.Date;
import java.util.Vector;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

/**
 * 
 */

/**
 * @author wanghan
 *
 */
public class Paper {
	
	private String title;
	private Vector<Author> authors;
	private String pages;
	private String abstractContent;
	private String source;
	private String link;
	private String doi;
	private String doiLink;
	private Conference conference;
	
	public Paper() {
		// TODO Auto-generated constructor stub
		this.doi="";
		this.doiLink="";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Vector<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Vector<Author> authors) {
		this.authors = authors;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getAbstractContent() {
		return abstractContent;
	}

	public void setAbstractContent(String abstractContent) {
		this.abstractContent = abstractContent;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getDoiLink() {
		return doiLink;
	}

	public void setDoiLink(String doiLink) {
		this.doiLink = doiLink;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}
	
	public Element toXMLElement(){
		Element root=new BaseElement("article");
		Element titleNode=root.addElement("title");
		titleNode.addText(this.title);
		
		Element confNode=root.addElement("conference");
		confNode.addText(this.conference.getName());
		confNode.addAttribute("date", conference.getDate().toString());
		
		Element pageNode=root.addElement("page");
		pageNode.addText(this.pages);
		
		Element authorNode=root.addElement("authors");
		for (Author author : this.authors) {
			Element temp=authorNode.addElement("author");
			temp.addText(author.getName());
			temp.addAttribute("link", author.getLink());
		}
		
		Element sourceNode=root.addElement("source");
		sourceNode.addText(this.source);
		
		Element linkNode=root.addElement("link");
		linkNode.addText(this.link);
		
		Element doiNode=root.addElement("doi");
		doiNode.addText(this.doi);
		doiNode.addAttribute("link", this.doiLink);
		
		Element absNode=root.addElement("abstract");
		absNode.addText(this.abstractContent);
		
		return root;
	}
	
	public static Paper xmlToInstance(Element element){
		Paper result=new Paper();
		result.setTitle(element.elementText("title").trim());
		result.setDoi(element.elementText("doi").trim());
		result.setDoiLink(element.element("doi").attributeValue("link").trim());
		result.setLink(element.elementText("link").trim());
		result.setSource(element.elementText("source").trim());
		result.setPages(element.elementText("page").trim());
		Element authorsNode=element.element("authors");
		Vector<Author> authors=new Vector<Author>();
		for (Object authorNode : authorsNode.elements()) {
			Author author=new Author();
			author.setLink(((Element)authorNode).attributeValue("link"));
			author.setName(((Element)authorNode).getTextTrim());
			authors.add(author);
		}
		result.setAuthors(authors);
		
		Conference conf=new Conference();
		conf.setDate(new Date(Date.parse(element.element("conference").attributeValue("date"))));
		conf.setName(element.elementText("conference").trim());
		
		result.setConference(conf);
		result.setAbstractContent(element.elementText("abstract").trim());
		return result;
	}
}
