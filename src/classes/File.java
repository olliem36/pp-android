package classes;

import java.io.Serializable;

import android.net.Uri;
import android.os.Environment;

public class File extends FileSystemObject implements Serializable {
	public int mobileFolderID;
	public int onlineFolderID;
	public String fileDescription;
	public String realName;
	public String MIMEType;
	public long fileSizeBytes;
	
	public Uri getFileUri() {
		java.io.File file = new java.io.File(Environment.getExternalStorageDirectory() + "/.PP", realName);
	    Uri fileUri = Uri.fromFile(file);
	    return fileUri;
	}
}


