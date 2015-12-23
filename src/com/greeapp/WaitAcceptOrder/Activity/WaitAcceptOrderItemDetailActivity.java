package com.greeapp.WaitAcceptOrder.Activity;

import android.support.v4.app.Fragment;

import com.greeapp.Infrastructure.CWActivity.SingleFragmentActivity;
import com.greeapp.WaitAcceptOrder.Fragment.WaitAcceptOrderInfoFragment;

public class WaitAcceptOrderItemDetailActivity extends SingleFragmentActivity{
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		String WorkId = getIntent().getStringExtra(WaitAcceptOrderInfoFragment.EXTRA_LIST_ITEM_ID);		
		int position = getIntent().getIntExtra("position", 0);	
		return WaitAcceptOrderInfoFragment.newInstance(WorkId,position);

	}
}
