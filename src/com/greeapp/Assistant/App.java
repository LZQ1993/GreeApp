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
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush     
    }

    public static Application getContext() {              
        return context;
    }
}
