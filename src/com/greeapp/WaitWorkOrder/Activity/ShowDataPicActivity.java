package com.greeapp.WaitWorkOrder.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.greeapp.R;
import com.greeapp.Infrastructure.CWActivity.NavBarActivity;
import com.greeapp.WaitWorkOrder.Assistant.LoadSDCardPicTask;

public class ShowDataPicActivity extends NavBarActivity{
     private ImageView iv_pic;
     private String picUrl;
     View view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		LayoutInflater inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.activity_showpic, null);// µ√µΩº”‘ÿview
		setContentView(view);
		initNavBar("Õº∆¨‰Ø¿¿", true, null);
		fetchUIFromLayout();
	}

	private void fetchUIFromLayout() {
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		picUrl = getIntent().getStringExtra("PicUrl");
		LoadSDCardPicTask pic0 = new LoadSDCardPicTask(view,R.id.iv_pic);
		pic0.execute(picUrl);
	}

}
