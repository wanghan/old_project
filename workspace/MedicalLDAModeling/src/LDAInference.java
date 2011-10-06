

import java.io.File;
import java.util.Date;
import java.util.Map;



/**
 * @author wanghan
 *
 */
public class LDAInference {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			GlobalData globalData=new GlobalData();
			Map<String,String> rudedata=ImageDescriptionFileParser.parser("G:\\Medical retrevial\\articles.xml");
			DataSet datas=new DataSet();
			for (String key : rudedata.keySet()) {
				String value=rudedata.get(key);
				if(value!=null&&value.length()>0){
					datas.insert(key, value, globalData);
				}
			}
			int topicCount=100;
			System.out.println("doc count:"+datas.documentSet.size());
			LDAModel model=new LDAModel(datas,globalData.getWordCount(),topicCount);
			model.InitNewModel();
			LDAInference infer=new LDAInference(datas,model);
			infer.inference();
			
			infer.Model.SaveFinalModel(infer.modelSavedDir);
			
			//save global data
			globalData.serialize(infer.modelSavedDir+"\\"+System.currentTimeMillis()+".global");
		
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public LDAModel Model;
	public DataSet DataSet;
	private double probs[];
	private int[] order;
	
	public int N_ITERS=100; //number of Gibbs sampling iteration
	public int SAVE_ITER=30;
	public String modelSavedDir="LDAModels/"+System.currentTimeMillis()+"_I{1}_T{2}/Slide_S{3}/";
	
	public static String modelSavedDirTemplate="LDAModels/"+System.currentTimeMillis()+"_I{1}_T{2}/Slide_S{3}/";
	
	
	public LDAInference(DataSet data,LDAModel model) {
		// TODO Auto-generated constructor stub
		this.DataSet=data;

		this.Model=model;

		this.probs=new double[Model.T];
		this.order=new int[DataSet.N];
		
		this.modelSavedDir=LDAInference.modelSavedDirTemplate.replace("{1}", String.valueOf(N_ITERS));
		this.modelSavedDir=this.modelSavedDir.replace("{2}",String.valueOf(Model.T));
		this.modelSavedDir=this.modelSavedDir.replace("{3}",String.valueOf(0));
		File dir=new File(modelSavedDir);
		if(!dir.exists()){
			dir.mkdirs();
		}
		for(int i=0;i<DataSet.N;++i){
			order[i]=i;
		}
	}
	public void inference(){
		double WBETA=Model.W*Model.be;
		double TALPHA=Model.T*Model.al;
		
		
	
		
		for(int iter=0;iter<N_ITERS;++iter){
			System.err.println(new Date()+ " Iteration : "+iter);
			

			
			for(int i=0;i<DataSet.N;++i){
	
				int curWord=DataSet.GlobalIndexWordMap.get(order[i]).Index;
				Document curDoc=DataSet.GlobalIndexDocMap.get(order[i]);
				int curTopic=Model.z[order[i]];
				int curDocument=curDoc.getIndex();
				Model.nzsum[curTopic]--;
				Model.ndsum[curDocument]--;
				Model.CWT[curWord][curTopic]--;
				Model.CTD[curTopic][curDocument]--;
				
				double totalProb=0;

				for(int k=0;k<Model.T;++k){
					probs[k]=(Model.CWT[curWord][k]+Model.be)/(Model.nzsum[k]+WBETA)
					*(Model.CTD[k][curDocument]+Model.al)/(Model.ndsum[curDocument]+TALPHA);
					totalProb+=probs[k];

				}
				 
				//sample a new topic and author assignment
				
				double r =Math.random()*totalProb;
				
				int newTopic=-1;
				
				double max=0;
				for(int j=0;j<Model.T;++j){
					max+=probs[j];
					if(max>=r){
						newTopic=j;
						break;
					}
				}
				
				
				Model.z[order[i]]=newTopic;
				Model.CTD[newTopic][curDocument]++;
				Model.CWT[curWord][newTopic]++;
				Model.nzsum[newTopic]++;
				Model.ndsum[curDocument]++;
			}
		}
		
		//updata theta and phi
		//theta: Probabilities of topics given authors size T x A
		// phi: Probabilities of words given topics, size W x T
		//this.CWT=new int[W][T];
		//this.CTA=new int[T][A];
		for(int i=0;i<Model.T;++i){
			for(int j=0;j<Model.W;++j){
				Model.phi[j][i]=1.0*(Model.be+Model.CWT[j][i])/(WBETA+Model.nzsum[i]);
			} 
			
			for (int j = 0; j < Model.D; j++) {
				Model.theta[i][j]=1.0*(Model.al+Model.CTD[i][j])/(TALPHA+Model.ndsum[j]);

			}
			
		}
		
	}
}
