/**
 * 
 */
package audr.text.lucene.distributed.searcher;

import java.io.Serializable;


/**
 * @author wanghan
 *
 */
public class TextQueryResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6466501668850113591L;
	public String id;//�ĵ���Ψһ��ʶ
	public String originalFileName; //ԭʼ�ĵ����ļ���
	public String originalFilePath; //ԭʼ�ĵ����ļ�·��
	public double score;  //�ײ����������ƶ�����
	public String snippet;
	
	public TextQueryResult(){
		
	}
	
	
	public TextQueryResult(String id,String fileName,String filePath,double score) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.originalFileName=fileName;
		this.originalFilePath=filePath;
		this.score=score;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id+"@"+originalFileName+"@"+originalFilePath+"@"+score+"@"+snippet;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getOriginalFilePath() {
		return originalFilePath;
	}

	public void setOriginalFilePath(String originalFilePath) {
		this.originalFilePath = originalFilePath;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}


	public String getSnippet() {
		return snippet;
	}


	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	
	
}
