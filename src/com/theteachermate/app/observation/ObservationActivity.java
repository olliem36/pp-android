package com.theteachermate.app.observation;

import interfaces.DatePickerInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import classes.Observation;
import classes.User;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import com.theteachermate.app.ChildrenFragment;
import com.theteachermate.app.R;

import dialogs.ChildrenDialog;
import dialogs.ppDatePickerDialog;

import adapters.ChildArrayAdapter;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class ObservationActivity extends SherlockFragmentActivity implements DatePickerInterface
{	
	private Observation observation = new Observation();
	
	private EditText tfObservationTitle;
	private Button btnObservationDate;
	private Button btnAddChildren;
	private Button btnAddFiles;
	private ListView children_listview;
	private RelativeLayout layout_children_title;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);

	    if(savedInstanceState != null)
	    {
	    	this.observation = (Observation) savedInstanceState.getSerializable("OBSERVATION");
	    }
	    
	    setContentView(R.layout.observation_activity);
	    
	    tfObservationTitle = (EditText) findViewById(R.id.editText_observation_title);
		btnObservationDate = (Button) findViewById(R.id.btn_observation_date);
		btnAddChildren = (Button) findViewById(R.id.btn_add_children);
		btnAddFiles = (Button) findViewById(R.id.btn_add_file);
		children_listview = (ListView) findViewById(R.id.observation_children_list_view);
		layout_children_title = (RelativeLayout) findViewById(R.id.layout_children_title);				
		btnAddChildren.setOnClickListener(SelectChildrenClickListener);
		btnObservationDate.setOnClickListener(SelectDateClickListener);
		
		tfObservationTitle.setText(observation.title);
		
		displayChildren();
		renderDate();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		savedInstanceState.putSerializable("OBSERVATION", observation);
	    super.onSaveInstanceState(savedInstanceState);
	}

	private OnClickListener SelectDateClickListener = new OnClickListener() {
	    @Override
	    public void onClick(final View v) {
	    	 FragmentManager fm = getSupportFragmentManager();
	    	 ppDatePickerDialog ppdatePickerDialog = new ppDatePickerDialog();
	    	 ppdatePickerDialog.show(fm, "select_date_dialog");
	    }
	};
	
	private OnClickListener SelectChildrenClickListener = new OnClickListener() {
	    @Override
	    public void onClick(final View v) {
	    	 FragmentManager fm = getSupportFragmentManager();
	    	 ChildrenDialog selectChildrenDialog = new ChildrenDialog();
	    	 selectChildrenDialog.show(fm, "select_children_dialog");
	    }
	};
	
	public void childrenSelected(ArrayList<User> children)
	{
		observation.children = children;
		displayChildren();
	}
	
	private void displayChildren()
	{
		ChildArrayAdapter childArrayAdapter = new ChildArrayAdapter(this, R.layout.listview_child_row, observation.children);
		
		/*
		ArrayAdapter<User> arrayAdapter = new ArrayAdapter<User>(
                this, 
                android.R.layout.simple_list_item_1,
                observation.children );
		*/
		
		children_listview.setAdapter(childArrayAdapter); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
	    MenuInflater mInflater = getSupportMenuInflater();
	    mInflater.inflate(R.menu.observation_menu, menu);

	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_save_observation:
			
			Log.i("PP", "saving observation...");

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean onPrepareOptionsMenu(Menu menu){
		//code here
		return true;
	}

	@Override
	public void dateSelected(Date selectedDate, String tag) {
		observation.date = selectedDate;
		renderDate();
	}
	
	private void renderDate()
	{
		if(observation.date == null)
			observation.date = new Date();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		btnObservationDate.setText(sdf.format(observation.date));
	}
}
