package data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import webservice.DownloadImageTask;

import com.theteachermate.app.R;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public final class SQLiteHelper extends SQLiteOpenHelper 
{
	//singleton instance:
	private static SQLiteHelper mInstance = null;
    private Context mCxt;
    
    private static String DB_PATH = "/data/data/com.theteachermate.app/databases/";
    private static String DB_NAME = "PPDATABASE.sqlite";
 
    private static SQLiteDatabase myDataBase; 
    private static int DATABASE_VERSION = 1;
    
    public static final String DATE_FORMAT_SQLITE = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_WS1 = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_FORMAT_WS2 = "dd/MM/yyyy";
    
    //break the code up into different helper classes
    private static DBAUser dbaUser;
    private static DBAFileSystem dbaFileSystem;
    
    public HashMap<Integer,Bitmap> imageCache = new HashMap<Integer,Bitmap>();
    
    public Bitmap getProfilePicture(Integer userID, ImageView imageview){
    	Bitmap bitmap = imageCache.get(userID);
    	
    	if(bitmap == null){
    		bitmap = BitmapFactory.decodeResource(mCxt.getResources(), R.drawable.profile_placeholder);
    	}
    	
    	imageview.setImageBitmap(bitmap);
    	
    	String profileImageURL = "http://precise-duality-278.appspot.com/profilepicture.php?userID=" + userID;
        
        new DownloadImageTask(imageview, mCxt, userID).execute(profileImageURL);
    	
    	return bitmap;
    }
    
    public static Date parseDateString(String strDate){
		Date date = null;
		
		try{
			date = new SimpleDateFormat(SQLiteHelper.DATE_FORMAT_WS1).parse(strDate);	
		}catch (ParseException e){
			try{
				date = new SimpleDateFormat(SQLiteHelper.DATE_FORMAT_WS2).parse(strDate);
			} catch (ParseException e1){
				Log.w("pp", "Warning: cannot parse date string: " + strDate + " so using todays date");
				date = new Date();
			}
		}
		return date;
    }
    
    public static DBAUser getDBAUser(){
        if (dbaUser == null) {
        	dbaUser = new DBAUser(myDataBase);
        }
        return dbaUser;
    }
    
    public static DBAFileSystem getDBAFileSystem() 
    {
        if (dbaFileSystem == null){
        	dbaFileSystem = new DBAFileSystem(myDataBase);
        }
        return dbaFileSystem;
    }
    
    public Cursor testTables(){
    	return myDataBase.rawQuery("SELECT * FROM sqlite_master WHERE type='table'", null);
    }
    
    public static SQLiteHelper getInstance(Context ctx){
        if (mInstance == null){
            mInstance = new SQLiteHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
 
    private SQLiteHelper(Context ctx){
        super(ctx, DB_NAME, null, DATABASE_VERSION);
        this.mCxt = ctx;
		try {
			createDataBase();
			openDataBase();
		}catch (IOException e){
			Log.e("pp", "CANNOT CREATE DATABASE");
			e.printStackTrace();
		}
    }
    
    public void createDataBase() throws IOException{
    	boolean dbExist = checkDataBase();
    	if(!dbExist){
        	this.getReadableDatabase();
 
        	try{
    			copyDataBase();
    		} catch (IOException e){
        		throw new Error("Error copying database");
        	}
    	}
    }
 
    private boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		Log.i("pp", "Using database path:"+myPath);
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
    		Log.i("pp", "database does't exist yet");
    	}
 
    	if(checkDB != null){
    		checkDB.close();
    	}
 
    	return checkDB != null ? true : false;
    }
 
    private void copyDataBase() throws IOException{
    	InputStream myInput = mCxt.getAssets().open(DB_NAME);
    	String outFileName = DB_PATH + DB_NAME;
 
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
    	
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }
 
    public void openDataBase() throws SQLException{
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
 
    @Override
	public synchronized void close() {

    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
}
