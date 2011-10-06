package audr.text.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/**
 * Wrapped api for HDFS operation.
 * 
 * @author yilong
 * @version 1.0
 */
public class HdfsHelper {
	Configuration conf = new Configuration();

	/**
	 * No parameter is acquired if you are running *nix or BSD. You can call
	 * this constructor in this case.
	 */
	public HdfsHelper() {
	}

	/**
	 * We must set the user first if we are using windows platform, or failure
	 * occurs.
	 * 
	 * @param userandsuper
	 *            something like this "xlliu,supergroup"
	 */
	public HdfsHelper(String userandsuper) {
		conf.setStrings("hadoop.job.ugi", userandsuper);
	}

	/**
	 * Delete a remote file in the HDFS cluster.
	 * 
	 * @param fileuri
	 */
	public void deleteRemote(String fileuri) {
		return;
	}

	/**
	 * Upload a local file to the HDFS cloud.
	 * 
	 * @param local
	 *            local file path to upload
	 * @param remote
	 *            target URI to store in HDFS
	 * @throws IOException
	 */
	public void upload(String local, String remote) throws IOException {
		FileSystem fs = FileSystem.get(URI.create(remote), conf);
		FileInputStream in = null;
		FSDataOutputStream out = null;

		try {
			in = new FileInputStream(local);
			out = fs.create(new Path(remote));
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			in.close();
			throw e;
		}

		try {
			IOUtils.copyBytes(in, out, conf, false);
		} finally {
			in.close();
			IOUtils.closeStream(out);
		}
	}

	/**
	 * Download file from HDFS.
	 * 
	 * @param remote
	 *            URI of the file in HDFS
	 * @param local
	 *            local file path to store the target file
	 * @throws IOException
	 */
	public void download(String remote, String local) throws IOException {
		FileSystem fs = FileSystem.get(URI.create(remote), conf);
		FSDataInputStream in = null;
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(local);
			in = fs.open(new Path(remote));
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			out.close();
			throw e;
		}
		try {
			IOUtils.copyBytes(in, out, conf, false);
		} finally {
			out.close();
			IOUtils.closeStream(in);
		}
	}

	/**
	 * It's present only for test's sake, you can take as an example if you
	 * like.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		HdfsHelper hdfsHelper = new HdfsHelper("xlliu,supergroup");

		String localInputFile = "D:/Data/content_xml/2010031817075212689032721401.xml";
		String localOutputFile = "D:/test1.xml";
		String remoteUri = "hdfs://192.168.102.100:9000/user/audr/text/201003181707551268903275968999.xml.xml";

		hdfsHelper.upload(localInputFile, remoteUri);
		hdfsHelper.download(remoteUri, localOutputFile);

		System.out.println("success.");
	}
}
