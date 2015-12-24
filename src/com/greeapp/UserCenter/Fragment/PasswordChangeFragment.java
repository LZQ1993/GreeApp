package com.greeapp.UserCenter.Fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.greeapp.LoginActivity;
import com.greeapp.R;
import com.greeapp.Entity.ReturnTransactionMessage;
import com.greeapp.Entity.UserMessage;
import com.greeapp.Infrastructure.CWDataDecoder.DataResult;
import com.greeapp.Infrastructure.CWDataDecoder.JsonDecode;
import com.greeapp.Infrastructure.CWDataRequest.RequestUtility;
import com.greeapp.Infrastructure.CWFragment.DataRequestFragment;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;
import com.greeapp.UserCenter.Activity.UserCenterActivity;

public class PasswordChangeFragment extends DataRequestFragment implements OnClickListener{
	private Context mContext;
	private EditText et_oldpassword;
	private EditText et_newpassword;
	private EditText et_passwordok;
	private Button btn_updatepassword;
	private ImageButton nav_bar_btn_left;
	private String currentNotiName="UpdatePasswordNotifications";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		Notifications.add(currentNotiName);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_passwordchange, container, false);
		findView(view);
		return view;
	}
	private void findView(View view) {
		btn_updatepassword=(Button) view.findViewById(R.id.btn_updatepassword);
		et_oldpassword=(EditText)  view.findViewById(R.id.et_oldpassword);
		et_newpassword=(EditText)  view.findViewById(R.id.et_newpassword);
		et_passwordok=(EditText)  view.findViewById(R.id.et_passwordok);
		nav_bar_btn_left = (ImageButton) view.findViewById(R.id.pc_nav_bar_btn_left);
		btn_updatepassword.setOnClickListener(this);
		nav_bar_btn_left.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v==nav_bar_btn_left){
			// ���ض���
			Animation animation = AnimationUtils.loadAnimation(
									mContext, R.anim.back_anim);
			nav_bar_btn_left.setAnimation(animation);
			onNavBarLeftButtonClick();
		}
		if(v==btn_updatepassword){
			if(et_oldpassword.getText().toString().equals("")||
				et_newpassword.getText().toString().equals("")||
				et_passwordok.getText().toString().equals("")){
				DefaultTip(getActivity(),"������д������Ϣ");	
			}else if(!et_newpassword.getText().toString().equals(et_passwordok.getText().toString())){
				 DefaultTip(getActivity(),"�������������벻һ�£�����ȷ��");					
			}else{
				netWork(et_oldpassword.getText().toString(),et_passwordok.getText().toString());
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void netWork(String oldnum, String newnum) {
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("UserManagerService", "passwordUpdate");
		Map requestCondition = new HashMap();
		String condition[] = { "OldPassword", "NewPassword", "UserName" };
		ISqlHelper iSqlHelper=new SqliteHelper(null,mContext);
		List<Object> list=iSqlHelper.Query("com.greeapp.Entity.UserMessage", null);
		UserMessage userMessage=(UserMessage) list.get(0);
		String value[] = {oldnum,newnum,userMessage.UserName};
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName);
		setRequestUtility(myru);
		requestData();
		showProgressDialog(getActivity(),"�ϴ��У����Ժ�...");
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
							new AlertDialog.Builder(getActivity())
							.setTitle("��ʾ")
							.setMessage(msg.tip)
							.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									ISqlHelper iSqlHelper = new SqliteHelper(null,mContext);					
									UserMessage userMessage = new UserMessage();
									userMessage.Indexmy = "1";
									userMessage.Password = et_passwordok.getText().toString();
									iSqlHelper.Update(userMessage);	
									
									et_oldpassword.setText("");
									et_passwordok.setText("");
									et_newpassword.setText("");			
									return;
								}
							})					
							.setCancelable(false)
							.show();
						     return;
						}else{
							DefaultTip(getActivity(),msg.tip);
					      }
				}else{
					DefaultTip(getActivity(),"��������");
				  }
			 }
			}else{
				DefaultTip(getActivity(),"�������ݻ�ȡʧ��");	
			 }
		}else{
			DefaultTip(getActivity(),"�������ݻ�ȡʧ��");	
		}
	}
	public void onNavBarLeftButtonClick(){
		
		 getActivity().finish();
	}
}
