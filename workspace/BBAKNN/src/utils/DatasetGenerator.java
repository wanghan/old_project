/**
 * 
 */
package utils;

/**
 * @author wanghan
 * 
 */
public class DatasetGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static double[][] GenRandomData(int pts, int ptDims) {
		double dataPts[][] = new double[pts][ptDims];

		for (int i = 0; i < dataPts.length; i++) {
			for (int j = 0; j < ptDims; j++) {
				dataPts[i][j] = Math.random();
				dataPts[i][j] = Math.random();
			}
		}
		return dataPts;
	}

	public static double Norm_rand(double miu, double sigma2) {
		double N = 12;
		double x = 0, temp = N;
		do {
			x = 0;
			for (int i = 0; i < N; i++)
				x = x + (Math.random());
			x = (x - temp / 2) / (Math.sqrt(temp / 12));
			x = miu + x * Math.sqrt(sigma2);
		} while (x <= 0); // 在此我把小于0的数排除掉了
		return x;
	}
}
