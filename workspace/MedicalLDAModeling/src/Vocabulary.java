

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 * 
 * @author wanghan
 *
 */
public class Vocabulary implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4681887572486951754L;
	
	
	public HashMap<Integer, Word> indexVocabularyMap;
	public HashMap<String, Word> wordVocabularyMap;
	private int index;
	
	
	
	public Vocabulary() {
		// TODO Auto-generated constructor stub
		this.indexVocabularyMap=new HashMap<Integer, Word>();
		this.wordVocabularyMap=new HashMap<String, Word>();
		index=0;
	}
	
	public Collection<Word> GetWordsCollection(){
		return indexVocabularyMap.values();
	}
	
	public Word InsertWord(String word){
		Word ww=this.wordVocabularyMap.get(word);
		if(ww==null){
			ww=new Word(index++, word);
			indexVocabularyMap.put(ww.Index, ww);
			wordVocabularyMap.put(ww.Word, ww);
		}
		return ww;
	}
	public Word GetWord(int index){
		return indexVocabularyMap.get(index);
	}
	public Word GetWordWithInsert(String word){
		if(wordVocabularyMap.containsKey(word)){
			return wordVocabularyMap.get(word);
		}
		else{
			Word ww=new Word(index++, word);
			indexVocabularyMap.put(ww.Index, ww);
			wordVocabularyMap.put(ww.Word, ww);
			return ww;
		}
		
	}
	
	public Word GetWord(String word){
		if(wordVocabularyMap.containsKey(word)){
			return wordVocabularyMap.get(word);
		}
		else{
			return null;
		}
	}
	
	public int GetWordCount(){
		return indexVocabularyMap.size();
	}
}
