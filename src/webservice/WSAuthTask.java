package webservice;

import interfaces.LoginWSInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import classes.User;
import data.DBAUser;
import data.SQLiteHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class WSAuthTask extends AsyncTask<Object, String, String> {

	private Context ctx;
	private DBAUser dbaUser;
	private LoginWSInterface callback;
	private String userMessage = "";
	
	@Override
	protected String doInBackground(Object... params) 
	{
		String username = (String) params[0];
		String password = (String) params[1];
		ctx = (Context) params[2];
		callback = (LoginWSInterface) params[3];
		
		dbaUser = SQLiteHelper.getInstance(ctx).getDBAUser();
		
		String str="http://precise-duality-278.appspot.com/api/auth.php?username="+ URLEncoder.encode(username)+"&password=" + URLEncoder.encode(password);
		String access_token = "";
		String error = "";
		
	    try
	    {
	        URL url=new URL(str);

	        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
	        HttpURLConnection.setFollowRedirects(true);
	        huc.setConnectTimeout(6 * 1000);
	        huc.setRequestMethod("GET");
	        huc.setRequestProperty("User-Agent", "Android App 1.0");
	        huc.connect();
	        
	        BufferedReader bfr=new BufferedReader(new InputStreamReader(huc.getInputStream()));
	        
	        String line;
	        while((line=bfr.readLine())!=null) {
	        	Log.i("pp", "reponse line: " + line);
	        		        		        	
	        	JSONObject jsa = new JSONObject(line);
	        	
	        	Object responseObject = jsa.get("response");
	        	
	        	if(responseObject instanceof String) {
	        		error = (String) responseObject;
		        	Log.i("pp", "response error: " + error);
		        	userMessage = error;
	        	} else if(responseObject instanceof JSONObject) {
	        		JSONObject responseDict = (JSONObject) responseObject;
	        		access_token = responseDict.getString("authTicket");
	        		
	        		String firstname = responseDict.getString("firstname");
        			String surname = responseDict.getString("surname");
        			Integer userID = responseDict.getInt("userID");
        			String email = responseDict.getString("email");
        			
        			Log.i("pp", "auth completed:");
        			Log.i("pp", "userID:" + userID);
        			Log.i("pp", "firstname:" + firstname);
        			Log.i("pp", "surname:" + surname);
        			Log.i("pp", "email:" + email);
        			
        			User user = new User(userID, 0, firstname, surname, email, username, 0, User.UserType.TEACHER.getTypeID(), null);
        			
        			user.mobileUserID = dbaUser.userExists(user);
        			
        			if(user.mobileUserID > 0)
        			{
        				dbaUser.updateuser(user);
        			}
        			else
        			{
        				user.mobileUserID = dbaUser.createuser(user);
        			}

        			
	        		SharedPreferences prefs = ctx.getApplicationContext().getSharedPreferences("com.theteachermate.app", Context.MODE_PRIVATE);
	    			prefs.edit().putString("access_token", access_token).commit();
	    			prefs.edit().putInt("userID", user.mobileUserID).commit();
	    			Log.i("pp", "saved (commit2) local userID:" + user.mobileUserID);
	    			
	        	}else{
	        		userMessage = "Sorry, there was an unexpected response from the server";
	        	}
	        	
		        //for(int i=0;i<jsa.length();i++)
		        //{
		           //JSONObject jo=(JSONObject)jsa.get(i);
		           // title=jo.getString("deal_title");  //tag name "deal_title",will return value that we save in title string
		           // des=jo.getString("deal_description");
		        //}
	        }
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    	Log.i("pp","error:" + e.getMessage());
	    	callback.loginCompleted(access_token, e.getMessage()); // error callback?
	    }

		return access_token;
	}

	protected void onPostExecute(String access_token) {
		Log.i("pp", "Finished with access_token: " + access_token);	
		if(userMessage == null){ userMessage = ""; }
 		callback.loginCompleted(access_token, userMessage);
    }
}
