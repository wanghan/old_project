/**
 * 
 */

/**
 * @author wanghan
 *
 */
public class Author {

	private String name;
	private String link;
	
	public Author(String name, String link) {
		// TODO Auto-generated constructor stub
		this.link=link;
		this.name=name;
	}
	
	public Author() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
