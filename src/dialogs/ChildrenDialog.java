package dialogs;

import java.util.ArrayList;
import java.util.Date;

import classes.User;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.theteachermate.app.R;
import com.theteachermate.app.observation.ObservationActivity;


import data.DBAUser;
import data.SQLiteHelper;

import adapters.ChildCursorAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ChildrenDialog extends DialogFragment {

	private DBAUser dbaUser;
	private int userID;
	private Button btn_select_children;
	private Cursor children;
	private ListView childrenListView;
	
	public ChildrenDialog(){
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
        View view = inflater.inflate(R.layout.children_fragment, container);

        getDialog().setTitle("Select Children");
        
        SQLiteHelper.getInstance(this.getActivity());
		
		SharedPreferences prefs = this.getActivity().getApplicationContext().getSharedPreferences("com.theteachermate.app", Context.MODE_PRIVATE);
		
		userID = prefs.getInt("userID", 0);
		dbaUser = SQLiteHelper.getDBAUser();
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
        return view;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
		children = dbaUser.getChildren(userID);
						
		ChildCursorAdapter childAdapter = new ChildCursorAdapter(this.getActivity(),children);
		
		childrenListView = (ListView) getView().findViewById(R.id.children_list_view);
		childrenListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		childrenListView.setAdapter(childAdapter);	

		btn_select_children = (Button) getView().findViewById(R.id.btn_select_children);
		btn_select_children.setOnClickListener(btnDoneClieckListener);
	}
	
	private OnClickListener btnDoneClieckListener = new OnClickListener() {
	    @Override
	    public void onClick(final View v) 
	    {	    	
	    	SparseBooleanArray sparseBooleanArray = childrenListView.getCheckedItemPositions();

	    	ArrayList<User> selectedChidren = new ArrayList<User>();
	    	
	    	children.moveToFirst();
	    	int i = 0;
	    	while (children.isAfterLast() == false) 
	    	{
	    		boolean checked = sparseBooleanArray.get(i);
	    		if(checked)
	    		{	
	    			int onlineUserID = children.getInt(children.getColumnIndex("onlineUserID"));
	    			int mobileUserID = children.getInt(children.getColumnIndex("onlineUserID"));
	    			String firstname = children.getString(children.getColumnIndex("firstname"));
	    			String surname = children.getString(children.getColumnIndex("surname"));
	    			String dob = children.getString(children.getColumnIndex("dob"));
	    			int userTypeID = children.getInt(children.getColumnIndex("userTypeID"));
	    			int ethnicityID = children.getInt(children.getColumnIndex("ethnicityID"));
	    			String email = children.getString(children.getColumnIndex("email"));
	    			String username = children.getString(children.getColumnIndex("username"));
	    			
	    			Date _dob = SQLiteHelper.parseDateString(dob);
	    			
	    			User child = new User(onlineUserID, mobileUserID, firstname, surname, email, username, ethnicityID, userTypeID, _dob);

	    			selectedChidren.add(child);
	    		}
	    	    i++;
	    	    children.moveToNext();
	    	}
	    	
	    	Log.i("pp", "Found: " + selectedChidren.size() + " selected children");
	    	
	    	children.close();
	    	
	    	((ObservationActivity)getActivity()).childrenSelected(selectedChidren);
	    	
	    	getDialog().cancel();
	    }
	};
	
	//@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
	    inflater.inflate(R.menu.observations_menu, menu);
	    onCreateOptionsMenu(menu,inflater);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return true;
		}
		return onOptionsItemSelected(item);
	}
}
