package com.greeapp.Fragment;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.greeapp.R;
import com.greeapp.Assistant.PhoneInfo;
import com.greeapp.Entity.ReturnTransactionMessage;
import com.greeapp.Entity.UserMessage;
import com.greeapp.Infrastructure.CWDataDecoder.DataResult;
import com.greeapp.Infrastructure.CWDataDecoder.JsonDecode;
import com.greeapp.Infrastructure.CWDataRequest.RequestUtility;
import com.greeapp.Infrastructure.CWFragment.DataRequestFragment;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;

public class LoginFragment extends DataRequestFragment {
	private Context mContext;
	private RelativeLayout rl_user;
	private Button mLogin;
	private ImageView login_picture;
	private EditText edt_username;
	private EditText edt_password;
	private CheckBox cb_savePwd;
	private SharedPreferences config;
	private String currentNotiName="UserLoginNotifications";
	private String currentNotiName1="DeviceSerialNotifications";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		Notifications.add(currentNotiName);
		Notifications.add(currentNotiName1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		findView(view);
		init();
		return view;
	}

	private void findView(View view) {
		rl_user = (RelativeLayout) view.findViewById(R.id.rl_user);
		mLogin = (Button) view.findViewById(R.id.login);
		edt_username = (EditText) view.findViewById(R.id.login_edt_username);
		edt_password = (EditText) view.findViewById(R.id.login_edt_password);
		login_picture = (ImageView) view.findViewById(R.id.login_picture);

		cb_savePwd = (CheckBox) view.findViewById(R.id.login_cb_savePwd);
		config = getActivity().getSharedPreferences("config",mContext.MODE_PRIVATE);
		boolean isChecked = config.getBoolean("isChecked", false);
		if (isChecked) {
			edt_username.setText(config.getString("UserName", ""));
			edt_password.setText(config.getString("Password", ""));
		}
		cb_savePwd.setChecked(isChecked);
	}


	private void init() {
		
		Animation anim = AnimationUtils.loadAnimation(mContext,
				R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);

		Animation anim2 = AnimationUtils.loadAnimation(mContext,
				R.anim.login_head_anim);
		anim2.setFillAfter(true);
		login_picture.startAnimation(anim2);

		mLogin.setOnClickListener(loginOnClickListener);

	}

	/**
	 * 执行登录
	 */

	private OnClickListener loginOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (edt_username.getText().toString().equals("")) {
				Toast.makeText(mContext, "用户名不能为空！",
						Toast.LENGTH_SHORT).show();
			} else if (edt_password.getText().toString().equals("")) {
				Toast.makeText(mContext, "密码不能为空！",
						Toast.LENGTH_SHORT).show();
			} else {
				loginTask();
			}

		}
	};

	/**
	 * 登录网络任务
	 */
	private void loginTask() {
		//设备码不为空-获取本地设备码
        String IMEI = PhoneInfo.getDeviceId();
        //未获得设备码
        if (IMEI == null || IMEI.equals("")) {
            new AlertDialog.Builder(getActivity())    
            .setTitle("提示")  
            .setMessage("未获取到设备标识！")  
            .setPositiveButton("确定",null)
            .show();
            return;
        }else{
        	NetWork(IMEI);
        }
	}

	private void NetWork(String iMEI) {
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("UserManagerService", "UserLogin");
		Map requestCondition = new HashMap();
		String condition[] = { "UserName", "UserPassword", "DeviceSerialNumber" };
		String value[] = { edt_username.getText().toString(),
				edt_password.getText().toString(), iMEI };
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName);
		setRequestUtility(myru);
		requestData();
		showProgressDialog(getActivity(),"登录中，请稍候...");
	}
	@Override
	public void updateView() {
		dismissProgressDialog();
		if (result != null) {		
			dataResult=dataDecode.decode(result,"ReturnTransactionMessage");
			if (dataResult != null) {
				DataResult realData = (DataResult) dataResult;
				if(CurrentAction==currentNotiName){
					if (realData.getResultcode().equals("1")) {
						ReturnTransactionMessage msg = (ReturnTransactionMessage) realData.getResult().get(0);
						if (msg.getResult().equals("1")) {				
							ISqlHelper iSqlHelper = new SqliteHelper(null,mContext);
							iSqlHelper.SQLExec("delete from UserMessage");// 删除表中原有的数据，保证只有一条
							UserMessage userMessage = new UserMessage();
							userMessage.Indexmy = "1";
							userMessage.UserName = edt_username.getText().toString();
							userMessage.Password = edt_password.getText().toString();
							iSqlHelper.Insert(userMessage);
							isRemember(edt_username.getText().toString(),edt_password.getText().toString());
							Class<?> gotoClz = null;
							try {
								gotoClz = Class.forName(getActivity().getIntent().getStringExtra("goto"));
							} catch (Exception e) {
						    }
							if (gotoClz != null) {
								Intent intent = new Intent(mContext, gotoClz);
								intent.putExtra("UserID", edt_username.getText().toString());
								intent.putExtra("IsShow", "0");
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
								startActivity(intent);
							}
							getActivity().finish();
							return;
						 } else if(msg.tip.equals("账号和手机还没有绑定")&&msg.result.equals("0")){
							new AlertDialog.Builder(getActivity())
									.setTitle("提示")
									.setMessage("账号和手机还没有绑定,是否现在申请验证")
									.setPositiveButton("申请",new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog,int which) {
													bindDeviceSerialNumber();
												}		
											})
									.setNegativeButton("取消", null)
									.setCancelable(false)
									.show();
						 }else{
							 DefaultTip(getActivity(),"用户无效或密码无效");							
						       }
						 }else{
							 DefaultTip(getActivity(),"暂无数据");							
						 }
				}else if(CurrentAction==currentNotiName1){
					if (realData.getResultcode().equals("1")) {
						ReturnTransactionMessage msg = (ReturnTransactionMessage)realData.getResult().get(0);
						if (msg.getResult().equals("1")) {
							 DefaultTip(getActivity(),msg.tip+"请重新登录");							
						}else{
							 DefaultTip(getActivity(),msg.tip);							
						}
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
	
	private void bindDeviceSerialNumber() {
		//获取本地设备码
        String IMEI = PhoneInfo.getDeviceId();
        if (IMEI == null || IMEI.equals("")) {
            new AlertDialog.Builder(getActivity())    
            .setTitle("提示")  
            .setMessage("未获取到设备标识，验证失败！")  
            .setPositiveButton("确定",null)
            .show();
            return;
        }else{        	
    		RequestUtility myru = new RequestUtility();
    		myru.setIP(null);
    		myru.setMethod("UserManagerService", "DeviceNumberVerify");
    		Map requestCondition = new HashMap();
    		String condition[] = { "UserName", "UserPassword", "DeviceSerialNumber" };
    		String value[] = { edt_username.getText().toString(),
    				edt_password.getText().toString(), IMEI };
    		String strJson = JsonDecode.toJson(condition, value);
    		requestCondition.put("json", strJson);
    		myru.setParams(requestCondition);
    		myru.setNotification(currentNotiName1);
    		setRequestUtility(myru); 		
    		requestData();
    		showProgressDialog(getActivity(),"载入中，请稍候...");
          }
      }	
	

	// 是否记住密码
	private void isRemember(String UserName, String Password) {

		Editor edit = config.edit();
		boolean isChecked = cb_savePwd.isChecked();
		edit.putBoolean("isChecked", isChecked);
		if (isChecked) {
			edit.putString("UserName", UserName)
			.putString("Password", Password);
		} else {
			edit.remove("UserName");
			edit.remove("Password");
		}
		edit.commit();

	}
	
}
