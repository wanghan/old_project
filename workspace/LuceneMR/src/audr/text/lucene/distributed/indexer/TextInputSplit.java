/**
 * 
 */
package audr.text.lucene.distributed.indexer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

/**
 * @author wanghan
 *
 */
public class TextInputSplit extends InputSplit implements Writable{

	
	private int index=0;
	private ArrayList<String> ID=new ArrayList<String>();
	private ArrayList<Path> path=new ArrayList<Path>();
	private ArrayList<String> orifilename=new ArrayList<String>();
	private ArrayList<String> orifilepath=new ArrayList<String>();
	
	public TextInputSplit() {
		// TODO Auto-generated constructor stub
	}
	
	public TextInputSplit(int num,ArrayList<String> id,ArrayList<Path> paths,ArrayList<String> name,ArrayList<String> path){
		this.ID=id;
		this.path=paths;
		this.orifilename=name;
		this.orifilepath=path;
		this.index=num;
	}
	
	@Override
	public long getLength() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String[] getLocations() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return new String[0];
	}
	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		this.index=in.readInt();
		int number=in.readInt();
		for(int i=0;i<number;++i){
			String temp=in.readUTF();
			ID.add(temp);
			temp=in.readUTF();
			orifilename.add(temp);
			temp=in.readUTF();
			orifilepath.add(temp);
			
		    byte[]  buffer = new byte[in.readInt()];
		    in.readFully(buffer);
		    path.add( new Path(new String(buffer)));
			
		}
		

	}
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(this.index);
		int number=ID.size();
		out.writeInt(number);
		for(int i=0;i<number;++i){
			if(ID.get(i)==null){
				System.out.println();
				continue;
			}
			out.writeUTF(ID.get(i));
			out.writeUTF(orifilename.get(i));
			out.writeUTF(orifilepath.get(i));
			
			byte[] uri = path.get(i).toUri().getPath().getBytes();
			out.writeInt(uri.length);
	    	out.write(uri);		
		}

    }

	public ArrayList<String> getID() {
		return ID;
	}

	public void setID(ArrayList<String> id) {
		ID = id;
	}

	public ArrayList<Path> getPath() {
		return path;
	}

	public void setPath(ArrayList<Path> path) {
		this.path = path;
	}

	public ArrayList<String> getOrifilename() {
		return orifilename;
	}

	public void setOrifilename(ArrayList<String> orifilename) {
		this.orifilename = orifilename;
	}

	public ArrayList<String> getOrifilepath() {
		return orifilepath;
	}

	public void setOrifilepath(ArrayList<String> orifilepath) {
		this.orifilepath = orifilepath;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	
}
