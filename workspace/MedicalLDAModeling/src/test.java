import java.io.IOException;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			LDAModel model = LDAModel
					.LoadWholeModel("LDAModels\\1305566603744_I100_T100\\Slide_S0\\1305567752650.model");
			System.out.println(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
