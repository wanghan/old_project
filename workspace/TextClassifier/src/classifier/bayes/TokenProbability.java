/**
 * 
 */
package classifier.bayes;

import java.io.Serializable;

import utils.Constants;

/**
 * @author wanghan
 *
 */
public class TokenProbability implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -538565302386918763L;
	private String token;
	private float[] probabilities;
	
	public TokenProbability(String token) {
		// TODO Auto-generated constructor stub
		this.token=token;
		probabilities=new float[Constants.CATEGORYS.length];
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public float[] getProbabilities() {
		return probabilities;
	}

	public void setProbabilities(float[] probabilities) {
		this.probabilities = probabilities;
	}
	
	public void setProbability(int i,float probability) {
		this.probabilities[i] = probability;
	} 
}
