package audr.text.lucene.distributed.indexer;

import java.io.Reader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import audr.text.lucene.fields.IndexFields;

/**
 * A utility for making Lucene Documents from a File.
 * 
 * @author wanghan
 * 
 */
public class DocumentMaker {

	public static Document Document(String id, String filename,
			String orifilepath, String contents,String lfFilePath)
			throws java.io.FileNotFoundException {

		// make a new, empty document
		Document doc = new Document();
		doc.add(new Field(IndexFields.ID, id, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(IndexFields.FILENAME, filename, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(IndexFields.FILEPATH, orifilepath, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(IndexFields.LF_FILEPATH, lfFilePath, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(IndexFields.CONTENTS, contents, Field.Store.YES,
				Field.Index.ANALYZED));
		
		// return the document
		
		return doc;
	}

	public static Document Document(String id, String filename,
			String orifilepath, Reader reader,String lfFilePath)
			throws java.io.FileNotFoundException {

		// make a new, empty document
		Document doc = new Document();
		doc.add(new Field(IndexFields.ID, id, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(IndexFields.FILENAME, filename, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(IndexFields.FILEPATH, orifilepath, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(IndexFields.LF_FILEPATH, lfFilePath, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(IndexFields.CONTENTS, reader));
		
		// return the document
		return doc;
	}

	private DocumentMaker() {
	}
}
