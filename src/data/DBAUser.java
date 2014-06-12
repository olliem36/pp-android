package data;

import classes.User;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//Contains database queries related to user objects

public class DBAUser 
{
	private SQLiteDatabase myDataBase; 
	
	public DBAUser(SQLiteDatabase dataBase )
	{
		myDataBase = dataBase;
	}

	public Cursor getChildren(int mobileTeacherID)
	{
		String[] whereArgs = new String[] {
				String.valueOf(mobileTeacherID)
			};
		
		String queryString =
				"SELECT " +
                "USR.onlineUserID, "+
                "USR.mobileUserID AS _id, "+
                "USR.firstname, "+
                "USR.surname, "+
                "USR.dob, "+
                "USR.authTicket, "+
                "USR.pinCode, "+
                "USR.userTypeID, "+
                "USR.ethnicityID, "+
                "USR.email, "+
                "USR.username "+
                "FROM "+
                "tbl_user USR "+
                "INNER JOIN tbl_ethnicity ETH ON ETH.ethnicityID=USR.ethnicityID "+
                "INNER JOIN tbl_student_x_teacher TEACH ON TEACH.mobileStudentID=USR.mobileUserID "+
                "WHERE "+
                "TEACH.mobileTeacherID=?";
		
		return myDataBase.rawQuery(queryString, whereArgs);
	}
	
	public int createuser(User user)
	{
		ContentValues values = new ContentValues();
		values.put("onlineUserID", user.onlineUserID); 
		values.put("firstname", user.firstname);
		values.put("surname", user.surname);
		values.put("dob", user.getdob_SQLITE());
		values.put("authTicket", "");
		values.put("pinCode", "0");
		values.put("userTypeID", user.userTypeID);
		values.put("ethnicityID", user.ethinicityID);
		values.put("email", user.email);
		values.put("username", user.username);
		
		return (int) myDataBase.insert("tbl_user", null, values);
	}
	
	public void createTeachesRecord(int mobileTeacherID, int modileChildID)
	{
		ContentValues values = new ContentValues();
		values.put("mobileStudentID", modileChildID); 
		values.put("mobileTeacherID", mobileTeacherID);
		
		try
		{
			myDataBase.insert("tbl_student_x_teacher", null, values);			
		}
		catch(SQLiteConstraintException e)
		{
			Log.i("pp", "already teaches this person");
		}
	}
	
	public int updateuser(User user)
	{
		ContentValues values = new ContentValues();
		values.put("onlineUserID", user.onlineUserID); 
		values.put("firstname", user.firstname);
		values.put("surname", user.surname);
		values.put("dob", user.getdob_SQLITE());
		values.put("authTicket", "");
		values.put("pinCode", "0");
		values.put("userTypeID", user.userTypeID);
		values.put("ethnicityID", user.ethinicityID);
		values.put("email", user.email);
		values.put("username", user.username);
		
		myDataBase.update("tbl_user", values, "mobileUserID="+user.mobileUserID, null);
		return 0;
	}
	
	public int userExists(User user) 
	{
		Cursor cursor= myDataBase.rawQuery("SELECT mobileUserID FROM tbl_user WHERE onlineUserID=?",
		         new String[] { String.valueOf(user.onlineUserID) });
		
		int mobileUserID = 0;
		if(null != cursor)
		{
		    if(cursor.getCount() > 0){
		      cursor.moveToFirst();    
		      mobileUserID = cursor.getInt(0);
		    }
		    cursor.close();
		}

		return mobileUserID;
	}
}
