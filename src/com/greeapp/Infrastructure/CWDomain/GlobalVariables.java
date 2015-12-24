package com.greeapp.Infrastructure.CWDomain;

import android.app.Activity;

public class GlobalVariables {
	//用于标识数据是在线所取还是本地获取
	public  static final boolean ISLOCALDATA=false;
	
	//用于网络访问的标识
	public static final String ACTION_DATA_RELOAD_OK="com.chinawit.data_reload_ok";
	public static final String ACTION_DATA_RELOAD_ERROR="com.chinawit.data_reload_error";
	public static final String DATA_RESULT="com.chinawit.data_result";
	//解析
	public static final String DATA_DECODE_RESULT="com.greeapp.Infrastructure.CWDataDecoder.DataResult";
	public static final String DATAEntity_PACKAGE="com.greeapp.Entity";
	public static final String SONOBJECTDEFULT_PACKAGE="com.greeapp.Infrastructure.CWDomain.ReturnTransactionMessage";
	//定义基类主键
	public static final String PRIMARY_KEY_NAME="AutoID";
	
    public final static int RESULT_OK = Activity.RESULT_OK;
    public final static int RESULT_ERROR = Activity.RESULT_FIRST_USER;
    public final static int RESULT_CANCELED = Activity.RESULT_CANCELED;
    public final static int REQUEST_DEFAULT = 0;
    
}
