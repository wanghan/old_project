package audr.text.extractor.config;
/**
 * 
 * @author wanghan
 *
 */
public class BFAttribute {
	private String Name;
	private String Type;
	private String Method;
	private String ClassName;
	

	
	public String getClassName() {
		return ClassName;
	}
	public void setClassName(String className) {
		ClassName = className;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getMethod() {
		return Method;
	}
	public void setMethod(String method) {
		Method = method;
	}
	
	
}
