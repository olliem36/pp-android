package classes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import data.SQLiteHelper;

public class User implements Serializable
{
	public enum UserType {
		CHILD(1), PARENT(2), ASSISTANT(3), TEACHER(4), MANAGER(5), HEADTEACHER(6);
	 
		private int statusCode;
	 
		private UserType(int i) {
			statusCode = i;
		}
	 
		public int getTypeID() {
			return statusCode;
		}
	 
	}
	
	public int onlineUserID;
	public int mobileUserID;
	public String firstname;
	public String surname;
	public String email;
	public String username;
	public int ethinicityID;
	public int userTypeID;
	public Date dob;
	
	public String fullname()
	{
		return this.firstname + " " + this.surname;
	}

	public User(int onlineUserID, int mobileUserID, String firstname, String surname, String email, String username, int ethinicityID, int userTypeID, Date dob)
	{
		this.onlineUserID = onlineUserID;
		this.mobileUserID = mobileUserID;
		this.firstname = firstname;
		this.surname = surname;
		this.email = email;
		this.username = username;
		this.ethinicityID = ethinicityID;
		this.userTypeID = userTypeID;
		this.dob = dob;
	}
	
	public String getdob_SQLITE()
	{
		SimpleDateFormat sdf = new SimpleDateFormat(SQLiteHelper.DATE_FORMAT_SQLITE);
		
		if(dob == null)
			return "";
		
		return sdf.format(dob);
	}
}
