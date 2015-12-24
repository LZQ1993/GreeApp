package com.greeapp.UserCenter.Activity;


import android.content.Intent;
import android.support.v4.app.Fragment;

import com.greeapp.Infrastructure.CWActivity.SingleFragmentActivity;
import com.greeapp.UserCenter.Fragment.PasswordChangeFragment;
import com.greeapp.UserCenter.Fragment.UserInfoMangerFragment;
public class UserCenterActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		Fragment _fragment=null;
		Intent _intent=null;
		try {
			_intent=getIntent();
		} catch (Exception e) {			
			e.printStackTrace();
		}
		if(_intent.getStringExtra("Key").equals("UserInfoManger")){		
			_fragment=new UserInfoMangerFragment();
			
		}
		else if(_intent.getStringExtra("Key").equals("PasswordChange")){ 
			_fragment=new PasswordChangeFragment();
		}
		return _fragment;
	}

	
}