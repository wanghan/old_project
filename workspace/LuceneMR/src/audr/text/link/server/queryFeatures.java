package audr.text.link.server;


import java.util.Map;

public interface queryFeatures {
	/**
	 * @param tableName
	 * @param LireID
	 * @return Map<String,String>：<特征名称，特征值>
	 */
	public Map<String,String> getFeature(String tableName,String LireID); 
	
	
}
