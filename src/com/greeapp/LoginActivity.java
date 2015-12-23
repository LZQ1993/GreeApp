package com.greeapp;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;

import com.greeapp.Fragment.LoginFragment;
import com.greeapp.Infrastructure.CWActivity.SingleFragmentActivity;

public class LoginActivity extends SingleFragmentActivity {
	private long firstBackKeyTime = 0;
	@Override
	protected Fragment createFragment() {		
		return new LoginFragment();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)// 主要是对这个函数的复写
	{
		// TODO Auto-generated method stub

		if ((keyCode == KeyEvent.KEYCODE_BACK)
				&& (event.getAction() == KeyEvent.ACTION_DOWN)) {
			if (System.currentTimeMillis() - firstBackKeyTime > 2000) // 2s内再次选择back键有效
			{
				Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
				firstBackKeyTime = System.currentTimeMillis();
			} else {			
				finish();
				System.exit(0); // 凡是非零都表示异常退出!0表示正常退出!
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
