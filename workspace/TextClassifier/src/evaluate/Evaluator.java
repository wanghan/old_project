/**
 * 
 */
package evaluate;

import java.util.Vector;

/**
 * @author wanghan
 *
 */
public class Evaluator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private double macroF1;
	private double microF1;
	public Evaluator(Vector<EvaluateUnit> units,int [] categoryTextCount){
		
		macroF1=0;
		microF1=0;
		int []rightClassify=new int[categoryTextCount.length];
		int []wrongInThisCategory=new int[categoryTextCount.length];
		int[] missedCount=new int[categoryTextCount.length];
		int totalTextNumber=0;
		int totalRight=0;
		
		for (EvaluateUnit evaluateUnit : units) {
			if(evaluateUnit.getExpectedCategory()==evaluateUnit.getRealCategory()){
				rightClassify[evaluateUnit.getExpectedCategory()]++;
				totalRight++;
			}
			else{
				wrongInThisCategory[evaluateUnit.getRealCategory()]++;
				missedCount[evaluateUnit.getExpectedCategory()]++;
			}
		}
		
		for(int i=0;i<categoryTextCount.length;++i){
			
			totalTextNumber+=categoryTextCount[i];
			if((rightClassify[i]+wrongInThisCategory[i])==0)
				continue;
			if(rightClassify[i]==0)
				continue;
			if((rightClassify[i]+missedCount[i]==0)){
				continue;
			}
			double precise=1.0*rightClassify[i]/(rightClassify[i]+wrongInThisCategory[i]);
			double recall=1.0*rightClassify[i]/(rightClassify[i]+missedCount[i]);
			double F1=2*precise*recall/(precise+recall);
			macroF1+=F1;
			
		}
		macroF1=macroF1/categoryTextCount.length;
		
		microF1=1.0*totalRight/totalTextNumber;
		
		
	}
	public double getMacroF1() {
		return macroF1;
	}
	public double getMicroF1() {
		return microF1;
	}
	
	
}
