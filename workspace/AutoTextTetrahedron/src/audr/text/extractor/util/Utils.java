/**
 * 
 */
package audr.text.extractor.util;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wanghan
 * 
 */
public class Utils {



	/**
	 * 获取文档类型
	 * 
	 * @param file
	 *            文档
	 * @return
	 * @throws Exception
	 */
	public static String getFileType(String file) throws Exception {
		String filetype = null;
		try {
			int index = file.lastIndexOf(".");
			if (-1 != index)
				filetype = file.substring(index + 1, file.length());
		} catch (Exception e) {
			throw e;
		}
		return filetype == null ? null : filetype;
	}

	/**
	 * 剔除之间的字符信息
	 * 
	 * @param content
	 *            内容
	 * @param start
	 *            开始
	 * @param end
	 *            结束
	 * @return
	 * @throws Exception
	 */
	public static String removeBetweenter(String content, String start,
			String end) throws Exception {
		String str = "";
		try {
			String regex = start + ".*?" + end;
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(content);
			str = matcher.replaceAll(" ").trim();
			return str;
		} catch (Exception e) {
			throw e;
		}

	}

	public static String removeSpecSymbol(String string) throws Exception {
		if (string == null)
			string = "";
		try {
			string = string.trim();
			string = removeBetweenter(string, "<", ">");
		} catch (Exception e) {
			throw e;
		}
		return string;
	}

	public static String removeSpecSymbol(String string, String specSymbol)
			throws Exception {
		if (string == null)
			string = "";
		string = string.trim();
		Pattern p = Pattern.compile(specSymbol, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(string);
		string = m.replaceAll(" ");
		string = string.trim();
		return string;
	}

	/**
	 * 根据正则表达式剔除信息
	 * 
	 * @param content
	 * @param regex
	 * @return
	 * @throws Exception
	 */
	public static String removeByRegex(String content, String regex)
			throws Exception {
		String str = "";
		try {
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(content);
			if (matcher.find()) {
				MatchResult result = matcher.toMatchResult();
				str = result.group();
			}
		} catch (Exception e) {
			throw e;
		}
		return str;
	}

}
