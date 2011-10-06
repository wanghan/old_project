

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * @author wanghan
 * 
 */
public class LDAModel {
	public double al, be; // hyperparameters

	// Estimated/Inferenced parameters
	public int W; // vocabulary size
	public int T; // number of topics
	public int D; // number of document

	public double[][] theta; // theta: Probabilities of topics given authors
	// size T x D
	public double[][] phi; // phi: Probabilities of words given topics, size W
	// x T

	public int[] z;// topic assignments
	// public int[] y; // conference assignments

	// Temp variables while sampling
	public int[][] CWT;// Number of words assigned to topic and word.
	public int[][] CTD;// Number of words assigned to conference and topic

	public int[] nzsum; // total number of words assigned to topic, size T
	public int[] ndsum;

	public DataSet dataSet;

	public LDAModel() {
		// TODO Auto-generated constructor stub
	}

	public LDAModel(DataSet data, int wordCount, int topicCount) {
		// TODO Auto-generated constructor stub
		this.dataSet = data;

		this.T = topicCount;

		this.W = wordCount;
		this.D = data.documentSet.size();
		SetDefaultParas();
	}

	public void SetDefaultParas() {

		this.al = 50.0 / T;
		this.be = 0.01;
	}

	public void InitNewModel() {

		this.z = new int[dataSet.N];

		this.nzsum = new int[T];
		this.ndsum = new int[D];
		this.CWT = new int[W][T];
		this.CTD = new int[T][D];

		this.phi = new double[W][T];
		this.theta = new double[T][D];

		for (int i = 0; i < W; ++i) {
			for (int j = 0; j < T; ++j) {
				this.CWT[i][j] = 0;
			}

		}
		for (int i = 0; i < T; ++i) {
			for (int j = 0; j < D; ++j) {
				this.CTD[i][j] = 0;
			}

		}
		for (int j = 0; j < T; ++j) {
			this.nzsum[j] = 0;
		}
		for (int j = 0; j < D; ++j) {
			this.ndsum[j] = 0;
		}

		for (int i = 0; i < dataSet.N; ++i) {
			int curWord = dataSet.GlobalIndexWordMap.get(i).Index;
			Document curDoc = dataSet.GlobalIndexDocMap.get(i);

			int randomTopic = (int) Math.floor(Math.random() * T);
			int curDocIndex = curDoc.getIndex();

			z[i] = randomTopic;
			CWT[curWord][randomTopic]++;
			this.nzsum[randomTopic]++;
			this.ndsum[curDocIndex]++;
			CTD[randomTopic][curDocIndex]++;
		}
	}

	public void SaveWholeModel(String filepath) throws FileNotFoundException,
			IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				filepath, false));
		oos.writeObject(this.al);
		oos.writeObject(this.be);
		oos.writeObject(this.W);
		oos.writeObject(this.T);
		oos.writeObject(this.D);
		oos.flush();
		oos.writeObject(this.phi);
		oos.flush();
		oos.writeObject(this.theta);
		oos.flush();
		oos.writeObject(this.z);
		oos.flush();
		oos.writeObject(this.dataSet);
		oos.flush();
		oos.writeObject(this.CWT);
		oos.flush();
		oos.writeObject(this.CTD);
		oos.flush();
		oos.writeObject(this.nzsum);
		oos.flush();
		oos.writeObject(this.ndsum);
		oos.flush();
		oos.close();
	}

	public static LDAModel LoadWholeModel(String filepath)
			throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				filepath));
		LDAModel model = new LDAModel();
		model.al = (Double) (ois.readObject());
		model.be = (Double) (ois.readObject());
		model.W = (Integer) (ois.readObject());
		model.T = (Integer) (ois.readObject());
		model.D = (Integer) (ois.readObject());
		model.phi = (double[][]) (ois.readObject());
		model.theta = (double[][]) (ois.readObject());
		model.z = (int[]) (ois.readObject());
		model.dataSet = (DataSet) (ois.readObject());
		model.CWT = (int[][]) (ois.readObject());
		model.CTD = (int[][]) (ois.readObject());
		model.nzsum = (int[]) (ois.readObject());
		model.ndsum = (int[]) (ois.readObject());
		ois.close();
		return model;
	}

	public void SaveFinalModel(String modelSavedDir) {
		String modleFile = modelSavedDir + System.currentTimeMillis()
				+ ".model";

		try {
			this.SaveWholeModel(modleFile);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
