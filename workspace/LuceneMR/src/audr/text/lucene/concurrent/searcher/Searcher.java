/**
 * 
 */
package audr.text.lucene.concurrent.searcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.filesystem.FileSystemDirectory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;



import audr.text.lucene.distributed.searcher.TextQueryResult;
import audr.text.lucene.fields.IndexFields;
import audr.text.lucene.fields.TextCategoryFields;
import audr.text.utils.Constants;
import audr.text.utils.FileUtils;
import audr.text.utils.HdfsHelper;

/**
 * @author wanghan
 * 
 */
public class Searcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TextQueryResult rr[];
		try {
			long before=System.currentTimeMillis();
			rr = new Searcher().queryByString("国家");
//			rr = new Searcher(TextCategoryFields.CONSUMPTION).queryByString("梅赛德斯");
			long after=System.currentTimeMillis();
			System.out.println(after-before);
			for (int i = 0; i < rr.length; ++i) {

				System.out.println(i + " " + rr[i].getOriginalFileName());
				
			}
			
//			List<TextModel> texts=new ArrayList<TextModel>();
//			for(int i=0;i<rr.length;++i){
//				TextModel model=new TextModel();
//				model.setTitle(rr[i].getOriginalFileName());
//				model.setContent(rr[i].snippet);
//				model.setUrl(rr[i].getOriginalFilePath());
//				model.setTextType(rr[i].getOriginalFileName().substring(rr[i].getOriginalFileName().lastIndexOf('.')+1));
//				texts.add(model);
//			
//			}
//			ShowResult frame = new ShowResult(texts);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private MultiSearcher searcher;
	private String prefixHTML = "<font color=\"#ff0000\">";
	private String suffixHTML = "</font>";

	public Searcher() throws IOException {
		// TODO Auto-generated constructor stub
		Configuration conf = new Configuration();
		String[] indexDirs = Constants.INDEX_SHARDS;
		ArrayList<IndexSearcher> singleSearchers = new ArrayList<IndexSearcher>();

		for(int j=0;j<TextCategoryFields.TEXT_CATEGOTIES_ENUM.length;++j){
			long before=System.currentTimeMillis();
			System.out.println(TextCategoryFields.TEXT_CATEGOTIES_ENUM[j]);
			
			for (int i = 0; i < indexDirs.length; ++i) {
				
				
				Directory dir = new FileSystemDirectory(FileSystem.get(conf),
						new Path(indexDirs[i].replace("%Category%",TextCategoryFields.TEXT_CATEGOTIES_ENUM[j])), false, conf);
				try {
					singleSearchers.add(new IndexSearcher(dir, true));
				} catch (FileNotFoundException e) {
					// TODO: handle exception
				}
				
				
			}
			long after=System.currentTimeMillis();
			System.out.println(after-before);
		}

		searcher = new MultiSearcher(singleSearchers.toArray(new IndexSearcher[0]));
		
		System.out.println(searcher.maxDoc());
	}
	public Searcher(String category) throws IOException{
		Configuration conf = new Configuration();
		String[] indexDirs = Constants.INDEX_SHARDS;
		ArrayList<IndexSearcher> singleSearchers = new ArrayList<IndexSearcher>();

		for (int i = 0; i < indexDirs.length; ++i) {

			Directory dir = new FileSystemDirectory(FileSystem.get(conf),
					new Path(indexDirs[i].replace("%Category%", category)), false, conf);
			try {
				singleSearchers.add(new IndexSearcher(dir, true));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		searcher = new MultiSearcher(singleSearchers.toArray(new IndexSearcher[0]));
	}
	/**
	 * Query Type : keywords
	 * 
	 * @param keywords
	 * @return
	 */
	public TextQueryResult[] queryByString(String keywords) {
		try {

			Query query = IKQueryParser.parse(IndexFields.CONTENTS, keywords);
			TopDocs topdocs = searcher.search(query, Constants.RETURN_NUM);
			TextQueryResult[] results = generateQueryResults(topdocs,query);
			for(int i=0;i<results.length;++i){
				System.out.println(results[i]);
				
			}
			return results;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private TextQueryResult[] generateQueryResults(TopDocs topdocs,Query query)
			throws CorruptIndexException, IOException {
		TextQueryResult[] results = new TextQueryResult[topdocs.scoreDocs.length];
		String temp_dir = FileUtils.genTempDir(Constants.TEMP_DIR);
		HdfsHelper helper = new HdfsHelper();
		if (!new File(temp_dir).exists()) {
			new File(temp_dir).mkdir();
		}
		for (int i = 0; i < topdocs.scoreDocs.length; ++i) {
			Document doc = searcher.doc(topdocs.scoreDocs[i].doc);

			results[i] = new TextQueryResult();
			results[i].id = doc.get(IndexFields.ID);
			results[i].originalFileName = doc.get(IndexFields.FILENAME);
			
			String newpath = temp_dir + results[i].originalFileName;
			String newLfFilePath=temp_dir+"lf_"+results[i].originalFileName;
			helper.download(doc.get(IndexFields.FILEPATH), newpath);
			helper.download(doc.get(IndexFields.LF_FILEPATH), newLfFilePath);
			results[i].originalFilePath = newpath;
			results[i].score = topdocs.scoreDocs[i].score;
			results[i].snippet=getSnippet(newLfFilePath,query);
//			results[i].snippet=results[i].snippet.replace("<B>", "<font color=\"#ff0000\">");
//			results[i].snippet=results[i].snippet.replace("</B>", "</font>");
//			results[i].snippet="<html><body>"+results[i].snippet+"</body></html>";
		}
		return results;
	}
	public String getSnippet(String lfFilePath,Query query){
		try {
			BufferedReader reader=new BufferedReader(
					new InputStreamReader(
							new FileInputStream(new File(lfFilePath)),Charset.forName("GBK")));
			
			StringBuffer content=new StringBuffer();
			while(true){
				String line=reader.readLine();
				if(line==null){
					
					break;
				}
				content.append(line);
				content.append(" ");
			}
			Highlighter highLighter=new Highlighter(new SimpleHTMLFormatter(prefixHTML,suffixHTML),new QueryScorer(query));
			String snippet=highLighter.getBestFragment(new IKAnalyzer(), IndexFields.CONTENTS, content.toString());
			return snippet;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "";
	}
	public TextQueryResult[] queryById(String[] ids) {

		try {
			TextQueryResult[] results = new TextQueryResult[ids.length];
			String temp_dir = FileUtils.genTempDir(Constants.TEMP_DIR);

			if (!new File(temp_dir).exists()) {
				new File(temp_dir).mkdir();
			}

			for (int i = 0; i < ids.length; ++i) {
				Query query = IKQueryParser.parse(IndexFields.ID, ids[i]);

				TopDocs topdocs = searcher.search(query, Constants.RETURN_NUM);
				results[i] = generateQueryResult(temp_dir, topdocs);

			}
			return results;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private TextQueryResult generateQueryResult(String localDir, TopDocs topdocs)
			throws CorruptIndexException, IOException {

		TextQueryResult result;
		HdfsHelper helper = new HdfsHelper();

		if (topdocs.scoreDocs.length == 0) {
			result = null;
		} else {
			Document doc = searcher.doc(topdocs.scoreDocs[0].doc);

			result = new TextQueryResult();
			result.id = doc.get(IndexFields.ID);
			result.originalFileName = doc.get(IndexFields.FILENAME);
			String newpath = localDir + result.originalFileName;
//			String newLfFilePath=localDir+"lf/"+result.originalFileName;
			helper.download(doc.get(IndexFields.FILEPATH), newpath);
//			helper.download(doc.get(IndexFields.LF_FILEPATH), newLfFilePath);
			result.originalFilePath = newpath;
			result.score = topdocs.scoreDocs[0].score;
			result.snippet="";
		}
		return result;
	}
}
