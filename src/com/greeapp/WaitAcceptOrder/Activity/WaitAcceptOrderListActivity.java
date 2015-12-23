package com.greeapp.WaitAcceptOrder.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.greeapp.MainActivity;
import com.greeapp.R;
import com.greeapp.Infrastructure.CWActivity.SingleFragmentActivity;
import com.greeapp.Infrastructure.CWFragment.IFragmentCallback;
import com.greeapp.WaitAcceptOrder.Fragment.WaitAcceptOrderListFragment;

public class WaitAcceptOrderListActivity extends SingleFragmentActivity
		implements IFragmentCallback {

	@Override
	protected Fragment createFragment() {

		// 添加第三个时，临时写入一些数据 --结束
		WaitAcceptOrderListFragment _ListFragment = new WaitAcceptOrderListFragment();
		_ListFragment.setListViewId(R.layout.waitacceptorder_item);
		_ListFragment.setTargetActivity(WaitAcceptOrderItemDetailActivity.class);

		return _ListFragment;

	}

	@Override
	public void onActivityCallback(String fragmentTag) {

	}
	@Override 
	public void onBackPressed() { 
		Intent intent=new Intent(this, MainActivity.class);
		setResult(2, intent);
		finish();	
	} 
	
	
}
