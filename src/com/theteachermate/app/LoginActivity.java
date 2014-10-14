package com.theteachermate.app;

import interfaces.LoginWSInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dd.processbutton.iml.ActionProcessButton;

import data.SQLiteHelper;

import webservice.WSAuthTask;
import webservice.WSGetChildrenTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class LoginActivity extends Activity implements LoginWSInterface
{
    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";

    
	private EditText txtUsername;
	private EditText txtPassword;
	private ActionProcessButton btnLogin; //  -->
	private ProgressBar progressBar;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
   
        setContentView(R.layout.activity_login);
                
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        
        txtUsername = (EditText)findViewById(R.id.login_username);
        txtPassword = (EditText)findViewById(R.id.login_password);
        btnLogin = (ActionProcessButton)findViewById(R.id.login_button);
		progressBar = (ProgressBar) findViewById(R.id.ProgressBar01);
        
		btnLogin.setMode(ActionProcessButton.Mode.ENDLESS);
		
		progressBar.setVisibility(View.GONE);
		
        btnLogin.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View view) 
		   {
			   btnLoginPressed();
		   }
		});
    }
	
	private void btnLoginPressed()
	{
		String username = txtUsername.getText().toString().trim();
		String password = txtPassword.getText().toString().trim();
				
		if(username.length()==0 || password.length() == 0)
		{
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
			dlgAlert.setMessage("Please enter a username and password");
			dlgAlert.setTitle("Login");
			dlgAlert.setPositiveButton("Ok",
				    new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				          //dismiss the dialog  
				        }
				    });
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
			return;
		}
		
		btnLogin.setProgress(2);
		
		progressBar.setVisibility(View.VISIBLE);
		progressBar.startAnimation(AnimationUtils.loadAnimation(this,R.drawable.splash_spinner));
		//btnLogin.setVisibility(View.GONE);
		txtUsername.setEnabled(false);
		txtPassword.setEnabled(false);
				
		WSAuthTask asAuthTask = new WSAuthTask();
		asAuthTask.execute(username, password, LoginActivity.this, this);		
	}
	
	
	private void downloadChildren(String access_token)
	{
		WSGetChildrenTask wsGetChildrenTask = new WSGetChildrenTask();
		
		SharedPreferences prefs = this.getApplicationContext().getSharedPreferences("com.theteachermate.app", Context.MODE_PRIVATE);
		int userID = prefs.getInt("userID", 0);
		
		wsGetChildrenTask.execute(access_token, userID, LoginActivity.this);
	}
	
	@Override
	public void loginCompleted(final String access_token, final String error) {

		LoginActivity.this.runOnUiThread(new Runnable() {
		    public void run() {
		    
		    	/*
		if(access_token.length() == 0) {
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
			 
			alertDialogBuilder.setTitle("Error");
			alertDialogBuilder
				.setMessage(error)
				.setCancelable(false)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
					}
				  });
	    	
	    	AlertDialog alertDialog = alertDialogBuilder.create();
	    	alertDialog.show();
	    	
			progressBar.setVisibility(View.GONE);
			//btnLogin.setVisibility(View.VISIBLE);
			txtUsername.setEnabled(true);
			txtPassword.setEnabled(true);
			btnLogin.setProgress(100);

			return;
		}
		*/
		    	
		downloadChildren(access_token);
		
		Intent myIntent = new Intent(LoginActivity.this, FragmentChangeActivity.class);
		LoginActivity.this.startActivity(myIntent);
		
		progressBar.setVisibility(View.GONE);
		//btnLogin.setVisibility(View.VISIBLE);
		txtUsername.setEnabled(true);
		txtPassword.setEnabled(true);
		btnLogin.setProgress(100);
		}});
	}
}
