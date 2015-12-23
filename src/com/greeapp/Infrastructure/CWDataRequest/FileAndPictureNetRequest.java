package com.greeapp.Infrastructure.CWDataRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.greeapp.Infrastructure.CWDomain.GlobalVariables;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FileAndPictureNetRequest implements IRequest {
	private String responseResult;
	private Context context;
	
	
	public FileAndPictureNetRequest(Context context) {
		this.context = context;
	}
	
	public String getResponseResult() {
		return responseResult;
	}

	public void setResponseResult(String responseResult) {
		this.responseResult = responseResult;
	}

	@Override
	public String responseData() {

		return responseResult;
	}

	@Override
	public void requestData(RequestUtility _requestUtility) {
		 RequestParams params = new RequestParams();
		 Iterator iterator = _requestUtility.getParams().keySet().iterator();                
         while (iterator.hasNext()) {    
          Object key = iterator.next();   
          params.put(key.toString(),_requestUtility.getParams().get(key)); 
          
         }  
			

		 final String action=_requestUtility.getNotificationName();
		 final String _errorAction=_requestUtility.getRequestError();
         HttpUtil.post(_requestUtility.getURLString(), params, new AsyncHttpResponseHandler() {
        	Intent  intent = new Intent();
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] bytes,
					Throwable error) {
				responseResult = null;
				intent.setAction(_errorAction);
				LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] bytes) {
				String content="";
				try {
					content = new String(bytes, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				responseResult = content;
				intent.setAction(action);
				intent.putExtra(GlobalVariables.DATA_RESULT, responseResult);
				LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
			}

			
         });
		
	}

}

