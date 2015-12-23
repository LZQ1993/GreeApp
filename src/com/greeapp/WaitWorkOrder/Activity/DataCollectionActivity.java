package com.greeapp.WaitWorkOrder.Activity;

import android.support.v4.app.Fragment;

import com.greeapp.Infrastructure.CWActivity.SingleFragmentActivity;
import com.greeapp.WaitWorkOrder.Fragment.DataCollectionFragment;

public class DataCollectionActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		return new DataCollectionFragment();
	}
}