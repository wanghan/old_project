package audr.text.extractor.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hslf.usermodel.PictureData;
import org.w3c.dom.Document;

import audr.text.extractor.dictionary.XMLDictionary;

public class InfoUtil {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String ENCODING = "utf-8";
	public static final String INDENT = "yes";
	
	public static final String  FILE_CONTENT_ENDWITH_TXT = ".txt";
	
	
	/**
	 * 创建节点
	 * @param name 节点名称
	 * @param text 节点值
	 * @return
	 */
	public static StringBuffer createElement( String name ) throws Exception
	{
		try{
			return createElement( name, null);
		}catch( Exception e)
		{
			throw e;
		}
		
	}
	
	/**
	 * 创建节点
	 * @param doc  创建xml文档的dom对象
	 * @param name 节点名称
	 * @param text 节点值
	 * @return
	 */
	public static StringBuffer createElement( String note, String text) throws Exception
	{
		StringBuffer info = new StringBuffer();
		try{
			if( null == text )
				return info.append( "<" + note + "/>");
			info.append( "<" + note + ">");
			info.append( StringFilter( text ) );
			info.append( "</" + note + ">");
			return info;
		}catch( Exception e)
		{
			throw e;
		}
		
	}
	
	
	/**
	 * 使用poi获取doc,ppt,xls共有的文档属性信息
	 * @param doc  创建xml文档的dom对象
	 * @param filePath	文档路径
	 * @param xmlFilePath xml文档路径
	 * @param root	xml根节点
	 * @param sumInfo	属性信息
	 * @param docInfo   文档属性
	 * @return	
	 * @throws Exception
	 */
	public static StringBuffer getFileInformation( File filePath, SummaryInformation sumInfo,
						DocumentSummaryInformation docInfo ) throws Exception
	{
		StringBuffer info = new StringBuffer();
		try
		{
		
			//文档类型
			info.append( createElement( XMLDictionary.TEXTTYPE, getFileType( filePath.getCanonicalPath())) );
			//标题
			info.append( createElement( XMLDictionary.TITLE, sumInfo.getTitle() ));
			//主题
			info.append( createElement( XMLDictionary.SUBJECT, sumInfo.getSubject() ));
			//url
			info.append( createElement( XMLDictionary.URL));
			//作者
			info.append( createElement( XMLDictionary.AUTHOR, sumInfo.getAuthor() ));
			//关键字
			info.append( createElement( XMLDictionary.KEYWORD, sumInfo.getKeywords() ));
			//备注
			info.append( createElement( XMLDictionary.REMARK, sumInfo.getComments()) );
			//字节数
			info.append( createElement( XMLDictionary.WORDCOUNT, String.valueOf( docInfo.getByteCount()) ) );
			//模板
			info.append( createElement( XMLDictionary.TEMPLATE, sumInfo.getTemplate()) );
			//单位
			info.append( createElement( XMLDictionary.DEPARTMENT, docInfo.getCompany()));
			//类别
			info.append( createElement( XMLDictionary.SORT, docInfo.getCategory()));
			//上次保存者
			info.append( createElement( XMLDictionary.LASTAUTHOR, sumInfo.getLastAuthor()) );
			//修订次数
			info.append( createElement( XMLDictionary.MODIFYCOUNT, sumInfo.getRevNumber()) );
			//编辑文档花费的时间
			info.append( createElement( XMLDictionary.EDITTIME,  sumInfo.getEditTime() / ( 10000000 * 60 ) + "" ) );
			//创建时间
			info.append( createElement( XMLDictionary.CREATETIME, dateFormat( sumInfo.getCreateDateTime())) );
			//打印时间
			info.append( createElement( XMLDictionary.PRINTTIME, dateFormat( sumInfo.getLastPrinted() ) ) );
			return info;
		}catch( Exception e )
		{
			throw e;
		}
	}
	
