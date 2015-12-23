package com.greeapp.Assistant;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * 设备信息
 * 
 */
public class PhoneInfo {
    
    //系统信息
    /**
     * 获取设备型号
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }
    
    /**
     * 获取手机品牌
     */
    public static String getPhoneBrand() {
        return Build.BRAND;
    }

    //电话管理器
    private static String deviceId = null;
    private static String subscriberId = null;
    
    /**
     * 获取设备Id
     */
    public static String getDeviceId() {
        if (deviceId == null || deviceId.equals("") || deviceId.equals("null")) {
            TelephonyManager tm = (TelephonyManager) App.getContext().getSystemService(Context.TELEPHONY_SERVICE);   
            deviceId = tm.getDeviceId();
        }
        return deviceId;
    }
    
    public static String getIMEI() {
        return getDeviceId();
    }

    /**
     * 获取sim卡Id
     */
    public static String getSubscriberId() {
        if (subscriberId == null || subscriberId.equals("") || subscriberId.equals("null")) {
            TelephonyManager tm = (TelephonyManager) App.getContext().getSystemService(Context.TELEPHONY_SERVICE);   
            subscriberId = tm.getSubscriberId();
        }
        return subscriberId;
    }
    
    public static String getIMSI() {
        return getSubscriberId();
    }

}
