package com.greeapp.Assistant;

import com.greeapp.R;

import cn.jpush.android.api.JPushInterface;
import android.app.Application;


public class App extends Application {

    private final static String TAG = "Gree-App";
    private static Application context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        JPushInterface.setDebugMode(true); 	// ���ÿ�����־,����ʱ��ر���־
        JPushInterface.init(this);     		// ��ʼ�� JPush     
    }

    public static Application getContext() {              
        return context;
    }
}
