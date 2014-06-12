package com.theteachermate.app;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.theteachermate.app.observation.ObservationActivity;


import data.SQLiteHelper;
import adapters.ChildCursorAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ObservationsFragment extends SherlockFragment {

	private int userID;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		SQLiteHelper.getInstance(this.getActivity());
		
		SharedPreferences prefs = this.getActivity().getApplicationContext().getSharedPreferences("com.theteachermate.app", Context.MODE_PRIVATE);
		
		userID = prefs.getInt("userID", 0);
		//dbaUser = SQLiteHelper.getDBAUser();
		
		((BaseActivity) getActivity()).getSupportActionBar().setTitle("Observations");

		return inflater.inflate(R.layout.observations_fragment, container, false);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		((BaseActivity) getActivity()).getSupportMenuInflater().inflate(R.menu.fragment_itemdetail, menu);
		return true;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

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
		case R.id.action_create_observation:
			Intent myIntent = new Intent(this.getActivity(), ObservationActivity.class);
			myIntent.putExtra("mobileAssessmentID", 0); //Optional parameters
			this.getActivity().startActivity(myIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
