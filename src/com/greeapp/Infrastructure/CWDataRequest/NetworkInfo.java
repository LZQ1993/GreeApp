package com.greeapp.Infrastructure.CWDataRequest;


public class NetworkInfo {

	//private final static String serviceUrl = "http://www.symnyk.com";
	private final static String serviceUrl = "http://192.168.0.152/GreeApp";
    private final static String homePageUrl = "http://pupboss.com/scloud/";
    
    
    public static String getServiceUrl() {
        return serviceUrl;
    }
    
 
    public static String getHomePageUrl() {
    	return homePageUrl;
    }

}
