package data;

import java.util.ArrayList;

import utils.Utils;

import classes.File;
import classes.Folder;
import classes.User;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// Contains database queries related to filesystem objects


public class DBAFileSystem {

	private SQLiteDatabase myDataBase; 
	
	public DBAFileSystem(SQLiteDatabase dataBase ){
		myDataBase = dataBase;
	}
	
	
	public int createFile(File file, int userID)
	{
		ContentValues values = new ContentValues();
		
		if(file.mobileID > 0){
			values.put("mobileFileID", file.mobileID);			
		}
		
		values.put("realName", file.realName);
		values.put("fileName", file.name);
		values.put("fileDescription", file.fileDescription);
		values.put("MIMEType", file.MIMEType);
		values.put("fileSizeBytes", file.fileSizeBytes);
		values.put("fileCreated", file.dateCreated.toGMTString());
		values.put("fileModified", file.dateModified.toGMTString());
		values.put("bin", (file.bin ? 1 : 0));
		values.put("mobileFolderID", file.mobileFolderID);

		file.mobileID = (int) myDataBase.insertOrThrow("tbl_file", null, values);
		
		ContentValues uxfContentValues = new ContentValues();
		uxfContentValues.put("mobileUserID", userID); 
		uxfContentValues.put("mobileFileID", file.mobileID);
		
		myDataBase.insertOrThrow("tbl_user_x_files", null, uxfContentValues);
		
		return file.mobileID;
	}

	public ArrayList<File> getFiles(int mobileFolderID, int userID)
	{
		String[] whereArgs = new String[] {
				String.valueOf(mobileFolderID), String.valueOf(userID)
			};
		
		String queryString =
				"SELECT " +
                "FILE.onlineFileID, "+
                "FILE.mobileFileID AS _id, "+
                "FILE.fileSizeBytes, "+
                "FILE.realName, "+
                "FILE.fileDescription, "+
                "FILE.fileCreated, "+
                "FILE.mobileFolderID, "+
                "FILE.fileName, "+
                "FILE.bin, "+
                "FILE.fileModified, "+
                "FILE.MIMEType, "+
                "PFOLDER.onlineFolderID " +
                "FROM "+
                "tbl_file FILE "+
                "LEFT OUTER JOIN tbl_folder PFOLDER ON FILE.mobileFolderID=PFOLDER.mobileFolderID "+
                "INNER JOIN tbl_user_x_files UXF ON FILE.mobileFileID=UXF.mobileFileID "+
                "WHERE "+
                "FILE.bin=0 AND " +
                "FILE.mobileFolderID=? AND " +
                "UXF.mobileUserID=?";
		
		Cursor folderCursor = myDataBase.rawQuery(queryString, whereArgs);
		ArrayList<File> files = new ArrayList<File>();
		 		
		while (folderCursor.moveToNext()) {
			
			File file = new File();
			file.onlineID = folderCursor.getInt(folderCursor.getColumnIndexOrThrow("onlineFileID"));
			file.mobileID = folderCursor.getInt(folderCursor.getColumnIndexOrThrow("_id"));
			file.fileSizeBytes = folderCursor.getInt(folderCursor.getColumnIndexOrThrow("fileSizeBytes"));
			file.name = folderCursor.getString(folderCursor.getColumnIndexOrThrow("fileName"));
			file.realName = folderCursor.getString(folderCursor.getColumnIndexOrThrow("realName"));
			file.fileDescription = folderCursor.getString(folderCursor.getColumnIndexOrThrow("fileDescription"));
			file.dateCreated = Utils.dateFromString(folderCursor.getString(folderCursor.getColumnIndexOrThrow("fileCreated")));
			file.mobileFolderID = folderCursor.getInt(folderCursor.getColumnIndexOrThrow("mobileFolderID"));
			file.bin = (folderCursor.getInt(folderCursor.getColumnIndexOrThrow("bin")) == 1);
			file.dateModified = Utils.dateFromString(folderCursor.getString(folderCursor.getColumnIndexOrThrow("fileModified")));
			file.onlineFolderID = folderCursor.getInt(folderCursor.getColumnIndexOrThrow("onlineFolderID"));
			file.MIMEType = folderCursor.getString(folderCursor.getColumnIndexOrThrow("MIMEType"));

			files.add(file);
		}
		
		return files;
	}
	
	public int createFolder(Folder folder, int userID)
	{
		ContentValues values = new ContentValues();
		
		if(folder.mobileID > 0){
			values.put("mobileFolderID", folder.mobileID);			
		}
		
		values.put("onlineFolderID", folder.onlineID); 
		values.put("name", folder.name);
		values.put("created", folder.dateCreated.toGMTString());
		values.put("modified", folder.dateModified.toGMTString());
		values.put("bin", (folder.bin ? 1 : 0));
		values.put("mobileParentID", folder.mobileParentID);

		folder.mobileID = (int) myDataBase.insertOrThrow("tbl_folder", null, values);
		
		ContentValues uxfContentValues = new ContentValues();
		uxfContentValues.put("mobileUserID", userID); 
		uxfContentValues.put("mobileFolderID", folder.mobileID);
		
		myDataBase.insertOrThrow("tbl_user_x_folder", null, uxfContentValues);
		
		return folder.mobileID;
	}
	
