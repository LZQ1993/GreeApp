package com.greeapp.Infrastructure.CWActivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.greeapp.R;

public abstract class SingleFragmentActivity extends FragmentActivity {

	private int fragmentContainer=R.layout.fragment_navigator_content_layout;
	private int contentContainer=R.id.id_content;
	

	public int getFragmentContainer() {
		return fragmentContainer;
	}
	public int getContentContainer() {
		return contentContainer;
	}

	public void setFragmentContainer(int fragmentContainer) {
		this.fragmentContainer = fragmentContainer;
	}
	
	public void setContentContainer(int contentContainer) {
		this.contentContainer = contentContainer;
	}

	protected abstract Fragment createFragment();//子类将重写该方法

	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(getFragmentContainer()); 
		FragmentManager _fragmentManager=getSupportFragmentManager();
		Fragment _fragment=_fragmentManager.findFragmentById(getContentContainer());
		if(_fragment==null){
			_fragment=createFragment();
			_fragmentManager.beginTransaction()
			.add(R.id.id_content, _fragment)
			.commit();
		}
	}
	
}
