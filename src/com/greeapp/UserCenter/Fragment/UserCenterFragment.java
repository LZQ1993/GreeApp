package com.greeapp.UserCenter.Fragment;

import java.util.List;

import com.greeapp.R;
import com.greeapp.Entity.UserMessage;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;
import com.greeapp.UserCenter.Activity.UserCenterActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class UserCenterFragment extends Fragment implements OnClickListener {
	private Context mContext;
	private Button btn_userInfoManger, btn_passwordChange;
	private TextView tv_userName,tv_webName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_usercenter, container,
				false);
		findView(view);
		return view;
	}

	private void findView(View view) {
		//btn_userInfoManger = (Button) view.findViewById(R.id.btn_userInfoManger);
		btn_passwordChange = (Button) view
				.findViewById(R.id.btn_passwordChange);
		tv_webName = (TextView) view.findViewById(R.id.tv_WebName);
		//btn_userInfoManger.setOnClickListener((OnClickListener) this);
		btn_passwordChange.setOnClickListener((OnClickListener) this);
		tv_userName = (TextView) view.findViewById(R.id.tv_userName);
		ISqlHelper iSqlHelper = new SqliteHelper(null, mContext);
		List<Object> list = iSqlHelper.Query("com.greeapp.Entity.UserMessage",null);
		if (list.size() > 0) {
			UserMessage userMessage = (UserMessage) list.get(0);
			tv_userName.setText(userMessage.UserName);
			tv_webName.setText(userMessage.WebName);
		}else{
			tv_userName.setText("нч");
			tv_webName.setVisibility(View.GONE);
		}
		

	}

	@Override
	public void onClick(View v) {
		/*if (v == btn_userInfoManger) {
			Intent intent = new Intent(mContext, UserCenterActivity.class);
			intent.putExtra("Key", "UserInfoManger");
			getActivity().startActivity(intent);
		}*/
		if (v == btn_passwordChange) {
			Intent intent = new Intent(mContext, UserCenterActivity.class);
			intent.putExtra("Key", "PasswordChange");
			getActivity().startActivity(intent);
		}
	}

}
