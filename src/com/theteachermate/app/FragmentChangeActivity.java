package com.theteachermate.app;

import java.util.Date;

import slidemenu.SlideOutMenuFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import classes.File;
import classes.Folder;

import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import data.DBAFileSystem;
import data.SQLiteHelper;
import dialogs.NewFileDialog;

public class FragmentChangeActivity extends BaseActivity {
	
	private static final int MEDIA_TYPE_IMAGE = 1;
	private static final int MEDIA_TYPE_VIDEO = 2;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	private static final int IMPORT_GALLERY_ACTIVITY_REQUEST_CODE = 300;
	
	private Fragment mContent;
	
	public FragmentChangeActivity() {
		super(R.string.home_title);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set the Above View
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new HomeMenuFragment();	
		
		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();
		
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new SlideOutMenuFragment())
		.commit();
		
		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) 
		{

		case android.R.id.home:
			if(mContent instanceof FilesFragment){
        		Folder parentFolder = ((FilesFragment) mContent).folder;
        		
    			if(parentFolder.mobileID != 0){
    				//pop back a folder    		        
    				getSupportFragmentManager().popBackStack();
    			}else{
    				//toggle side menu
    				getSlidingMenu().toggle();
    			}
			}
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	    
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
	public void switchContent(Fragment fragment) 
	{
		mContent = fragment;
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content_frame, fragment);
		transaction.commit();
		
		getSlidingMenu().showContent();
	}

	public void pushFragment(Fragment fragment) 
	{
		mContent = fragment;

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content_frame, fragment);
		transaction.addToBackStack("folderfrag");
		transaction.commit();
		getSlidingMenu().showContent();
	}


	  @Override
	  public void onBackPressed() {
	      if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
	    	  getSlidingMenu().toggle();
	          //this.finish();
	      } else {
	    	  getSupportFragmentManager().popBackStack();
	      }
	  }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == Activity.RESULT_OK) {
	           
	        	if(mContent instanceof FilesFragment){
	        		String mediaGUID = ((FilesFragment) mContent).mediaGUID;
	        		Folder parentFolder = ((FilesFragment) mContent).folder;
	        		
	        		SQLiteHelper.getInstance(this);
	        		DBAFileSystem dbaFileSystem = SQLiteHelper.getDBAFileSystem();
	        		
	        		SharedPreferences prefs = this.getApplicationContext().getSharedPreferences("com.theteachermate.app", Context.MODE_PRIVATE);
	        		int userID = prefs.getInt("userID", 0);
	        		
	        		File file = new File();
	        		
	        		file.bin = false;
	        		file.dateCreated = new Date();
	        		file.dateModified = new Date();
	        		file.fileDescription = "";
	        		file.mobileFolderID = parentFolder.mobileID;
	        		file.fileSizeBytes = 0;
	        		file.name = "New photo";
	        		file.realName = mediaGUID;
	        		file.MIMEType = "image/*";
	        		
	        		dbaFileSystem.createFile(file, userID);
	        	}
	        } else if (resultCode == Activity.RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    } else if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == Activity.RESULT_OK) {
	        	if(mContent instanceof FilesFragment){
	        		String mediaGUID = ((FilesFragment) mContent).mediaGUID;
	        		Folder parentFolder = ((FilesFragment) mContent).folder;
	        		
	        		SQLiteHelper.getInstance(this);
	        		DBAFileSystem dbaFileSystem = SQLiteHelper.getDBAFileSystem();
	        		
	        		SharedPreferences prefs = this.getApplicationContext().getSharedPreferences("com.theteachermate.app", Context.MODE_PRIVATE);
	        		int userID = prefs.getInt("userID", 0);
	        		
	        		File file = new File();
	        		
	        		file.bin = false;
	        		file.dateCreated = new Date();
	        		file.dateModified = new Date();
	        		file.fileDescription = "";
	        		file.mobileFolderID = parentFolder.mobileID;
	        		file.fileSizeBytes = 0;
	        		file.name = "New video";
	        		file.realName = mediaGUID;
	        		file.MIMEType = "video/mp4";
	        		
	        		dbaFileSystem.createFile(file, userID);
	        	}
	        } else if (resultCode == Activity.RESULT_CANCELED) {
	            // User cancelled the video capture
	        } else {
	            // Video capture failed, advise user
	        }
	    } else if (requestCode == IMPORT_GALLERY_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == Activity.RESULT_OK) {
	        	if(mContent instanceof FilesFragment){
	        		String mediaGUID = ((FilesFragment) mContent).mediaGUID;
	        		Folder parentFolder = ((FilesFragment) mContent).folder;
	        		
	        		SQLiteHelper.getInstance(this);
	        		DBAFileSystem dbaFileSystem = SQLiteHelper.getDBAFileSystem();
	        		
	        		SharedPreferences prefs = this.getApplicationContext().getSharedPreferences("com.theteachermate.app", Context.MODE_PRIVATE);
	        		int userID = prefs.getInt("userID", 0);
	        		
	        		File file = new File();
	        		
	        		file.bin = false;
	        		file.dateCreated = new Date();
	        		file.dateModified = new Date();
	        		file.fileDescription = "";
	        		file.mobileFolderID = parentFolder.mobileID;
	        		file.fileSizeBytes = 0;
	        		file.name = "New audio";
	        		file.realName = mediaGUID;
	        		file.MIMEType = "audio/*";
	        		
	        		dbaFileSystem.createFile(file, userID);
	        	}
	        } else if (resultCode == Activity.RESULT_CANCELED) {
	            // User cancelled the video capture
	        } else {
	            // Video capture failed, advise user
	        }
	    }
	}
	
	/*
	@Override
	public void onBackPressed() 
	{			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	 
				// set title
				alertDialogBuilder.setTitle("Log Out");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Do you want to log out?")
					.setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) 
						{
							finish();
						}
					  })
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) 
						{
							dialog.cancel();
						}
					});
	 
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();	
		}
	}
	*/
}
