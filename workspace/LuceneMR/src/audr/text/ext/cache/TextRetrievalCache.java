package audr.text.ext.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class TextRetrievalCache {

	String tableName = "TextSearchCache";
	String columnName = "Content";
	String dateColumnName = "Date";

	final int CACHE_TTL = 6000; // 6000s
	HBaseConfiguration config = new HBaseConfiguration();

	static byte[] getBytes(Object obj) throws java.io.IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		bos.close();
		byte[] data = bos.toByteArray();
		return data;
	}

	static Object fromBytes(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bis);
		Object obj = ois.readObject();

		ois.close();
		bis.close();
		return obj;
	}

	void createTable() throws IOException {
		HBaseAdmin admin = new HBaseAdmin(config);

		if (admin.tableExists(tableName))
			return;

		System.err.println("creating table...");
		HTableDescriptor tableDescripter = new HTableDescriptor(tableName
				.getBytes());
		tableDescripter.addFamily(new HColumnDescriptor(columnName));
		tableDescripter.addFamily(new HColumnDescriptor(dateColumnName));

		admin.createTable(tableDescripter);
		System.err.println("creat table successfully!");
	}

	void removeTable() throws IOException {
		HBaseAdmin admin = new HBaseAdmin(config);
		if (admin.tableExists(tableName))
			admin.deleteTable(tableName);
	}

	public Object getData(String key) throws IOException,
			ClassNotFoundException {
		HTable table = new HTable(config, tableName);
		Get g = new Get(Bytes.toBytes(key));
		Result r = table.get(g);
		byte[] value = r.getValue(Bytes.toBytes(columnName));
		byte[] datebytes = r.getValue(Bytes.toBytes(dateColumnName));
		if (value == null || value.length == 0)
			return null;
		Object obj = fromBytes(value);
		Date date = (Date) fromBytes(datebytes);
		long interval = (new Date().getTime() - date.getTime()) / 1000;
		if (interval > CACHE_TTL) {
			System.err.println("cache expires");
			return null;
		}
		System.out.println(date);
		System.out.println(obj);
		return obj;
	}

	public void putData(String key, Object data) throws IOException {
		HTable table = new HTable(config, tableName);
		Put p = new Put(Bytes.toBytes(key));
		p.add(Bytes.toBytes(columnName), Bytes.toBytes(""), getBytes(data));
		p.add(Bytes.toBytes(dateColumnName), Bytes.toBytes(""),
				getBytes(new Date()));
		table.put(p);
	}

	public TextRetrievalCache() throws IOException {
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		TextRetrievalCache cache = new TextRetrievalCache();
		// cache.removeTable();
		// cache.createTable();
		cache.putData("www.baidu.com", "Hello");
		cache.getData("www.baidu.com");
		cache.putData("www.baidu.com", "sadfsdf");
		cache.getData("www.baidu.com");
		cache.getData("g");
	}
}
