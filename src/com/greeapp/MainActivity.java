package com.greeapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.greeapp.Assistant.MainFragmentAdpater;
import com.greeapp.Assistant.PushUtil;
import com.greeapp.Entity.UserMessage;
import com.greeapp.Fragment.MainFragment;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;
import com.greeapp.Order.Fragment.OrderFragment;
import com.greeapp.UserCenter.Fragment.UserCenterFragment;

public class MainActivity extends FragmentActivity {
	private RadioGroup rg;
	private List<Fragment> fragments;
	private long firstBackKeyTime = 0;
	  private static final String TAG = "JPush";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ISqlHelper iSqlHelper = new SqliteHelper(null, this);
		List<Object> list = iSqlHelper.Query("com.greeapp.Entity.UserMessage",null);
		if (list.size() > 0) {
			UserMessage userMessage = (UserMessage) list.get(0);
            setAlias(userMessage.UserName);
		}else{
			 setAlias("");
		}
		registerMessageReceiver();  // used for receive msg
		rg = (RadioGroup) findViewById(R.id.radioGroup);// 实例化radiogroup
		fragments = new ArrayList<Fragment>();

		// 分别添加4个fragment
		fragments.add(new MainFragment());
		fragments.add(new OrderFragment());
		fragments.add(new UserCenterFragment());
		new MainFragmentAdpater(this, fragments, R.id.id_content, rg);// 设置适配器

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)// 主要是对这个函数的复写
	{
		// TODO Auto-generated method stub

		if ((keyCode == KeyEvent.KEYCODE_BACK)
				&& (event.getAction() == KeyEvent.ACTION_DOWN)) {
			if (System.currentTimeMillis() - firstBackKeyTime > 2000) // 2s内再次选择back键有效
			{
				Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
				firstBackKeyTime = System.currentTimeMillis();
			} else {
				ISqlHelper iSqlHelper = new SqliteHelper(null,getApplicationContext());
				iSqlHelper.SQLExec("delete from UserMessage");// 删除表中原有的数据，保证只有一条
				finish();
				System.exit(0); // 凡是非零都表示异常退出!0表示正常退出!
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.greeapp.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public static boolean isForeground = false;
	
	@Override
	protected void onResume() {	
		isForeground = true;
		super.onResume();
	}


	@Override
	protected void onPause() {		
		isForeground = false;
		super.onPause();
	}


	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}
	

	public void registerMessageReceiver() {	
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);		
		registerReceiver(mMessageReceiver, filter);
		
	}

	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				
			}
		}
	}
	
	private void setAlias(String alias){
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(this,R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		//调用JPush API设置Alias
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}
	
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case 0:
                logs = "Set tag and alias success";
                Log.i(TAG, logs);
                break;
                
            case 6002:
                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                Log.i(TAG, logs);
                if (PushUtil.isConnected(getApplicationContext())) {
                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                } else {
                	Log.i(TAG, "No network");
                }
                break;
            
            default:
                logs = "Failed with errorCode = " + code;
                Log.e(TAG, logs);
            }
        }
	    
	};
	
	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;
	
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case MSG_SET_ALIAS:
                Log.d(TAG, "Set alias in handler.");
                JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                break;
            default:
                Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
}
