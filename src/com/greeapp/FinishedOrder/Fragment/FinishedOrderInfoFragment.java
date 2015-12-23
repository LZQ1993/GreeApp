package com.greeapp.FinishedOrder.Fragment;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greeapp.R;
import com.greeapp.Entity.OrderDetail;
import com.greeapp.Entity.ProductList;
import com.greeapp.Infrastructure.CWDataDecoder.DataResult;
import com.greeapp.Infrastructure.CWDataDecoder.JsonDecode;
import com.greeapp.Infrastructure.CWDataRequest.RequestUtility;
import com.greeapp.Infrastructure.CWFragment.DataRequestFragment;

public class FinishedOrderInfoFragment extends DataRequestFragment implements
		OnClickListener {
	public static final String EXTRA_LIST_ITEM_ID = "com.chinawit.android.listitemId";
	private Context mContext;
	private String currentNotiName = "QueryFinishedOrderInfoNotifications";
	private ImageButton nav_bar_btn_left;
	private String WorkId;
	TextView tv_workId, tv_orderType, tv_appointTime, tv_workTime, tv_remark,
			tv_clientName, tv_tel1, tv_tel2, tv_installAddress, tv_SaleNum,
			tv_buyTime, tv_unit, tv_productType, tv_productNum;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		WorkId = getActivity().getIntent().getStringExtra(
				FinishedOrderInfoFragment.EXTRA_LIST_ITEM_ID);
		Notifications.add(currentNotiName);

	}

	public static FinishedOrderInfoFragment newInstance(String WorkId) {
		Bundle args = new Bundle();
		args.putString(EXTRA_LIST_ITEM_ID, WorkId);
		FinishedOrderInfoFragment _fragment = new FinishedOrderInfoFragment();
		_fragment.setArguments(args);
		return _fragment;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_finishedorderinfo, container, false);
		nav_bar_btn_left = (ImageButton) view.findViewById(R.id.foinfo_nav_bar_btn_left);
		nav_bar_btn_left.setOnClickListener(this);
		tv_workId = (TextView) view.findViewById(R.id.tv_workId);
        tv_orderType = (TextView) view.findViewById(R.id.tv_orderType);
        tv_appointTime = (TextView) view.findViewById(R.id.tv_appointTime);
        tv_workTime = (TextView) view.findViewById(R.id.tv_workTime);
        tv_remark = (TextView) view.findViewById(R.id.tv_remark);
        tv_clientName = (TextView) view.findViewById(R.id.tv_clientName);
        tv_tel1 = (TextView) view.findViewById(R.id.tv_tel1);
        tv_tel1.setOnClickListener(this);
        tv_tel2 = (TextView) view.findViewById(R.id.tv_tel2);
        tv_tel2.setOnClickListener(this);
        tv_installAddress = (TextView) view.findViewById(R.id.tv_installAddress);
        tv_SaleNum = (TextView) view.findViewById(R.id.tv_SaleNum);
        tv_buyTime = (TextView) view.findViewById(R.id.tv_buyTime);
        tv_unit = (TextView) view.findViewById(R.id.tv_unit);
        tv_productType = (TextView) view.findViewById(R.id.tv_productType);
        tv_productNum = (TextView) view.findViewById(R.id.tv_productNum);
		initData();
		return view;
	}

	private void initData() {
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("WorkSystemService", "queryWorkDetail");
		Map requestCondition = new HashMap();
		String condition[] = { "WorkID" };
		String value[] = { WorkId };
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName);
		setRequestUtility(myru);
		requestData();
		showProgressDialog(getActivity(), "载入中，请稍候...");
	}

	@Override
	public void onClick(View v) {
		if (v == nav_bar_btn_left) {
			// 加载动画
			Animation animation = AnimationUtils.loadAnimation(mContext,
					R.anim.back_anim);
			nav_bar_btn_left.setAnimation(animation);
			getActivity().finish();
		}
        if(v==tv_tel1){
        	if(tv_tel1.getText().toString().equals("")||tv_tel1.getText().toString().equals("无")){
				DefaultTip(getActivity(), "此号码无效");
			}else{
				Intent _intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv_tel1.getText().toString()));
				startActivity(_intent);
			}	
		}
		if(v==tv_tel2){
			if(tv_tel2.getText().toString().equals("")||tv_tel2.getText().toString().equals("无")){
				DefaultTip(getActivity(), "此号码无效");
			}else{
				Intent _intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv_tel2.getText().toString()));
				startActivity(_intent);
			}	
		}
	}

	@Override
	public void updateView() {
		dismissProgressDialog();
		if (result != null) {
			dataResult=dataDecode.decode(result,"OrderDetail");
			if (dataResult != null) {
				DataResult realData = (DataResult) dataResult;
				if(CurrentAction==currentNotiName){
					if (realData.getResultcode().equals("1")) {
						OrderDetail od = (OrderDetail)realData.getResult().get(0);						
						Message message = new Message();
						message.obj = od;
						handler.sendMessage(message);
					}else{
						DefaultTip(getActivity(),"暂无数据");	
					 }
				}
			}else{
				DefaultTip(getActivity(),"网络数据获取失败");	
		   }
		}else{
			DefaultTip(getActivity(),"网络数据获取失败");	
		}
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			OrderDetail orderDetial = (OrderDetail) msg.obj;
			tv_workId.setText(orderDetial.WorkID);
			tv_orderType.setText(orderDetial.WorkType);
			tv_appointTime.setText(orderDetial.AppointTime);
			tv_workTime.setText(orderDetial.AssignTime);
			tv_remark.setText(orderDetial.Remark);
			tv_clientName.setText(orderDetial.ClientName);
			tv_tel1.setText(orderDetial.ClientTel1);
			tv_tel2.setText(orderDetial.ClientTel2);
			tv_installAddress.setText(orderDetial.InstallAddress);
			tv_SaleNum.setText(orderDetial.SaleOrder);
			tv_buyTime.setText(orderDetial.BuyTime);
			tv_unit.setText(orderDetial.SaleDepartment);
			String str_type = "";
			String str_num  = "";
			for(int i = 0;i<orderDetial.ProductList.size();i++){
				ProductList pl = new ProductList();
				pl=(ProductList) orderDetial.ProductList.get(i);
				if(i==orderDetial.ProductList.size()-1){
					str_type =  str_type+pl.ProductModel;
					str_num =  str_num + pl.ProductNumber;
				}else{
					str_type = str_type + pl.ProductModel+"\n";
					str_num =  str_num + pl.ProductNumber+"\n";
				}
			}
			tv_productType.setText(str_type);
			tv_productNum.setText(str_num);
		
		}
	};
}