	/**
	 * 如果文件解析失败或是损坏文件则 是空标签
	 * @throws Exception
	 */
	public static void getNullSFFile( String sfPath ) throws Exception
	{
		StringBuffer info = new StringBuffer();
		//文档类型
		info.append( createElement( XMLDictionary.TEXTTYPE ) );
		//标题
		info.append( createElement( XMLDictionary.TITLE ));
		//主题
		info.append( createElement( XMLDictionary.SUBJECT ));
		//url
		info.append( createElement( XMLDictionary.URL));
		//作者
		info.append( createElement( XMLDictionary.AUTHOR ));
		//关键字
		info.append( createElement( XMLDictionary.KEYWORD ));
		//备注
		info.append( createElement( XMLDictionary.REMARK) );
		//字节数
		info.append( createElement( XMLDictionary.WORDCOUNT ) );
		//模板
		info.append( createElement( XMLDictionary.TEMPLATE ) );
		//单位
		info.append( createElement( XMLDictionary.DEPARTMENT ));
		//类别
		info.append( createElement( XMLDictionary.SORT ));
		//上次保存者
		info.append( createElement( XMLDictionary.LASTAUTHOR ) );
		//修订次数
		info.append( createElement( XMLDictionary.MODIFYCOUNT ) );
		//编辑文档花费的时间
		info.append( createElement( XMLDictionary.EDITTIME ));
		//创建时间
		info.append( createElement( XMLDictionary.CREATETIME ) );
		//打印时间
		info.append( createElement( XMLDictionary.PRINTTIME ) );
		info.append( "\n" );
		InfoUtil.appendSFInfo(sfPath, info.toString() );
		
	}
	
	/**
	 * 创建xml文档
	 * @param document
	 * @param filename
	 * @throws Exception
	 */
	public static void createXmlFile(Document document, String filename) throws Exception 
	{
		   try 
		   {
			    TransformerFactory tFactory = TransformerFactory.newInstance();
			    Transformer transformer = tFactory.newTransformer();
			   	//设置编码格式
			    transformer.setOutputProperty(OutputKeys.ENCODING, ENCODING );
			    //是否缩进输出
			    transformer.setOutputProperty( OutputKeys.INDENT, INDENT  );
			    
			    DOMSource source = new DOMSource(document);
			    StreamResult result = new StreamResult(new File(filename));
			    transformer.transform(source, result);
		   } catch (Exception ex) {
			    throw ex;
		   }
	}
	
	/**
	 * 获取文件夹存储相应的文档信息(xml,图片等)
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static String getNewFilePath( String savePath ) throws Exception
	{
		String name = savePath;
		StringBuffer sbName = new StringBuffer();
		for( int i = 0; i < name.length(); i++ )
		{
			char c = name.charAt( i );
			if("\\".equals( String.valueOf( c )))
				sbName.append( "/" );
			else
				sbName.append( c );
		}
		name = sbName.toString();
		int index = name.lastIndexOf("/");
		if( index != name.length() - 1 )
			name = name + "/";
		return name;
	}
	
	/**
	 * 过滤文档的路径信息,把其变为相对路径
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String getFilterPath( String path ) throws Exception
	{
		StringBuffer sbName = new StringBuffer();
		for( int i = 0; i < path.length(); i++ )
		{
			char c = path.charAt( i );
			if("\\".equals( String.valueOf( c )))
				sbName.append( "/" );
			else
				sbName.append( c );
		}
		path = sbName.toString();
		int index = path.indexOf(":");
		return index >= 0 ? ".." + path.substring(index + 1, path.length()) : ".." + path;
	}
	
	/**
	 * 过滤文档的路径信息
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String getFilterPath2( String path ) throws Exception
	{
		StringBuffer sbName = new StringBuffer();
		for( int i = 0; i < path.length(); i++ )
		{
			char c = path.charAt( i );
			if("\\".equals( String.valueOf( c )))
				sbName.append( "/" );
			else
				sbName.append( c );
		}
		return sbName.toString();
	}
	
	/**
	 * 获取文档类型
	 * @param file	文档
	 * @return
	 * @throws Exception
	 */
	public static String getFileType ( String file ) throws Exception
	{
		String filetype = null;
		try{
			int index = file.lastIndexOf( "." );
			if( -1 != index )
				filetype = file.substring( index + 1, file.length() );
		}catch( Exception e)
		{
			throw e;
		}
		return filetype == null ? null:filetype;
	}
	
