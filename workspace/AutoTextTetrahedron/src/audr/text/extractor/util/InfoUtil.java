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
	 * �����ڵ�
	 * @param name �ڵ�����
	 * @param text �ڵ�ֵ
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
	 * �����ڵ�
	 * @param doc  ����xml�ĵ���dom����
	 * @param name �ڵ�����
	 * @param text �ڵ�ֵ
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
	 * ʹ��poi��ȡdoc,ppt,xls���е��ĵ�������Ϣ
	 * @param doc  ����xml�ĵ���dom����
	 * @param filePath	�ĵ�·��
	 * @param xmlFilePath xml�ĵ�·��
	 * @param root	xml���ڵ�
	 * @param sumInfo	������Ϣ
	 * @param docInfo   �ĵ�����
	 * @return	
	 * @throws Exception
	 */
	public static StringBuffer getFileInformation( File filePath, SummaryInformation sumInfo,
						DocumentSummaryInformation docInfo ) throws Exception
	{
		StringBuffer info = new StringBuffer();
		try
		{
		
			//�ĵ�����
			info.append( createElement( XMLDictionary.TEXTTYPE, getFileType( filePath.getCanonicalPath())) );
			//����
			info.append( createElement( XMLDictionary.TITLE, sumInfo.getTitle() ));
			//����
			info.append( createElement( XMLDictionary.SUBJECT, sumInfo.getSubject() ));
			//url
			info.append( createElement( XMLDictionary.URL));
			//����
			info.append( createElement( XMLDictionary.AUTHOR, sumInfo.getAuthor() ));
			//�ؼ���
			info.append( createElement( XMLDictionary.KEYWORD, sumInfo.getKeywords() ));
			//��ע
			info.append( createElement( XMLDictionary.REMARK, sumInfo.getComments()) );
			//�ֽ���
			info.append( createElement( XMLDictionary.WORDCOUNT, String.valueOf( docInfo.getByteCount()) ) );
			//ģ��
			info.append( createElement( XMLDictionary.TEMPLATE, sumInfo.getTemplate()) );
			//��λ
			info.append( createElement( XMLDictionary.DEPARTMENT, docInfo.getCompany()));
			//���
			info.append( createElement( XMLDictionary.SORT, docInfo.getCategory()));
			//�ϴα�����
			info.append( createElement( XMLDictionary.LASTAUTHOR, sumInfo.getLastAuthor()) );
			//�޶�����
			info.append( createElement( XMLDictionary.MODIFYCOUNT, sumInfo.getRevNumber()) );
			//�༭�ĵ����ѵ�ʱ��
			info.append( createElement( XMLDictionary.EDITTIME,  sumInfo.getEditTime() / ( 10000000 * 60 ) + "" ) );
			//����ʱ��
			info.append( createElement( XMLDictionary.CREATETIME, dateFormat( sumInfo.getCreateDateTime())) );
			//��ӡʱ��
			info.append( createElement( XMLDictionary.PRINTTIME, dateFormat( sumInfo.getLastPrinted() ) ) );
			return info;
		}catch( Exception e )
		{
			throw e;
		}
	}
	
	/**
	 * ����ļ�����ʧ�ܻ������ļ��� �ǿձ�ǩ
	 * @throws Exception
	 */
	public static void getNullSFFile( String sfPath ) throws Exception
	{
		StringBuffer info = new StringBuffer();
		//�ĵ�����
		info.append( createElement( XMLDictionary.TEXTTYPE ) );
		//����
		info.append( createElement( XMLDictionary.TITLE ));
		//����
		info.append( createElement( XMLDictionary.SUBJECT ));
		//url
		info.append( createElement( XMLDictionary.URL));
		//����
		info.append( createElement( XMLDictionary.AUTHOR ));
		//�ؼ���
		info.append( createElement( XMLDictionary.KEYWORD ));
		//��ע
		info.append( createElement( XMLDictionary.REMARK) );
		//�ֽ���
		info.append( createElement( XMLDictionary.WORDCOUNT ) );
		//ģ��
		info.append( createElement( XMLDictionary.TEMPLATE ) );
		//��λ
		info.append( createElement( XMLDictionary.DEPARTMENT ));
		//���
		info.append( createElement( XMLDictionary.SORT ));
		//�ϴα�����
		info.append( createElement( XMLDictionary.LASTAUTHOR ) );
		//�޶�����
		info.append( createElement( XMLDictionary.MODIFYCOUNT ) );
		//�༭�ĵ����ѵ�ʱ��
		info.append( createElement( XMLDictionary.EDITTIME ));
		//����ʱ��
		info.append( createElement( XMLDictionary.CREATETIME ) );
		//��ӡʱ��
		info.append( createElement( XMLDictionary.PRINTTIME ) );
		info.append( "\n" );
		InfoUtil.appendSFInfo(sfPath, info.toString() );
		
	}
	
	/**
	 * ����xml�ĵ�
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
			   	//���ñ����ʽ
			    transformer.setOutputProperty(OutputKeys.ENCODING, ENCODING );
			    //�Ƿ��������
			    transformer.setOutputProperty( OutputKeys.INDENT, INDENT  );
			    
			    DOMSource source = new DOMSource(document);
			    StreamResult result = new StreamResult(new File(filename));
			    transformer.transform(source, result);
		   } catch (Exception ex) {
			    throw ex;
		   }
	}
	
	/**
	 * ��ȡ�ļ��д洢��Ӧ���ĵ���Ϣ(xml,ͼƬ��)
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
	 * �����ĵ���·����Ϣ,�����Ϊ���·��
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
	 * �����ĵ���·����Ϣ
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
	 * ��ȡ�ĵ�����
	 * @param file	�ĵ�
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
	 * ��ȡppt�ĵ��е�ͼƬ��ʽ
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
	 * ��ȡ��ʽ�����ʱ����Ϣ
	 * @param dar	ʱ����Ϣ
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
	 * ��ȡ��ʽ�����ʱ����Ϣ
	 * @param d	ʱ����Ϣ
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
	 * ��ȡ��ʽ�����ʱ����Ϣ
	 * @param l	ʱ����Ϣ
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
	 * ����ʱ���ȡ����
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
	 * �޳�֮����ַ���Ϣ
	 * @param content	����
	 * @param start	��ʼ
	 * @param end	����
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
	 * ����������ʽ�޳���Ϣ
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
	 * ��ȡhtml�е�title��ǩ
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
	 * ��ȡ�ļ�����
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
	 * ׷��sf�ĵ���Ϣ
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
	 * ��ȡ�ĵ�������Ϣ
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
	 * ��ȡ�ĵ�������Ϣ
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
		System.out.println( f.getCanonicalPath() + "�������==========�����ļ�Ϊ:" + savePath + "��" + XMLDictionary.COUNT + "���ļ�" );
		XMLDictionary.COUNT++;
		return savePath;
	}
	
	/**
	 * ���������ַ�
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String StringFilter(String  str) throws PatternSyntaxException   
	{     
		  
		 StringBuffer sb = new StringBuffer();
		 //���̿������������ *[^[`!@# %^*()_\\+\\-={}\\[\\]:,|\";',.?/\\s��~������-��������������������������������������]<>&+$]*[^[\u4E00-\u9FFF]+$]
		 //�����ȡ���֣���ĸ�����֣����壩�Լ����м��̿������������
		 String   regEx  =  "[^[\u4E00-\u9FFF]+$]*[^[a-zA-Z0-9]+$]*[^[`!@#\\$\\%^*()_\\+\\-={}\\[\\]:,|\";',.?/��~������-��������������������������������������]<>&+$]*[^[\u4E00-\u9FFF]+$]*";
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
