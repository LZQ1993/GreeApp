package com.greeapp.Infrastructure.CWDataRequest;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import android.content.Context;

public class HttpUtil {
	
	private static AsyncHttpClient httpClient = buildAsyncHttpClient();

	private static AsyncHttpClient buildAsyncHttpClient() {
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.setTimeout(5000);
		return httpClient;
	}

	/**
	 * get方法
	 */
	public static void get(String url, ResponseHandlerInterface responseHandler) {
		httpClient.get(url, responseHandler);
	}
	public static void get(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		httpClient.get(url, params, responseHandler);
	}
	public static void get(Context context, String url, ResponseHandlerInterface responseHandler) {
		httpClient.get(context, url, responseHandler);
	}
	public static void get(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		httpClient.get(context, url, params, responseHandler);
	}
	public static void get(Context context, String url, Header[] headers, RequestParams params, ResponseHandlerInterface responseHandler) {
		httpClient.get(context, url, headers, params, responseHandler);
	}
	
	/**
	 * post方法
	 */
	public static void post(String url, ResponseHandlerInterface responseHandler) {
		httpClient.post(url, responseHandler);
	}
	public static void post(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		httpClient.post(url, params, responseHandler);
	}
	public static void post(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		httpClient.post(context, url, params, responseHandler);
	}
	public static void post(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
		httpClient.post(context, url, entity, contentType, responseHandler);
	}
	public static void post(Context context, String url, Header[] headers, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
		httpClient.post(context, url, headers, entity, contentType, responseHandler);
	}
	public static void post(Context context, String url, Header[] headers, RequestParams params, String contentType, ResponseHandlerInterface responseHandler) {
		httpClient.post(context, url, headers, params, contentType, responseHandler);
	}

}
