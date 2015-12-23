package com.greeapp.Infrastructure.CWBroadcastReceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.greeapp.Infrastructure.CWDomain.GlobalVariables;

public class ResultBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		dataReload(context,intent);
	}
	public void dataReload(Context context, Intent intent){
		  Toast.makeText(context, "消息内容为:网络访问成功"+intent.getStringExtra(GlobalVariables.DATA_RESULT),
				Toast.LENGTH_SHORT).show();
	}
}
