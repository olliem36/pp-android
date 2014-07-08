package dialogs;

import interfaces.NewFolderInterface;

import com.theteachermate.app.R;

import data.SQLiteHelper;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class NewFolderDialog extends DialogFragment {

	private EditText etFolderName;
	private Button btnAddFolder;
	public NewFolderInterface callback;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
        View view = inflater.inflate(R.layout.dialog_new_folder, container);
        
        etFolderName = (EditText) view.findViewById(R.id.etFolderName);
	    btnAddFolder = (Button) view.findViewById(R.id.btnAddFolder);
		
        getDialog().setTitle("New Folder");
        
        //SQLiteHelper.getInstance(this.getActivity());
        		
		SharedPreferences prefs = this.getActivity().getApplicationContext().getSharedPreferences("com.theteachermate.app", Context.MODE_PRIVATE);
		
		//userID = prefs.getInt("userID", 0);
		//dbaUser = SQLiteHelper.getDBAUser();
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		btnAddFolder.setOnClickListener(BtnNewFolderClickListener);
				
		etFolderName.requestFocus();
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(etFolderName, InputMethodManager.SHOW_IMPLICIT);
		
        return view;
    }
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(etFolderName, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	private OnClickListener BtnNewFolderClickListener = new OnClickListener() {
	    @Override
	    public void onClick(final View v) {
	    	 
	    	String folderName = etFolderName.getText().toString().trim();
	    	
	    	if(folderName.length() > 0)
	    	{
	    		if(callback != null)
	    		{
	    			callback.folderNameSelected(folderName);
			    	getDialog().dismiss();
	    		}
	    		else{
	    			Log.i("PP", "callback is null");
	    		}
	    	}
	    	else{
	    		etFolderName.requestFocus();
	    		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	    		imm.showSoftInput(etFolderName, InputMethodManager.SHOW_IMPLICIT);
	    	}
	    }
	};
	
}
