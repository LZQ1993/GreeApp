package com.greeapp.UserCenter.Fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.greeapp.R;
import com.greeapp.Entity.ReturnTransactionMessage;
import com.greeapp.Entity.UserInfo;
import com.greeapp.Entity.UserMessage;
import com.greeapp.Infrastructure.CWDataDecoder.DataResult;
import com.greeapp.Infrastructure.CWDataDecoder.JsonDecode;
import com.greeapp.Infrastructure.CWDataRequest.RequestUtility;
import com.greeapp.Infrastructure.CWFragment.DataRequestFragment;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;

public class UserInfoMangerFragment extends DataRequestFragment implements
		OnClickListener {
	private Context mContext;
	private ImageButton nav_bar_btn_left, nav_bar_btn_right;
	private String currentNotiName = "SaveNotifications";
	private String currentNotiName1 = "QueryInfoNotifications";
	private EditText et_name;
	private EditText et_tel;
	private EditText et_address;
	private EditText et_webname;
	private RadioGroup rg_sex;
	private RadioButton rb_male;
	private RadioButton rb_female;
	private String UserName, sex;
	private RadioButton checkRadioButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		Notifications.add(currentNotiName);
		Notifications.add(currentNotiName1);
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
		View view = inflater.inflate(R.layout.fragment_userinfomanger,
				container, false);
		findView(view);
		initData();
		return view;
	}

	private void initData() {
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("UserManagerService", "queryUserInfo");
		Map requestCondition = new HashMap();
		String condition[] = { "UserName" };
		String value[] = { UserName };
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName1);
		setRequestUtility(myru);
		requestData();
		showProgressDialog(getActivity(), "载入中，请稍候...");
	}

	private void findView(View view) {
		nav_bar_btn_left = (ImageButton) view
				.findViewById(R.id.um_nav_bar_btn_left);
		nav_bar_btn_left.setOnClickListener(this);
		nav_bar_btn_right = (ImageButton) view
				.findViewById(R.id.um_nav_bar_btn_right);
		nav_bar_btn_right.setOnClickListener(this);
		et_name = (EditText) view.findViewById(R.id.et_name);
		et_tel = (EditText) view.findViewById(R.id.et_tel);
		et_address = (EditText) view.findViewById(R.id.et_address);
		et_webname = (EditText) view.findViewById(R.id.et_webname);
		rg_sex = (RadioGroup) view.findViewById(R.id.rg_sex);
		rb_male = (RadioButton) view.findViewById(R.id.rb_male);
		rb_female = (RadioButton) view.findViewById(R.id.rb_female);
		rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				// 点击事件获取的选择对象
				checkRadioButton = (RadioButton) rg_sex.findViewById(checkedId);
				sex = checkRadioButton.getText().toString();
			}
		});
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
		if (v == nav_bar_btn_right) {
			save();
		}
	}

	private void save() {
		// 将数据保存到本地
		if (et_name.getText().toString().equals("")
				|| et_tel.getText().toString().equals("")
				|| et_address.getText().toString().equals("")
				|| et_webname.getText().toString().equals("")) {
			DefaultTip(getActivity(),"参数不能为空");	

		} else {
			/*
			 * ISqlHelper iSqlHelper = new SqliteHelper(null, mContext); String
			 * sqlStr = "update UserMessage set UserAddress ='" +
			 * et_address.getText().toString() + "',UserSex='" + sex +
			 * "',UserTel='" + et_tel.getText().toString() + "',WebName='" +
			 * et_webname.getText().toString() + "',Name='" +
			 * et_name.getText().toString() + "' where UserName = '" + UserName
			 * + "'"; iSqlHelper.SQLExec(sqlStr);
			 */
			userMsgUpdate();
		}

	}

	private void userMsgUpdate() {
		
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("UserManagerService", "updateUserInfo");
		Map requestCondition = new HashMap();
		String condition[] = { "UserName", "UserAddress", "WebName", "Name",
				"UserTel", "UserSex" };
		String value[] = { UserName, et_address.getText().toString(),
				et_webname.getText().toString(), et_name.getText().toString(),
				et_tel.getText().toString(),sex };
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName);
		setRequestUtility(myru);
		requestData();
		showProgressDialog(getActivity(), "上传中，请稍候...");
	}

	@Override
	public void updateView() {
		dismissProgressDialog();
		if (result != null) {
			if (CurrentAction == currentNotiName) {
				dataResult=dataDecode.decode(result,"ReturnTransactionMessage");
				if (dataResult != null) {
					DataResult realData = (DataResult) dataResult;
					if (realData.getResultcode().equals("1")) {
						ReturnTransactionMessage msg = (ReturnTransactionMessage) realData.getResult().get(0);
						if (msg.getResult().equals("1")) {
							DefaultTip(getActivity(),msg.tip);				            
						} else {
							DefaultTip(getActivity(),msg.tip);		
						}
					} else {
						DefaultTip(getActivity(),"暂无数据");		
					}
				} else {
					DefaultTip(getActivity(),"网络数据获取失败");	
				}
			}else if (CurrentAction == currentNotiName1) {
				dataResult = dataDecode.decode(result,"UserInfo");
				if (dataResult != null) {
					DataResult realData = (DataResult) dataResult;
					if (realData.getResultcode().equals("1")) {
						UserInfo msg = (UserInfo) realData.getResult().get(0);
						ISqlHelper iSqlHelper = new SqliteHelper(null, mContext);
						String sqlStr = "update UserMessage set UserAddress ='"
								+ msg.UserAddress + "',UserSex='" + msg.UserSex
								+ "',UserTel='" + msg.UserTel + "',WebName='"
								+ msg.WebName + "',Name='" + msg.Name
								+ "' where UserName = '" + UserName + "'";
						iSqlHelper.SQLExec(sqlStr);
						handler.sendEmptyMessage(0);
					} else {
						handler.sendEmptyMessage(0);
					}
				} else {
					DefaultTip(getActivity(),"网络数据获取失败");	
				}
			}
		} else {
			DefaultTip(getActivity(),"网络数据获取失败");	
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ISqlHelper iSqlHelper = new SqliteHelper(null, mContext);
			List<Object> list = iSqlHelper.Query("com.greeapp.Entity.UserMessage", null);
			if (list.size() > 0) {
				UserMessage userMessage = (UserMessage) list.get(0);
				et_name.setText(userMessage.Name);
				et_tel.setText(userMessage.UserTel);
				et_address.setText(userMessage.UserAddress);
				et_webname.setText(userMessage.WebName);
				if (userMessage.UserSex.equals("男")) {
					rb_male.setChecked(true);
					
				} else {
					rb_female.setChecked(true);
				}
				sex = userMessage.UserSex;
			} else {
				et_name.setText("");
				et_tel.setText("");
				et_address.setText("");
				et_webname.setText("");
				rb_male.setChecked(true);
				sex = "男";
			}

		}
	};
}