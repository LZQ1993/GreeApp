package com.greeapp.Assistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * ������Ϣ
 */
public class SettingInfo {
    
    /**
     * �Ƿ��ס����
     */
    public static boolean isSavePassword() {
        SharedPreferences sp = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE);
        return sp.getBoolean("savePassword", false);
    }
    
    /**
     * ���ü�ס����
     */
    public static void setSavePassword(boolean savePassword) {
        Editor ed = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE).edit();
        ed.putBoolean("savePassword", savePassword);
        ed.commit();
    }

    /**
     * ��ȡ������û���
     */
    public static String getSavedUsername() {
        SharedPreferences sp = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE);
        try {
            return Des3Util.decode(sp.getString("savedUsername", ""));
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * ��ȡ���������
     */
    public static String getSavedPassword() {
        SharedPreferences sp = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE);
        try {
            return Des3Util.decode(sp.getString("savedPassword", ""));
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * ���ñ�����û���
     */
    public static void setSavedUsername(String value) {
        Editor ed = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE).edit();
        try {
            ed.putString("savedUsername", Des3Util.encode(value));
        } catch (Exception e) {
            ed.putString("savedUsername", "");
        }
        ed.commit();
    }
    
    /**
     * ���ñ��������
     */
    public static void setSavedPassword(String value) {
        Editor ed = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE).edit();
        try {
            ed.putString("savedPassword", Des3Util.encode(value));
        } catch (Exception e) {
            ed.putString("savedPassword", "");
        }
        ed.commit();
    }
    
    /**
     * ����������Ϣ
     */
    public static void clearSavedInfo() {
        Editor ed = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE).edit();
        ed.putString("savedUsername", "");
        ed.putString("savedPassword", "");
        ed.commit();
    }
    
    
	 /*
     * ��ά������
     */
    
    /**
     * �Ƿ����
     */
    public static boolean isQrcodePlayBeepSound() {
        SharedPreferences sp = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE);
        return sp.getBoolean("qrcodePlayBeepSound", true);
    }
    
    /**
     * ���÷���
     */
    public static void setQrcodePlayBeepSound(boolean play) {
        Editor ed = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE).edit();
        ed.putBoolean("qrcodePlayBeepSound", play);
        ed.commit();
    }
    
    /**
     * �Ƿ���
     */
    public static boolean isQrcodeVibrate() {
        SharedPreferences sp = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE);
        return sp.getBoolean("qrcodeVibrate", true);
    }
    
    /**
     * ������
     */
    public static void setQrcodeVibrate(boolean vibrate) {
        Editor ed = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE).edit();
        ed.putBoolean("qrcodeVibrate", vibrate);
        ed.commit();
    }
    

	
}