package com.greeapp.Fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greeapp.R;
import com.greeapp.Entity.LocalWorkState;
import com.greeapp.Entity.UserMessage;
import com.greeapp.Entity.WorkState;
import com.greeapp.FinishedOrder.Activity.FinishedOrderMainActivity;
import com.greeapp.InfoQuery.Activity.InfoQueryActivity;
import com.greeapp.Infrastructure.CWDataDecoder.DataResult;
import com.greeapp.Infrastructure.CWDataDecoder.JsonDecode;
import com.greeapp.Infrastructure.CWDataRequest.RequestUtility;
import com.greeapp.Infrastructure.CWFragment.DataRequestFragment;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;
import com.greeapp.WaitAcceptOrder.Activity.WaitAcceptOrderListActivity;
import com.greeapp.WaitWorkOrder.Activity.WaitWorkOrderListActivity;

public class MainFragment extends DataRequestFragment implements OnClickListener {
	LinearLayout ll_infoQuery,ll_finishedOrder,ll_waitAcceptOrder,ll_waitWorkOrder;
	private Context mContext;
	private String currentNotiName="WorkStateNotifications";
	private String UserName;
	private TextView tv_UserRealName,tv_WebName,tv_WorkWaitedNumber,tv_WorkUnderProcNumber;
	ProgressDialog progressDialog;
	Button btn_refresh;
	private String IsShow;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		IsShow = getActivity().getIntent().getStringExtra("IsShow");
		Notifications.add(currentNotiName);
		ISqlHelper iSqlHelper=new SqliteHelper(null,mContext);
		List<Object> list=iSqlHelper.Query("com.greeapp.Entity.UserMessage", null);
		UserMessage userMessage=(UserMessage) list.get(0);
		UserName = userMessage.UserName;		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_main,container, false);	
		tv_UserRealName = (TextView) view.findViewById(R.id.tv_UserRealName);
		tv_WebName = (TextView) view.findViewById(R.id.tv_WebName);
		tv_WorkWaitedNumber = (TextView) view.findViewById(R.id.tv_WorkWaitedNumber);
		tv_WorkUnderProcNumber = (TextView) view.findViewById(R.id.tv_WorkUnderProcNumber);	
		ll_infoQuery = (LinearLayout) view.findViewById(R.id.ll_infoQuery);
		ll_infoQuery.setOnClickListener((OnClickListener)this);
		ll_finishedOrder = (LinearLayout) view.findViewById(R.id.ll_finishedOrder);
		ll_finishedOrder.setOnClickListener((OnClickListener)this);
		ll_waitAcceptOrder = (LinearLayout) view.findViewById(R.id.ll_waitAcceptOrder);
		ll_waitAcceptOrder.setOnClickListener((OnClickListener)this);
		ll_waitWorkOrder = (LinearLayout) view.findViewById(R.id.ll_waitWorkOrder);
		ll_waitWorkOrder.setOnClickListener((OnClickListener)this);
		btn_refresh = (Button) view.findViewById(R.id.btn_refresh);
		btn_refresh.setOnClickListener((OnClickListener)this);
		dismissProgressDialog();
	    showProgressDialog(getActivity(),"加载中...");
		initData();
		return view;
	}

	@Override
	public void onClick(View v) {
		if(v==btn_refresh){
			showProgressDialog(getActivity(),"加载中...");
			initData();
		}
		if(v==ll_infoQuery){
			Intent intent = new Intent();
			intent.setClass(mContext, InfoQueryActivity.class);
			startActivity(intent);
		}
		if(v==ll_finishedOrder){
			Intent intent = new Intent();
			intent.setClass(mContext, FinishedOrderMainActivity.class);
			startActivity(intent);
		}
		if(v==ll_waitAcceptOrder){
			Intent intent = new Intent();
			intent.setClass(mContext, WaitAcceptOrderListActivity.class);
			startActivityForResult(intent, 2);
		}
		if(v==ll_waitWorkOrder){
			Intent intent = new Intent();
			intent.setClass(mContext, WaitWorkOrderListActivity.class);
			startActivityForResult(intent, 3);
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==2||resultCode==3){
			showProgressDialog(getActivity(),"数据更新中...");
			initData();
		}
	}

	private void initData() {
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("WorkSystemService", "queryWorkState");
		Map requestCondition = new HashMap();
		String condition[] = { "UserName"};		
		String value[] = {UserName};
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName);
		setRequestUtility(myru);
		requestData();		
	}

	@Override
	public void updateView() {
		dismissProgressDialog();
		if (result != null) {
			dataResult=dataDecode.decode(result,"WorkState");
			if (dataResult != null) {
				DataResult realData = (DataResult) dataResult;
				if(CurrentAction==currentNotiName){
					if (realData.getResultcode().equals("1")) {
						WorkState ws = (WorkState) realData.getResult().get(0);
						ISqlHelper iSqlHelper = new SqliteHelper(null,mContext);
						String sql = "update UserMessage set Name = '"+ws.StaffName+"',WebName = '"+ws.NetPointName+"' where UserName='"+UserName+"'";
						iSqlHelper.SQLExec(sql);
						LocalWorkState lws = new LocalWorkState();
						lws.AutoID=1;
						lws.NetPointName=ws.NetPointName;
						lws.StaffName = ws.StaffName;
						lws.WorkUnderProcNumber =ws.WorkUnderProcNumber;
						lws.WorkWaitedNumber =  ws.WorkWaitedNumber;		
						List<Object> ls = iSqlHelper.Query("com.greeapp.Entity.LocalWorkState","AutoID=1");
						if(ls.size()>0){
							String sql1 = "update LocalWorkState set StaffName = '"+ws.StaffName+"',NetPointName = '"+ws.NetPointName+"',WorkUnderProcNumber ='"+ws.WorkUnderProcNumber+"',WorkWaitedNumber='"+ws.WorkWaitedNumber+"' where AutoID=1";
							iSqlHelper.SQLExec(sql1);
						}else{
							iSqlHelper.Insert(lws);
						}
						handler.sendEmptyMessage(0);
					}else{
						handler.sendEmptyMessage(0);
						DefaultTip(getActivity(),"暂无数据");	
					}
				}
			}else{
				handler.sendEmptyMessage(0);
				DefaultTip(getActivity(),"网络数据获取失败");	
		   }
		}else{
			handler.sendEmptyMessage(0);
			DefaultTip(getActivity(),"网络数据获取失败");	
		}
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ISqlHelper iSqlHelper = new SqliteHelper(null,mContext);
			List<Object> ls = iSqlHelper.Query("com.greeapp.Entity.LocalWorkState","AutoID=1");
			if(ls.size()>0){
				LocalWorkState lws = (LocalWorkState) ls.get(0);
				tv_UserRealName.setText(lws.StaffName);
				tv_WebName.setText(lws.NetPointName);
				tv_WorkWaitedNumber.setText("("+lws.WorkWaitedNumber+")");
				tv_WorkUnderProcNumber.setText("("+lws.WorkUnderProcNumber+")");	
			}else{
				tv_UserRealName.setText("无");
				tv_WebName.setText("无");
				tv_WorkWaitedNumber.setText("(0)");
				tv_WorkUnderProcNumber.setText("(0)");
			}
				
		 }
	};
	
	@Override
	public void onResume() {
		super.onResume();
	}
}