	/**
	 * 获取ppt文档中的图片格式
	 * @param pictureData
	 * @return
	 * @throws Exception
	 */
	public static String getPictureSuffix( PictureData pictureData ) throws Exception
	{
		String suffix = "";
		int tp = pictureData.getType();
		switch( tp ){
			case org.apache.poi.hslf.model.Picture.DIB:
				suffix = "dib";
				break;
			case org.apache.poi.hslf.model.Picture.EMF:
				suffix = "emf";
				break;
			case org.apache.poi.hslf.model.Picture.JPEG:
				suffix = "jpeg";
				break;
			case org.apache.poi.hslf.model.Picture.PICT:
				suffix = "pict";
				break;
			case org.apache.poi.hslf.model.Picture.PNG:
				suffix = "png";
				break;
			case org.apache.poi.hslf.model.Picture.WMF:
				suffix = "wmf";
				break;
			
		}
		return suffix;
	}

	/**
	 * 获取格式化后的时间信息
	 * @param dar	时间信息
	 * @return
	 * @throws Exception
	 */
	public static String dateFormat( Calendar calendar ) throws Exception
	{
		if( null == calendar )
			return null;
		String date = null;
		try{
			String pattern = DATE_FORMAT;
			SimpleDateFormat format = new SimpleDateFormat( pattern );
			date = format.format( calendar.getTime() );
		}catch( Exception e )
		{
			throw e;
		}
		return date == null ? "" : date;
	}
	
	
	/**
	 * 获取格式化后的时间信息
	 * @param d	时间信息
	 * @return
	 * @throws Exception
	 */
	public static String dateFormat( Date d ) throws Exception
	{
		if( null == d )
			return null;
		String date = null;
		try{
			String pattern = DATE_FORMAT;
			SimpleDateFormat format = new SimpleDateFormat( pattern );
			date = format.format( d);
		}catch( Exception e )
		{
			throw e;
		}
		return date == null ? "" : date;
	}
	
	/**
	 * 获取格式化后的时间信息
	 * @param l	时间信息
	 * @return
	 * @throws Exception
	 */
	public static String dateFormat( long l ) throws Exception
	{
		String date = null;
		try{
			String pattern = DATE_FORMAT;
			SimpleDateFormat format = new SimpleDateFormat( pattern );
			Date d = new Date( l );
			date = format.format( d);
		}catch( Exception e )
		{
			throw e;
		}
		return date == null ? "" : date;
	}
	
	/**
	 * 根据时间获取名称
	 * @return
	 * @throws Exception
	 */
	public static String getName() throws Exception
	{
		String name = "";
		String format = "yyyyMMddHHmmss";
		SimpleDateFormat date = new SimpleDateFormat( format );
		name = date.format( new Date() ) + System.currentTimeMillis() ;
		return name;
	}

	/**
	 * 剔除之间的字符信息
	 * @param content	内容
	 * @param start	开始
	 * @param end	结束
	 * @return
	 * @throws Exception
	 */
	public static String removeBetweenter( String content, String start, String end ) throws Exception
	{
		String str = "";
		try{
			String regex = start + ".*?" + end;
			Pattern pattern = Pattern.compile( regex, Pattern.CASE_INSENSITIVE );
			Matcher matcher = pattern.matcher( content );
			str = matcher.replaceAll( " " ).trim();
			return str;
		}catch( Exception e )
		{
			throw e;
		}
		
	}
	
	public static String removeSpecSymbol(String string) throws Exception{
		if(string==null)
			string="";
		try {
			string=string.trim();
			string=removeBetweenter(string,"<",">");
		} catch (Exception e) {
			throw e;
		}
		return string;
	}
	
