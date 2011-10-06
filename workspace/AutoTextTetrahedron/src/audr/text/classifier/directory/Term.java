/**
 * 
 */
package audr.text.classifier.directory;

/**
 * ¹Ø¼ü×Ö
 * @author wanghan
 *
 */
public class Term {
	private int index;
	private String word;
	private double weight;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
}
