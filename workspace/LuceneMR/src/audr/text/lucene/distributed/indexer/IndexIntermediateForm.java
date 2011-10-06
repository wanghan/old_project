package audr.text.lucene.distributed.indexer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.filesystem.RAMDirectoryUtil;
import org.apache.hadoop.io.Writable;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;


/**
 * An intermediate form for one or more parsed Lucene documents and/or delete
 * terms. It actually uses Lucene file format as the format for the intermediate
 * form by using RAM dir files.
 */
public class IndexIntermediateForm implements Writable {

	private RAMDirectory dir;
	private IndexWriter writer;
	private int numDocs;

	/**
	 * Constructor
	 * 
	 * @throws IOException
	 */
	public IndexIntermediateForm() throws IOException {
		dir = new RAMDirectory();
		writer = null;
		numDocs = 0;
	}

	/**
	 * Get the ram directory of the intermediate form.
	 * 
	 * @return the ram directory
	 */
	public Directory getDirectory() {
		return dir;
	}

	/**
	 * This method is used by the index update mapper and process a document
	 * operation into the current intermediate form.
	 * 
	 * @param doc
	 *            input document operation
	 * @param analyzer
	 *            the analyzer
	 * @throws IOException
	 */
	public void process(Document doc, Analyzer analyzer) throws IOException {
		if (writer == null) {
			// analyzer is null because we specify an analyzer with addDocument
			writer = createWriter();
		}
		try {
			writer.addDocument(doc, analyzer);
			numDocs++;
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
		}
		
	}

	/**
	 * This method is used by the index update combiner and process an
	 * intermediate form into the current intermediate form. More specifically,
	 * the input intermediate forms are a single-document ram index and/or a
	 * single delete term.
	 * 
	 * @param form
	 *            the input intermediate form
	 * @throws IOException
	 */
	public void process(IndexIntermediateForm form) throws IOException {
		if (form.dir.sizeInBytes() > 0) {
			if (writer == null) {
				writer = createWriter();
			}

			writer.addIndexesNoOptimize(new Directory[] { form.dir });
			numDocs++;
		}

	}

	/**
	 * Close the Lucene index writer associated with the intermediate form, if
	 * created. Do not close the ram directory. In fact, there is no need to
	 * close a ram directory.
	 * 
	 * @throws IOException
	 */
	public void closeWriter() throws IOException {
		if (writer != null) {
			writer.close();
			writer = null;
		}
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(this.getClass().getSimpleName());
		buffer.append("[numDocs=");
		buffer.append(numDocs);
		return buffer.toString();
	}

	private IndexWriter createWriter() throws IOException {
		IndexWriter writer = new IndexWriter(dir, new IKAnalyzer(), true,
				IndexWriter.MaxFieldLength.UNLIMITED);
		writer.setUseCompoundFile(false);
		return writer;
	}

	private void resetForm() throws IOException {
		if (dir.sizeInBytes() > 0) {
			// it's ok if we don't close a ram directory
			dir.close();
			// an alternative is to delete all the files and reuse the ram
			// directory
			dir = new RAMDirectory();
		}
		assert (writer == null);
		numDocs = 0;
	}

	public void write(DataOutput out) throws IOException {

		String[] files = dir.listAll();
		RAMDirectoryUtil.writeRAMFiles(out, dir, files);
	}

	public void readFields(DataInput in) throws IOException {
		resetForm();
		RAMDirectoryUtil.readRAMFiles(in, dir);
	}

}
