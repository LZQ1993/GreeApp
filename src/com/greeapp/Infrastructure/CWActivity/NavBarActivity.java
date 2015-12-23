package com.greeapp.Infrastructure.CWActivity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greeapp.R;

public class NavBarActivity extends Activity {

    
	    private TextView tvTitle;
	    private ImageButton btnLeft;  
	    private Button btnRight;

	    /**
	     * onCreate
	     */
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
    
	    }
	    
	   
	    /**
	     * ��ʼ��������
	     */
	    protected void initNavBar(String title,Boolean visible,String right) {
	        tvTitle = (TextView) findViewById(R.id.nav_bar_tv_title);
	        tvTitle.setText(title);
	        btnLeft = (ImageButton) findViewById(R.id.nav_bar_btn_left);
	        if(visible) btnLeft.setVisibility(View.VISIBLE);
	        else btnLeft.setVisibility(View.INVISIBLE);
	        btnRight = (Button) findViewById(R.id.nav_bar_btn_right);
	        btnRight.setText(right);
	        if(title == null) tvTitle.setVisibility(View.INVISIBLE);
	        if(right == null) btnRight.setVisibility(View.INVISIBLE);
	    }

	    /**
	     * ���ñ���
	     */
	    protected void setNavBarTitle(String title) {
	        tvTitle.setText(title);
	    }

	    /**
	     * ��ȡ����
	     */
	    protected String getNavBarTitle() {
	        return tvTitle.getText().toString();  
	    }

	    /**
	     * �����Ұ�ť�ı�
	     */
	    protected void setNavBarRightButtonText(String text) {
	        btnRight.setText(text);
	    }


	    /**
	     * ��ȡ�Ұ�ť�ı�
	     */
	    protected String getNavBarRightButtonText() {
	        return btnRight.getText().toString();
	    }

	    /**
	     * ��ť��������
	     */
	    public void onNavBarLeftButtonClick(View view) {
	    	// ���ض���
	    Animation animation = AnimationUtils.loadAnimation(
	    				getApplicationContext(), R.anim.back_anim);
	        btnLeft.setAnimation(animation);
	        finish();
	    }

	    /**
	     * �Ұ�ť��������
	     */
	    public void onNavBarRightButtonClick(View view) {

	    }

	    /**
	     * �������
	     */
	    public void onNavBarTitleClick(View view) {
	    }
	    
	    /**
	     * ������ť����
	     */
	    protected void setNavBarLeftButtonVisible(boolean visible) {
	        if(visible) btnLeft.setVisibility(View.VISIBLE);
	        else btnLeft.setVisibility(View.INVISIBLE);
	    }

	    /**
	     * �����Ұ�ť����
	     */
	    protected void setNavBarRightButtonVisible(boolean visible) {
	        if(visible) btnRight.setVisibility(View.VISIBLE);
	        else btnRight.setVisibility(View.INVISIBLE);
	    }

	}
