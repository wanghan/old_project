package document;

import java.io.File;

public class DocumentHandler {
	
	protected File file;
	public DocumentHandler(File file) {
		// TODO Auto-generated constructor stub
		this.file=file;
	}
	
	public File getFile() {
		return file;
	}

	public Document getDocument(){
		return null;
	}
}
