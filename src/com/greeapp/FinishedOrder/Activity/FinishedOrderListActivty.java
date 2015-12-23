package com.greeapp.FinishedOrder.Activity;

import android.support.v4.app.Fragment;

import com.greeapp.R;
import com.greeapp.Entity.FinishedOrder;
import com.greeapp.FinishedOrder.Domain.FinishedOrderListLab;
import com.greeapp.FinishedOrder.Fragment.FinishOrderListFragment;
import com.greeapp.Infrastructure.CWActivity.SingleFragmentActivity;
import com.greeapp.Infrastructure.CWDataDecoder.DataDecode;
import com.greeapp.Infrastructure.CWDataDecoder.DataResult;
import com.greeapp.Infrastructure.CWDataDecoder.IDataDecode;
import com.greeapp.Infrastructure.CWFragment.IFragmentCallback;

public class FinishedOrderListActivty extends SingleFragmentActivity implements IFragmentCallback{
 
	@Override
	protected Fragment createFragment() {
		String str = getIntent().getStringExtra("OrderList");
		IDataDecode dataDecode= new DataDecode();	
		DataResult realData =(DataResult) dataDecode.decode(str,"FinishedOrder");
		  FinishedOrderListLab fol = FinishedOrderListLab.get(getApplicationContext());
	      for(int i=0;i<realData.getResult().size();i++){
	    	  FinishedOrder orderInfo = new FinishedOrder();
	    	  orderInfo = (FinishedOrder) realData.getResult().get(i);
	    	  fol.add(orderInfo);
	      }
	    

	    //添加第三个时，临时写入一些数据 --结束
	      FinishOrderListFragment _ListFragment=new FinishOrderListFragment();
	      //_ListFragment.setListItemes(null);
	      _ListFragment.setListItemes(fol.getListItemes());
	      _ListFragment.setListViewId(R.layout.finishedorderlist_item);
	      _ListFragment.setTargetActivity(FinishedOrderItemDetailActivity.class);

			return _ListFragment;
		
	}

	@Override
	public void onActivityCallback(String fragmentTag) {
	
		
	}
}
