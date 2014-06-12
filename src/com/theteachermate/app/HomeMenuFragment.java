package com.theteachermate.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class HomeMenuFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		//if (savedInstanceState != null)
		//	mColorRes = savedInstanceState.getInt("mColorRes");


		return inflater.inflate(R.layout.activity_main, container, false);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//outState.putInt("mColorRes", mColorRes);
	}
}
