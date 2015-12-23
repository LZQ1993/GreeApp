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
		initNavBar("���ϲ�ѯ", true, null);
		fetchUIFromLayout();
	
	}


	private void fetchUIFromLayout() {

		// ʵ����WebView����
		medicalReport = (WebView) findViewById(R.id.wv_medicalreport);
		medicalReport.getSettings().setAllowFileAccess(true);
		//֧��javascript
		medicalReport.getSettings().setJavaScriptEnabled(true);
		// ���ÿ���֧������
		medicalReport.getSettings().setSupportZoom(true);
		medicalReport.getSettings().setBlockNetworkImage(false);
		medicalReport.getSettings().setBlockNetworkLoads(false);
		// ���ó������Ź���
		medicalReport.getSettings().setBuiltInZoomControls(true);
		medicalReport.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		//ֱ�Ӽ�����ҳ��ͼƬ����ʾ
		medicalReport.getSettings().setLoadsImagesAutomatically(true);
		medicalReport.getSettings().setDomStorageEnabled(true);
		//������������� ���ô����ԣ��������������
		//medicalReport.getSettings().setUseWideViewPort(true);
		//����Ӧ��Ļ
		medicalReport.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		medicalReport.getSettings().setLoadWithOverviewMode(true);
		//����֧�ֻ�ȡ���ƽ���
		medicalReport.requestFocusFromTouch();
		medicalReport.loadUrl("http://www.baidu.com");
		//����ҳʱ������ϵͳ������� �����ڱ�WebView����ʾ
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
			medicalReport.goBack(); // goBack()��ʾ����WebView����һҳ��
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
