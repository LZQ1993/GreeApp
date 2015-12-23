package com.greeapp.Assistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;


public class AppInfo {

    /**
     * 获取应用版本名称
     */
    public static String getVersionName() {
        PackageManager manager = App.getContext().getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(App.getContext().getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            return "-ver not found-";
        }
    }
    
    /**
     * 获取应用版本代码
     */
    public static int getVersionCode() {
        PackageManager manager = App.getContext().getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(App.getContext().getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            return -1;
        }
    }
    
    /**
     * 获取应用显示版本字符
     */
    public static String getVersionShow() {
    	
        return "v " + getVersionName() + " - Bate";
    }
    
    /**
     * 获取应用包名称
     */
    public static String getPackageName() {
        return App.getContext().getPackageName();
    }
    
    /**
     * 获取应用名称
     * @return
     */
    public static String getApplicationName() {
        return "SCloud - Apartment Digitizing";
    }
    
    /**
     * 获取作者信息
     */
    public static String getAuthorInfo() {
        return "\nTeam : China Wit" +
               "\nHome Page : Http://www.china-wit.com/scloud";
    }
    
    /**
     * 更新日期
     * @return
     */
    public static String getUpdateTime() {
        return "2014-8-22";
    }
    
    /**
     * 是否为新版本
     */
    public static boolean isNewVersion() {
        SharedPreferences sp = App.getContext().getSharedPreferences("app_info", Context.MODE_PRIVATE);
        return getVersionCode() > sp.getInt("versionCode", -1);
    }
    
    /**
     * 标记当前版本
     */
    public static void markCurrentVersion() {
        Editor ed = App.getContext().getSharedPreferences("app_info", Context.MODE_PRIVATE).edit();
        ed.putInt("versionCode", getVersionCode());
        ed.commit();
    }

}
