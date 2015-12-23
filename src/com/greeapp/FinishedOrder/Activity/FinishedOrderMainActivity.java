package com.greeapp.FinishedOrder.Activity;

import android.support.v4.app.Fragment;

import com.greeapp.FinishedOrder.Fragment.FinishedOrderMainFragment;
import com.greeapp.Infrastructure.CWActivity.SingleFragmentActivity;

public class FinishedOrderMainActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		return new FinishedOrderMainFragment();
	}

}
