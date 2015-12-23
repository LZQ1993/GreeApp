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
	     * 初始化导航栏
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
	     * 设置标题
	     */
	    protected void setNavBarTitle(String title) {
	        tvTitle.setText(title);
	    }

	    /**
	     * 获取标题
	     */
	    protected String getNavBarTitle() {
	        return tvTitle.getText().toString();  
	    }

	    /**
	     * 设置右按钮文本
	     */
	    protected void setNavBarRightButtonText(String text) {
	        btnRight.setText(text);
	    }


	    /**
	     * 获取右按钮文本
	     */
	    protected String getNavBarRightButtonText() {
	        return btnRight.getText().toString();
	    }

	    /**
	     * 左按钮监听函数
	     */
	    public void onNavBarLeftButtonClick(View view) {
	    	// 加载动画
	    Animation animation = AnimationUtils.loadAnimation(
	    				getApplicationContext(), R.anim.back_anim);
	        btnLeft.setAnimation(animation);
	        finish();
	    }

	    /**
	     * 右按钮监听函数
	     */
	    public void onNavBarRightButtonClick(View view) {

	    }

	    /**
	     * 标题监听
	     */
	    public void onNavBarTitleClick(View view) {
	    }
	    
	    /**
	     * 设置左按钮可视
	     */
	    protected void setNavBarLeftButtonVisible(boolean visible) {
	        if(visible) btnLeft.setVisibility(View.VISIBLE);
	        else btnLeft.setVisibility(View.INVISIBLE);
	    }

	    /**
	     * 设置右按钮可视
	     */
	    protected void setNavBarRightButtonVisible(boolean visible) {
	        if(visible) btnRight.setVisibility(View.VISIBLE);
	        else btnRight.setVisibility(View.INVISIBLE);
	    }

	}
