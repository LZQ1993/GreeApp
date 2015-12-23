package com.greeapp.Infrastructure.CWDataRequest;

import java.util.HashMap;
import java.util.Map;

import com.greeapp.Infrastructure.CWDomain.GlobalVariables;

import android.content.Context;

public class RequestUtility {
	private String IP=null;
    private String ComponentName=null;
    private String MethodName=null;
    private String NotificationName=null;
    private String RequestError=null;
    
	public String getRequestError() {
		return RequestError;
	}
	public void setRequestError(String requestError) {
    	if (requestError==null){
    		RequestError=GlobalVariables.ACTION_DATA_RELOAD_ERROR;
    	}
    	else{
    		RequestError=requestError;
    	}
	}

	private Map Params=new HashMap();
    public RequestUtility(){
    	setIP(null);
    	setMethod(null,null);
    	setNotification(null);
    	setRequestError(null);

    }
    public RequestUtility(String _ip,String _componentName,String _methodName,String _notificationName){
    	setIP(_ip);
    	setMethod(_componentName,_methodName);
    	setNotification(_notificationName);
    	setRequestError(null);
    }
    public void setIP(String _ip){
    	if (_ip==null){
    		IP=NetworkInfo.getServiceUrl();
    	}
    	else{
    		IP=_ip;
    	}
    }
    public void setMethod(String _componentName,String _methodName){
    	if (_componentName==null){
    		ComponentName="Client";
    	}
    	else{
    		ComponentName=_componentName;
    	}
    	if (_methodName==null){
    		MethodName="Logon.ashx";
    	}
    	else{
    		MethodName=_methodName;
    	}
    }
    public void setNotification(String _notificationName){
    	if (_notificationName==null){
    		NotificationName=GlobalVariables.ACTION_DATA_RELOAD_OK;
    	}
    	else{
    		NotificationName=_notificationName;
    	}
    }
    public String getIP(){
    	return IP;
    }
    public String getComponentName(){
    	return ComponentName;
    }
    public String getMethodName(){
    	return MethodName;
    }
    public String getNotificationName(){
    	return NotificationName;
    }
    
    public void setParams(Map _params){
    	Params=_params;
    }
    public Map getParams(){
    	return Params;
    }
    
    public String getURLString(){
    	return String.format("%1$s/%2$s/%3$s", IP,ComponentName,MethodName);
    }

}