	public ArrayList<Folder> getFolders(int mobileParentID, int userID)
	{
		String[] whereArgs = new String[] {
				String.valueOf(mobileParentID), String.valueOf(userID)
			};
		
		String queryString =
				"SELECT " +
                "FOLDER.onlineFolderID, "+
                "FOLDER.mobileFolderID AS _id, "+
                "FOLDER.name, "+
                "FOLDER.created, "+
                "FOLDER.mobileParentID, "+
                "FOLDER.bin, "+
                "FOLDER.modified, "+
                "PFOLDER.onlineFolderID " +
                "FROM "+
                "tbl_folder FOLDER "+
                "LEFT OUTER JOIN tbl_folder PFOLDER ON FOLDER.mobileParentID=PFOLDER.mobileFolderID "+
                "INNER JOIN tbl_user_x_folder UXF ON FOLDER.mobileFolderID=UXF.mobileFolderID "+
                "WHERE "+
                "FOLDER.bin=0 AND " +
                "FOLDER.mobileParentID=? AND " +
                "UXF.mobileUserID=?";
		
		Cursor folderCursor = myDataBase.rawQuery(queryString, whereArgs);
		ArrayList<Folder> folders = new ArrayList<Folder>();
		 		
		while (folderCursor.moveToNext()) {
			
			Folder folder = new Folder();
			folder.onlineID = folderCursor.getInt(folderCursor.getColumnIndexOrThrow("onlineFolderID"));
			folder.mobileID = folderCursor.getInt(folderCursor.getColumnIndexOrThrow("_id"));
			folder.name = folderCursor.getString(folderCursor.getColumnIndexOrThrow("name"));
			folder.dateCreated = Utils.dateFromString(folderCursor.getString(folderCursor.getColumnIndexOrThrow("name")));
			folder.mobileParentID = folderCursor.getInt(folderCursor.getColumnIndexOrThrow("mobileParentID"));
			folder.bin = (folderCursor.getInt(folderCursor.getColumnIndexOrThrow("bin")) == 1);
			folder.dateModified = Utils.dateFromString(folderCursor.getString(folderCursor.getColumnIndexOrThrow("modified")));
			folder.onlineParentFolderID = folderCursor.getInt(folderCursor.getColumnIndexOrThrow("onlineFolderID"));

			folders.add(folder);
		}
		
		return folders;
	}
	
	public Folder getFolder(int mobileID, int userID)
	{
		String[] whereArgs = new String[] {
				String.valueOf(mobileID), String.valueOf(userID)
			};
		
		String queryString =
				"SELECT " +
                "FOLDER.onlineFolderID, "+
                "FOLDER.mobileFolderID AS _id, "+
                "FOLDER.name, "+
                "FOLDER.created, "+
                "FOLDER.mobileParentID, "+
                "FOLDER.bin, "+
                "FOLDER.modified, "+
                "PFOLDER.onlineFolderID " +
                "FROM "+
                "tbl_folder FOLDER "+
                "LEFT OUTER JOIN tbl_folder PFOLDER ON FOLDER.mobileParentID=PFOLDER.mobileFolderID "+
                "INNER JOIN tbl_user_x_folder UXF ON FOLDER.mobileFolderID=UXF.mobileFolderID "+
                "WHERE "+
                "FOLDER.mobileID=? AND " +
                "UXF.mobileUserID=?";
		
		Cursor folderCursor = myDataBase.rawQuery(queryString, whereArgs);

		Folder folder = new Folder();

		while (folderCursor.moveToNext()) {
			folder.onlineID = folderCursor.getInt(folderCursor.getColumnIndexOrThrow("onlineFolderID"));
			folder.mobileID = folderCursor.getInt(folderCursor.getColumnIndexOrThrow("_id"));
			folder.name = folderCursor.getString(folderCursor.getColumnIndexOrThrow("name"));
			folder.dateCreated = Utils.dateFromString(folderCursor.getString(folderCursor.getColumnIndexOrThrow("name")));
			folder.mobileParentID = folderCursor.getInt(folderCursor.getColumnIndexOrThrow("mobileParentID"));
			folder.bin = (folderCursor.getInt(folderCursor.getColumnIndexOrThrow("bin")) == 1);
			folder.dateModified = Utils.dateFromString(folderCursor.getString(folderCursor.getColumnIndexOrThrow("modified")));
			folder.onlineParentFolderID = folderCursor.getInt(folderCursor.getColumnIndexOrThrow("onlineFolderID"));
		}
		
		return folder;
	}
}
