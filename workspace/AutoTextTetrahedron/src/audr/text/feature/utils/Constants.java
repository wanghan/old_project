package audr.text.feature.utils;

public interface Constants {
	
	public String[] CATEGORYS={"CONSUMPTION","CULTURE","EDUCATION","FINANCE","GOVERNMENT","HEALTH","MILITARY"
			,"SCIENCE","SPORT","TOUR"};
	
	public String[] TRAINSETPATHS={
			"D:\\Data\\original\\lf\\consumption",
			"D:\\Data\\original\\lf\\culture",
			"D:\\Data\\original\\lf\\education",
			"D:\\Data\\original\\lf\\finance",
			"D:\\Data\\original\\lf\\government",
			"D:\\Data\\original\\lf\\health",
			"D:\\Data\\original\\lf\\military",
			"D:\\Data\\original\\lf\\science",
			"D:\\Data\\original\\lf\\sport",
			"D:\\Data\\original\\lf\\tour"
	};
	public String[] TESTSETPATHS={
			"D:\\Data\\original\\lf\\consumption_test",
			"D:\\Data\\original\\lf\\culture_test",
			"D:\\Data\\original\\lf\\education_test",
			"D:\\Data\\original\\lf\\finance_test",
			"D:\\Data\\original\\lf\\government_test",
			"D:\\Data\\original\\lf\\health_test",
			"D:\\Data\\original\\lf\\military_test",
			"D:\\Data\\original\\lf\\science_test",
			"D:\\Data\\original\\lf\\sport_test",
			"D:\\Data\\original\\lf\\tour_test"
	};
	
	
//	public String[] TRAINSETPATHS={
//			"D:\\AUDR\\语料库\\SogouC.mini.20061127\\SogouC.mini\\Sample\\C000014",
//			"D:\\AUDR\\语料库\\SogouC.mini.20061127\\SogouC.mini\\Sample\\C000013",
//			"D:\\AUDR\\语料库\\SogouC.mini.20061127\\SogouC.mini\\Sample\\C000016",
//			"D:\\AUDR\\语料库\\SogouC.mini.20061127\\SogouC.mini\\Sample\\C000008",
//			"D:\\AUDR\\语料库\\SogouC.mini.20061127\\SogouC.mini\\Sample\\C000020",
//			"D:\\AUDR\\语料库\\SogouC.mini.20061127\\SogouC.mini\\Sample\\C000024"
//			
//	};
//	public String[] TRAINSETPATHS={
//			"D:\\AUDR\\语料库\\SogouC\\ClassFile\\C000014",
//			"D:\\AUDR\\语料库\\SogouC\\ClassFile\\C000013",
//			"D:\\AUDR\\语料库\\SogouC\\ClassFile\\C000016",
//			"D:\\AUDR\\语料库\\SogouC\\ClassFile\\C000008",
//			"D:\\AUDR\\语料库\\SogouC\\ClassFile\\C000020",
//			"D:\\AUDR\\语料库\\SogouC\\ClassFile\\C000024"
//			
//	};
	//选取特征的最小DF
	
	
	public int MIN_DF=5;
	public String INDEX_ROOT="index/";
	
	
	public String MODEL_DATA_ROOT="model/";
	
	public double FEATURE_FILTER=0.1;
	
	
	public int FEATURE_SELECTOR_DF=1;
	public int FEATURE_SELECTOR_MI_AVG=2;
	public int FEATURE_SELECTOR_IG=3;
	public int FEATURE_SELECTOR_CHI_AVG=4;
	public int FEATURE_SELECTOR_MI_MAX=5;
	public int FEATURE_SELECTOR_CHI_MAX=6;
}
