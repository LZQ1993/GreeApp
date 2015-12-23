package com.greeapp.Assistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 设置信息
 */
public class SettingInfo {
    
    /**
     * 是否记住密码
     */
    public static boolean isSavePassword() {
        SharedPreferences sp = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE);
        return sp.getBoolean("savePassword", false);
    }
    
    /**
     * 设置记住密码
     */
    public static void setSavePassword(boolean savePassword) {
        Editor ed = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE).edit();
        ed.putBoolean("savePassword", savePassword);
        ed.commit();
    }

    /**
     * 获取保存的用户名
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
     * 获取保存的密码
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
     * 设置保存的用户名
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
     * 设置保存的密码
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
     * 清除保存的信息
     */
    public static void clearSavedInfo() {
        Editor ed = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE).edit();
        ed.putString("savedUsername", "");
        ed.putString("savedPassword", "");
        ed.commit();
    }
    
    
	 /*
     * 二维码设置
     */
    
    /**
     * 是否蜂鸣
     */
    public static boolean isQrcodePlayBeepSound() {
        SharedPreferences sp = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE);
        return sp.getBoolean("qrcodePlayBeepSound", true);
    }
    
    /**
     * 设置蜂鸣
     */
    public static void setQrcodePlayBeepSound(boolean play) {
        Editor ed = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE).edit();
        ed.putBoolean("qrcodePlayBeepSound", play);
        ed.commit();
    }
    
    /**
     * 是否震动
     */
    public static boolean isQrcodeVibrate() {
        SharedPreferences sp = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE);
        return sp.getBoolean("qrcodeVibrate", true);
    }
    
    /**
     * 设置震动
     */
    public static void setQrcodeVibrate(boolean vibrate) {
        Editor ed = App.getContext().getSharedPreferences("setting_info", Context.MODE_PRIVATE).edit();
        ed.putBoolean("qrcodeVibrate", vibrate);
        ed.commit();
    }
    

	
}