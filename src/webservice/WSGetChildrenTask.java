package webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import classes.User;

import data.DBAUser;
import data.SQLiteHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class WSGetChildrenTask extends AsyncTask<Object, String, String> 
{
	
	private Context ctx;
	private DBAUser dbaUser;

	@Override
	protected String doInBackground(Object... params) 
	{
		String access_token = (String) params[0];
		Integer userID = (Integer) params[1];
		ctx = (Context) params[2];
		
		dbaUser = SQLiteHelper.getInstance(ctx).getDBAUser();

		String str="http://precise-duality-278.appspot.com/api/getchildren.php?authTicket="+ URLEncoder.encode(access_token)+"&userID="+userID;
		String error = "";
		
	    try
	    {
	        URL url=new URL(str);
	        URLConnection urlc=url.openConnection();
	        BufferedReader bfr=new BufferedReader(new InputStreamReader(urlc.getInputStream()));
	        String line;
	        while((line=bfr.readLine())!=null)
	        {
	        	Log.i("pp", "getchildren reponse line: " + line);
	        		        		        	
	        	JSONObject jsa = new JSONObject(line);
	        	
	        	Object responseObject = jsa.get("response");
	        	
	        	if(responseObject instanceof String)
	        	{
	        		error = (String) responseObject;
		        	Log.i("pp", "response error: " + error);
	        	}
	        	else if(responseObject instanceof JSONObject)
	        	{
	        		JSONObject responseDict = (JSONObject) responseObject;
	        		access_token = responseDict.getString("authTicket");
	        		
	        		JSONArray childrenObject = jsa.getJSONArray("children");
		        	
	        		Log.i("pp", "NUmber of children found:" + childrenObject.length());
	        		
	        		for(int x = 0; x < childrenObject.length(); x++)
	        		{
	        			JSONObject childDict = (JSONObject) childrenObject.get(x);
	        			
	        			String firstname = childDict.getString("firstname");
	        			String strDOB = childDict.getString("dob");
	        			String surname = childDict.getString("surname");
	        			Integer childID = childDict.getInt("userID");
	        			Integer ethnicityID = childDict.getInt("ethnicityID");
	        			
	        			Log.i("pp","firstname: " + firstname);
	        			Log.i("pp","surname: " + surname);
	        			Log.i("pp","dob: " + strDOB);
	        			Log.i("pp","childID: " + childID);
	        			Log.i("pp","ethnicityID: " + ethnicityID);

	        			String email = "";
	        			String username = "";
	        			
	        			Date dob = SQLiteHelper.parseDateString(strDOB);
	        			
	        			User child = new User(childID, 0, firstname, surname, email, username, ethnicityID, User.UserType.CHILD.getTypeID(), dob);
	        			
	        			child.mobileUserID = dbaUser.userExists(child);
	        			
	        			if(child.mobileUserID > 0)
	        			{
	        				dbaUser.updateuser(child);
	        			}
	        			else
	        			{
	        				child.mobileUserID = dbaUser.createuser(child);
	        			}
	        			
	        			dbaUser.createTeachesRecord(userID, child.mobileUserID);
	        		}
	        	}
	        }
	    }
	    
	    catch(Exception e){
	    	e.printStackTrace();
	    	Log.i("pp","error:" + e.getMessage());
	    }

		return access_token;
	}

	protected void onPostExecute(String access_token) 
	{
		Log.i("pp", "finished with access_token: " + access_token);		
    }
}
