package com.greeapp.WaitWorkOrder.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.greeapp.Infrastructure.CWActivity.SingleFragmentActivity;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;
import com.greeapp.WaitWorkOrder.Fragment.WaitWorkOrderInfoFragment;

public class WaitWorkOrderItemDetailActivity extends SingleFragmentActivity{
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		String WorkId = getIntent().getStringExtra(WaitWorkOrderInfoFragment.EXTRA_LIST_ITEM_ID);		
		int position = getIntent().getIntExtra("position", 0);	
		return WaitWorkOrderInfoFragment.newInstance(WorkId,position);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		ISqlHelper iSqlHelper = new SqliteHelper(null,this);
		iSqlHelper.SQLExec("delete from LocalCollectData");// 删除表中原有的数据，保证只有一条
		Intent intent = new Intent(this,
				WaitWorkOrderListActivity.class);
		setResult(0, intent);
		finish();
	}
	
	
}
