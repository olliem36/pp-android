package com.theteachermate.app;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import classes.File;
import classes.Folder;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import data.DBAFileSystem;
import data.SQLiteHelper;

public class FileFragment extends SherlockFragment {
	private DBAFileSystem dbaFileSystem;
	public File file;
	private int userID;
	
	private Button btnPreviewFile;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		if (savedInstanceState != null) {
			userID = savedInstanceState.getInt("userID", 0);
	    	file = (File) savedInstanceState.getSerializable("FILE");
		}
				
		SQLiteHelper.getInstance(this.getActivity());
		SharedPreferences prefs = this.getActivity().getApplicationContext().getSharedPreferences("com.theteachermate.app", Context.MODE_PRIVATE);
		
		userID = prefs.getInt("userID", 0);
		dbaFileSystem = SQLiteHelper.getDBAFileSystem();
		
		((BaseActivity) getActivity()).getSupportActionBar().setTitle("Preview");
		((BaseActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.file_icon);
		
		return inflater.inflate(R.layout.preview_file_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
		btnPreviewFile = (Button) getView().findViewById(R.id.btnPreviewFile);
		btnPreviewFile.setOnClickListener(BtnPreviewFileClickListener);

	    setHasOptionsMenu(true);
	}
	
	
	private OnClickListener BtnPreviewFileClickListener = new OnClickListener() {
	    @Override
	    public void onClick(final View v) {
	    	previewFile();
	    }
	};
	
	private void previewFile(){
		Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(file.getFileUri(), file.MIMEType);
        getActivity().startActivity(intent); 
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("FILE", file);
		outState.putInt("userID", userID);
	}
	
	//@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
	    inflater.inflate(R.menu.file_preview_menu, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) 
		{
		case R.id.action_delete_file:
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			        	dbaFileSystem.deleteFile(file);
			        	if (getActivity() instanceof FragmentChangeActivity) {
			    			FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
			    			fca.getSupportFragmentManager().popBackStack();
			    		}
			            break;
			        case DialogInterface.BUTTON_NEGATIVE:
			            break;
			        }
			    }
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Do you want to delete this file?").setPositiveButton("Delete", dialogClickListener)
			    .setNegativeButton("No", dialogClickListener).show();

			return true;
		
		case R.id.action_upload_file:
			if (getActivity() instanceof FragmentChangeActivity) {
				FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
				fca.getSupportFragmentManager().popBackStack();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
