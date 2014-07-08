package com.theteachermate.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import interfaces.NewFolderInterface;

import classes.File;
import classes.Folder;
import classes.Observation;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import data.DBAFileSystem;
import data.DBAUser;
import data.SQLiteHelper;
import dialogs.NewFileDialog;
import dialogs.NewFolderDialog;
import dialogs.ppDatePickerDialog;
import adapters.ChildArrayAdapter;
import adapters.ChildCursorAdapter;
import adapters.FolderArrayAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FilesFragment extends SherlockFragment implements NewFolderInterface
{
	private DBAFileSystem dbaFileSystem;
	private ListView listviewFiles;
	private int userID;
	public Folder folder = new Folder();
	ArrayList<Folder> folders;
	ArrayList<File> files;
	
	public String mediaGUID;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		if (savedInstanceState != null) {
			userID = savedInstanceState.getInt("userID", 0);
			mediaGUID = savedInstanceState.getString("mediaGUID", "");
	    	folder = (Folder) savedInstanceState.getSerializable("FOLDER");
	    	folders = (ArrayList<Folder>) savedInstanceState.getSerializable("FOLDERS");
	    	files = (ArrayList<File>) savedInstanceState.getSerializable("FILES");
		}
			
		SQLiteHelper.getInstance(this.getActivity());
		SharedPreferences prefs = this.getActivity().getApplicationContext().getSharedPreferences("com.theteachermate.app", Context.MODE_PRIVATE);
		
		userID = prefs.getInt("userID", 0);
		dbaFileSystem = SQLiteHelper.getDBAFileSystem();
		
		((BaseActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.folder_icon);
		
		
		if(folder.mobileID != 0){
			((BaseActivity) getActivity()).getSupportActionBar().setTitle(folder.name);
			((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}else{
			((BaseActivity) getActivity()).getSupportActionBar().setTitle("Files");	
			((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		}
		
		return inflater.inflate(R.layout.files_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    refreshFileSystem();
		setHasOptionsMenu(true);
	}
	
	private void loadFolder(Folder folder) {
		FilesFragment fragment = new FilesFragment();
		fragment.folder = folder;
		
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof FragmentChangeActivity) {
			FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
			
			fca.pushFragment(fragment);
		}
	}
	
	private void loadFile(File file) {
		FileFragment fragment = new FileFragment();
		fragment.file = file;
		
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof FragmentChangeActivity) {
			FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
			
			fca.pushFragment(fragment);
		}
	}
	
	private void refreshFileSystem()
	{
	 	listviewFiles = (ListView) getView().findViewById(R.id.files_list_view);
		
	    folders = dbaFileSystem.getFolders(folder.mobileID, userID);
	    files = dbaFileSystem.getFiles(folder.mobileID, userID);
	    
		FolderArrayAdapter folderArrayAdapter = new FolderArrayAdapter(getActivity(), R.layout.listview_child_row, folders, files);
				
		listviewFiles.setAdapter(folderArrayAdapter); 
		listviewFiles.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) 
	        {
	        	if(position >= folders.size()){
	        		File file = files.get(position-folders.size());
	        		loadFile(file);
	        	}else{
		        	Folder folder = folders.get(position);
		        	Log.i("pp", "Selected folder:" + folder.mobileID);
		        	loadFolder(folder);
	        	}
            }
        });
	}
	
	private void newFolderDialog(){
		FragmentManager fm = getChildFragmentManager();
		NewFolderDialog newFolderDialog = new NewFolderDialog();
		newFolderDialog.callback = this;
		newFolderDialog.show(fm, "new_folder_dialog");
	}
	
	private void newFileDialog(){
		mediaGUID = UUID.randomUUID().toString();
		
		FragmentManager fm = getChildFragmentManager();
		NewFileDialog newileDialog = new NewFileDialog();
		newileDialog.mediaGUID = mediaGUID;
		newileDialog.show(fm, "new_file_dialog");
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("FOLDER", folder);
		outState.putInt("userID", userID);
		outState.putSerializable("FOLDERS", folders);
		outState.putSerializable("FILES", files);
		outState.putString("mediaGUID", mediaGUID);
	}
	
	//@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
	    inflater.inflate(R.menu.files_menu, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) 
		{
		case R.id.action_create_file:
			newFileDialog();
			return true;
		
		case R.id.action_create_folder:
			newFolderDialog();
			return true;
			
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void folderNameSelected(String folderName) {
				
		Folder folder = new Folder();
		
		folder.mobileParentID = this.folder.mobileID;
		folder.dateCreated = new Date();
		folder.dateModified = new Date();
		folder.name = folderName;
		folder.bin = false;
		
		dbaFileSystem.createFolder(folder, userID);
		
		refreshFileSystem();
	}
}
