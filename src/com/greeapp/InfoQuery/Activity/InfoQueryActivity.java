package com.greeapp.InfoQuery.Activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.greeapp.R;
import com.greeapp.Infrastructure.CWActivity.NavBarActivity;

public class InfoQueryActivity extends NavBarActivity {
	private WebView medicalReport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_infoquery);
		initNavBar("资料查询", true, null);
		fetchUIFromLayout();
	
	}


	private void fetchUIFromLayout() {

		// 实例化WebView对象
		medicalReport = (WebView) findViewById(R.id.wv_medicalreport);
		medicalReport.getSettings().setAllowFileAccess(true);
		//支持javascript
		medicalReport.getSettings().setJavaScriptEnabled(true);
		// 设置可以支持缩放
		medicalReport.getSettings().setSupportZoom(true);
		medicalReport.getSettings().setBlockNetworkImage(false);
		medicalReport.getSettings().setBlockNetworkLoads(false);
		// 设置出现缩放工具
		medicalReport.getSettings().setBuiltInZoomControls(true);
		medicalReport.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		//直接加载网页、图片并显示
		medicalReport.getSettings().setLoadsImagesAutomatically(true);
		medicalReport.getSettings().setDomStorageEnabled(true);
		//扩大比例的缩放 设置此属性，可任意比例缩放
		//medicalReport.getSettings().setUseWideViewPort(true);
		//自适应屏幕
		medicalReport.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		medicalReport.getSettings().setLoadWithOverviewMode(true);
		//设置支持获取手势焦点
		medicalReport.requestFocusFromTouch();
		medicalReport.loadUrl("http://www.baidu.com");
		//打开网页时不调用系统浏览器， 而是在本WebView中显示
		medicalReport.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)&&medicalReport.canGoBack()) {
			medicalReport.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
