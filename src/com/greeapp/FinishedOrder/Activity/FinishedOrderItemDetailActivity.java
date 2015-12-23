package com.greeapp.FinishedOrder.Activity;

import android.support.v4.app.Fragment;

import com.greeapp.FinishedOrder.Fragment.FinishedOrderInfoFragment;
import com.greeapp.Infrastructure.CWActivity.SingleFragmentActivity;

public class FinishedOrderItemDetailActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		String WorkId = getIntent().getStringExtra(FinishedOrderInfoFragment.EXTRA_LIST_ITEM_ID);		
		return FinishedOrderInfoFragment.newInstance(WorkId);
	}
}