	public static String removeSpecSymbol(String string, String specSymbol) throws Exception {
		if(string==null)
			string="";
		string=string.trim();
		Pattern p = Pattern.compile(specSymbol,Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(string);
		string = m.replaceAll(" ");
		string=string.trim();
		return string;
	}
	/**
	 * 根据正则表达式剔除信息
	 * @param content
	 * @param regex
	 * @return
	 * @throws Exception
	 */
	public static String removeByRegex( String content, String regex ) throws Exception
	{
		String str = "";
		try{
			Pattern pattern = Pattern.compile( regex, Pattern.CASE_INSENSITIVE );
			Matcher matcher = pattern.matcher( content );
			if( matcher.find() )
			{
				MatchResult result = matcher.toMatchResult();
				str = result.group();
			}
		}catch( Exception e )
		{
			throw e;
		}
		return str;
	}
	
	/**
	 * 获取html中的title标签
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String getHtmlTitle( String text ) throws Exception
	{
		String title = "";
		try{
			String regex = "(<title.*?</title>)";
			text = removeByRegex( text, regex );
			title = InfoUtil.removeSpecSymbol( text );
		}catch( Exception e )
		{
			throw e;
		}
		return title;
	}
	
	/**
	 * 获取文件名称
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static String getFileName( String filePath ) throws Exception
	{
		String fileName = null;
		try{
			filePath = getFilterPath( filePath );
			int index = filePath.lastIndexOf( "/" );
			if( index > 0 )
			{
				fileName = filePath.substring( index + 1 , filePath.length() );
			}
		}catch( Exception e)
		{
			throw e;
		}
		return fileName == null ? "" : fileName ;
	}
	/**
	 * 追加sf文档信息
	 * @param sfPath
	 * @param info
	 */
	public static void appendSFInfo( String sfPath, String info )throws Exception
	{
		try{
			sfPath = InfoUtil.getNewFilePath( sfPath );
			File file = new File( sfPath );
			FileWriter fw = new FileWriter( file, true );
			BufferedWriter bw = new BufferedWriter( fw );
			bw.write( info );
			bw.flush();
			bw.close();
			fw.close();
		}catch( Exception e)
		{
			throw e;
		}
	}
	/**
	 * 获取文档内容信息
	 * @param content
	 * @param docFile
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static String getContent(  File f, String savePath ) throws Exception
	{
		return getContent( "", f, savePath );
	}
	
	/**
	 * 获取文档内容信息
	 * @param content
	 * @param docFile
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static String getContent( String content, File f, String savePath ) throws Exception
	{
		savePath = InfoUtil.getNewFilePath( savePath );
		File file = new File( savePath );
		if( !file.exists() )
		{
			File dir = file.getParentFile();
			if( !dir.exists())
				dir.mkdirs();
			file.mkdir();
		}
		savePath = savePath + InfoUtil.getName() + XMLDictionary.COUNT + FILE_CONTENT_ENDWITH_TXT;
		File saveFile = new File( savePath );
		if( !saveFile.exists() )
		{
			saveFile.createNewFile();
		}
		FileWriter fw = new FileWriter( saveFile );
		BufferedWriter writer = new BufferedWriter( fw );
		writer.write( content);
		writer.flush();
		writer.close();
		fw.close();
		System.out.println( f.getCanonicalPath() + "解析完毕==========解析文件为:" + savePath + "第" + XMLDictionary.COUNT + "个文件" );
		XMLDictionary.COUNT++;
		return savePath;
	}
	
	/**
	 * 过滤特殊字符
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String StringFilter(String  str) throws PatternSyntaxException   
	{     
		  
		 StringBuffer sb = new StringBuffer();
		 //键盘可输入特殊符号 *[^[`!@# %^*()_\\+\\-={}\\[\\]:,|\";',.?/\\s·~！——-【】￥……（）、；‘：“”，。《》？、]<>&+$]*[^[\u4E00-\u9FFF]+$]
		 //正则获取数字，字母，汉字（简繁体）以及所有键盘可输入特殊符号
		 String   regEx  =  "[^[\u4E00-\u9FFF]+$]*[^[a-zA-Z0-9]+$]*[^[`!@#\\$\\%^*()_\\+\\-={}\\[\\]:,|\";',.?/·~！——-【】￥……（）、；‘：“”，。《》？、]<>&+$]*[^[\u4E00-\u9FFF]+$]*";
		 try{
			 Pattern   p   =   Pattern.compile(regEx);     
			 Matcher   m   =   p.matcher(str); 
			 while( m.find() )
			 {
				 sb.append( m.group() );
			 }
			 return   sb.toString();  
		 }catch( PatternSyntaxException e )
		 {
			 e.printStackTrace();
			 throw e;
		 }
	}     
	
}
