package com.theteachermate.app;


import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import data.DBAUser;
import data.SQLiteHelper;
import adapters.ChildCursorAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ChildrenFragment extends SherlockFragment 
{
	
	private DBAUser dbaUser;
	private int userID;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		SQLiteHelper.getInstance(this.getActivity());
		
		SharedPreferences prefs = this.getActivity().getApplicationContext().getSharedPreferences("com.theteachermate.app", Context.MODE_PRIVATE);
		
		userID = prefs.getInt("userID", 0);
		dbaUser = SQLiteHelper.getDBAUser();
		
		((BaseActivity) getActivity()).getSupportActionBar().setTitle("Children");
				
		return inflater.inflate(R.layout.children_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
		Cursor children = dbaUser.getChildren(userID);
						
		ChildCursorAdapter childAdapter = new ChildCursorAdapter(this.getActivity(), children);
		
		ListView listView = (ListView) getView().findViewById(R.id.children_list_view);
		listView.setAdapter(childAdapter);	
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//outState.putInt("mColorRes", mColorRes);
	}
	
	//@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
	    inflater.inflate(R.menu.observations_menu, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
