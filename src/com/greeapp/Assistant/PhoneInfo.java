package com.greeapp.Assistant;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * �豸��Ϣ
 * 
 */
public class PhoneInfo {
    
    //ϵͳ��Ϣ
    /**
     * ��ȡ�豸�ͺ�
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }
    
    /**
     * ��ȡ�ֻ�Ʒ��
     */
    public static String getPhoneBrand() {
        return Build.BRAND;
    }

    //�绰������
    private static String deviceId = null;
    private static String subscriberId = null;
    
    /**
     * ��ȡ�豸Id
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
     * ��ȡsim��Id
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
