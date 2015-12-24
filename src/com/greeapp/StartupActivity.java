package com.greeapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.greeapp.Assistant.AppInfo;
import com.greeapp.Assistant.PushUtil;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;
import com.greeapp.Infrastructure.CWUtilities.ToastUtil;

public class StartupActivity extends Activity {

    TextView startup_tv_version;
	/**
	 * onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup);
		
		startup_tv_version = (TextView) findViewById(R.id.startup_tv_version);
		startup_tv_version.setText(AppInfo.getVersionShow());
	
		// 延时线程
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				// 判断是否为首次登陆
				Intent intent = new Intent();
				ISqlHelper iSqlHelper = new SqliteHelper(null,getApplicationContext());
				if (AppInfo.isNewVersion()) {
					iSqlHelper.CreateTable("com.greeapp.Entity.UserMessage");
					iSqlHelper.CreateTable("com.greeapp.Entity.LocalCollectData");
					iSqlHelper.CreateTable("com.greeapp.Entity.LocalWorkState");
					intent.setClass(StartupActivity.this, LoginActivity.class);
					intent.putExtra("goto", MainActivity.class.getName());
					AppInfo.markCurrentVersion(); // 标记当前版本
				} else {
					intent.setClass(StartupActivity.this, LoginActivity.class);
					intent.putExtra("goto", MainActivity.class.getName());
				}

				startActivity(intent);
				finish();
			}
		}.start();

	}

	/**
	 * 返回键按下
	 */
	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
	


}
