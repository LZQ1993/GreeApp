package com.greeapp.WaitWorkOrder.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.greeapp.MainActivity;
import com.greeapp.R;
import com.greeapp.Infrastructure.CWActivity.SingleFragmentActivity;
import com.greeapp.Infrastructure.CWFragment.IFragmentCallback;
import com.greeapp.WaitWorkOrder.Fragment.WaitWorkOrderListFragment;

public class WaitWorkOrderListActivity extends SingleFragmentActivity implements
		IFragmentCallback {

	@Override
	protected Fragment createFragment() {

		// 添加第三个时，临时写入一些数据 --结束
		WaitWorkOrderListFragment _ListFragment = new WaitWorkOrderListFragment();
		_ListFragment.setListViewId(R.layout.waitworkorder_item);
		_ListFragment.setTargetActivity(WaitWorkOrderItemDetailActivity.class);

		return _ListFragment;

	}

	@Override
	public void onActivityCallback(String fragmentTag) {

	}

	@Override
	public void onBackPressed() {
		Intent intent=new Intent(this, MainActivity.class);
		setResult(3, intent);
		finish();	
	}

}
