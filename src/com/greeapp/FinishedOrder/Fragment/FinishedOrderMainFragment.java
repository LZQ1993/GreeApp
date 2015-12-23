package com.greeapp.FinishedOrder.Fragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.greeapp.R;
import com.greeapp.Entity.UserMessage;
import com.greeapp.FinishedOrder.Activity.FinishedOrderListActivty;
import com.greeapp.Infrastructure.CWDataDecoder.DataResult;
import com.greeapp.Infrastructure.CWDataDecoder.JsonDecode;
import com.greeapp.Infrastructure.CWDataRequest.RequestUtility;
import com.greeapp.Infrastructure.CWFragment.DataRequestFragment;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;

public class FinishedOrderMainFragment extends DataRequestFragment implements
		OnClickListener {
	private Context mContext;
	private String currentNotiName = "QueryFinishedOrderNotifications";
	private ImageButton nav_bar_btn_left;
	private Button btn_query;
	private EditText et_time, et_orderNum, et_UserName, et_tel, et_address;
	private Spinner spn_type;
	private EditText startTime, endTime;
	private String start = "", end = "", UserName;
	private String WorkType;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		Notifications.add(currentNotiName);
		ISqlHelper iSqlHelper = new SqliteHelper(null, mContext);
		List<Object> list = iSqlHelper.Query("com.greeapp.Entity.UserMessage",
				null);
		UserMessage userMessage = (UserMessage) list.get(0);
		UserName = userMessage.UserName;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_finishedorder_main,
				container, false);
		nav_bar_btn_left = (ImageButton) view
				.findViewById(R.id.fo_nav_bar_btn_left);
		nav_bar_btn_left.setOnClickListener(this);
		btn_query = (Button) view.findViewById(R.id.btn_query);
		btn_query.setOnClickListener(this);

		et_time = (EditText) view.findViewById(R.id.et_time);
		et_time.setInputType(InputType.TYPE_NULL);
		et_orderNum = (EditText) view.findViewById(R.id.et_orderNum);
		et_UserName = (EditText) view.findViewById(R.id.et_UserName);
		et_tel = (EditText) view.findViewById(R.id.et_tel);
		et_address = (EditText) view.findViewById(R.id.et_address);
		spn_type = (Spinner) view.findViewById(R.id.spn_type);
		et_time.setOnClickListener(this);
		return view;
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
		if (v == btn_query) {
			QueryNetWork();
		}
		if (v == et_time) {
			showDialog();
		}
	}

	private void QueryNetWork() {

		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("WorkSystemService", "queryFinishedWorkList");
		Map requestCondition = new HashMap();
		String condition[] = { "UserName", "WorkType", "WorkID", "ClientName",
				"ClientTel", "ClientAddress", "StartTime", "EndTime" };

		if (spn_type.getSelectedItem().toString().equals("全部")) {
			WorkType = "";
		} else {
			WorkType = spn_type.getSelectedItem().toString();
		}
		String value[] = { UserName, WorkType,
				et_orderNum.getText().toString(),
				et_UserName.getText().toString(), et_tel.getText().toString(),
				et_address.getText().toString(), start, end };
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName);
		setRequestUtility(myru);
		requestData();
		showProgressDialog(getActivity(), "载入中，请稍候...");
	}

	public void updateView() {
		dismissProgressDialog();
		if (result != null) {
			if (CurrentAction == currentNotiName) {
				dataResult=dataDecode.decode(result,"FinishedOrder");
				if (dataResult != null) {
					DataResult realData = (DataResult) dataResult;
					if (realData.getResultcode().equals("1")) {
						Intent  intent = new Intent();	
						intent.putExtra("OrderList", result);
						intent.putExtra("UserName", UserName);
						intent.putExtra("WorkType", WorkType);
						intent.putExtra("WorkID",et_orderNum.getText().toString());
						intent.putExtra("ClientName",et_UserName.getText().toString());
						intent.putExtra("ClientTel",et_tel.getText().toString());
						intent.putExtra("ClientAddress",et_address.getText().toString());
						intent.putExtra("StartTime",start);
						intent.putExtra("EndTime",end);
						intent.setClass(getActivity(),FinishedOrderListActivty.class);
						startActivity(intent);
					}else{
						 DefaultTip(getActivity(),"暂无数据");	
					}
				}else{
					DefaultTip(getActivity(),"网络数据获取失败");		
				}
			}
		}else{
			DefaultTip(getActivity(),"网络数据获取失败");		
		}
	}

	private void showDialog() {
		View itemview = getActivity().getLayoutInflater().inflate(
				R.layout.showseacherdialog, null);
		startTime = (EditText) itemview.findViewById(R.id.pressureguardianship_start);
		endTime = (EditText) itemview
				.findViewById(R.id.pressureguardianship_end);
		startTime.setInputType(InputType.TYPE_NULL);
		endTime.setInputType(InputType.TYPE_NULL);
		startTime.setTag("");
		endTime.setTag("");
		startTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								int month = (monthOfYear + 1);
								String strdate = year + "-";
								if (month < 10) {
									strdate = strdate + "0" + month + "-";
								} else {
									strdate = strdate + month + "-";
								}
								if (dayOfMonth < 10) {
									strdate = strdate + "0" + dayOfMonth + " ";
								} else {
									strdate = strdate + dayOfMonth + " ";
								}

								startTime.setText(strdate + "00:00:00");
								startTime.setTag(strdate);
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		endTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								int month = (monthOfYear + 1);
								String strdate = year + "-";
								if (month < 10) {
									strdate = strdate + "0" + month + "-";
								} else {
									strdate = strdate + month + "-";
								}
								if (dayOfMonth < 10) {
									strdate = strdate + "0" + dayOfMonth + " ";
								} else {
									strdate = strdate + dayOfMonth + " ";
								}

								endTime.setText(strdate + "23:59:59");
								endTime.setTag(strdate);
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		// 对话框
		new AlertDialog.Builder(getActivity()).setView(itemview)
				.setTitle("提示：输入条件")
				.setPositiveButton("确定", new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(startTime.getTag().toString().equals("")||endTime.getTag().toString().equals("")){
							et_time.setText(startTime.getTag().toString()+endTime.getTag().toString());
						}else{
							et_time.setText(startTime.getTag().toString() + "~"+ endTime.getTag().toString());
						}
						start = startTime.getText().toString();
						end = endTime.getText().toString();
					}
				}).setNegativeButton("取消", null).setCancelable(false) // 触摸不消失
				.show();
		return;
	}
}
