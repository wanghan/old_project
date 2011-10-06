package classifier.bayes;

/**
* ������
*/
public class ClassifyResult implements Comparable<ClassifyResult>
{
	private double probility;//����ĸ���
	private int classification;//����
	
	
	public ClassifyResult()
	{
		this.probility = 0;
		this.classification = -1;
	}

	public double getProbility() {
		return probility;
	}

	public void setProbility(double probility) {
		this.probility = probility;
	}

	public int getClassification() {
		return classification;
	}

	public void setClassification(int classification) {
		this.classification = classification;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return classification+"\t:"+probility;
	}
	@Override
	public int compareTo(ClassifyResult o) {
		// TODO Auto-generated method stub
		if(this.probility>o.probility){
			return -1;
		}
		else{
			return 1;
		}
	}
}
