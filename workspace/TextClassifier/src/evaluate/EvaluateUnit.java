/**
 * 
 */
package evaluate;

/**
 * @author wanghan
 *
 */
public class EvaluateUnit {
	private int expectedCategory;
	private int realCategory;
	private String filename;
	public EvaluateUnit(String filename,int expect, int real) {
		// TODO Auto-generated constructor stub
		this.filename=filename;
		this.expectedCategory=expect;
		this.realCategory=real;
	}
	public int getExpectedCategory() {
		return expectedCategory;
	}
	public int getRealCategory() {
		return realCategory;
	}
	public String getFilename() {
		return filename;
	}
	
	
}
