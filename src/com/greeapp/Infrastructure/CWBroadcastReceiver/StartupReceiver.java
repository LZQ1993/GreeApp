package com.greeapp.Infrastructure.CWBroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartupReceiver extends BroadcastReceiver {
	private static final String TAG="test";

	@Override
	public void onReceive(Context context, Intent intent) {
		//һ��������������һ������ 
		Log.i(TAG,"Received started"+intent.getAction());
	}

}
