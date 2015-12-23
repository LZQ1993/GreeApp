package com.greeapp.WaitWorkOrder.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.greeapp.R;
import com.greeapp.Entity.LocalCollectData;
import com.greeapp.Entity.UserMessage;
import com.greeapp.Infrastructure.CWFragment.DataRequestFragment;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;
import com.greeapp.WaitWorkOrder.Activity.ShowDataPicActivity;
import com.greeapp.WaitWorkOrder.Assistant.LoadSDCardPicTask;

public class FinishedCollectDataDetailFragment extends DataRequestFragment implements OnClickListener {
	private Context mContext;
	private String SubProductID;
	private ImageButton nav_bar_btn_left;
	private LinearLayout ll_Interior_1, ll_External, ll_Interior_2;
	private TextView tv_Interior_1, tv_External, tv_Interior_2,tv_productType;
	private ImageView iv_Interior_1, iv_External, iv_Interior_2;
	View view;
	LocalCollectData localCollectData;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		SubProductID = getActivity().getIntent().getStringExtra("SubProductID");	
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_finishedcollectdatadetail,
				container, false);
		nav_bar_btn_left = (ImageButton) view
				.findViewById(R.id.fcdd_nav_bar_btn_left);
		nav_bar_btn_left.setOnClickListener(this);
		
		ll_Interior_1 = (LinearLayout) view.findViewById(R.id.ll_Interior_1);
		ll_External = (LinearLayout) view.findViewById(R.id.ll_External);
		ll_Interior_2 = (LinearLayout) view.findViewById(R.id.ll_Interior_2);
		
		tv_Interior_1 = (TextView) view.findViewById(R.id.tv_Interior_1);
		tv_External = (TextView) view.findViewById(R.id.tv_External);
		tv_Interior_2 = (TextView) view.findViewById(R.id.tv_Interior_2);
		tv_productType = (TextView) view.findViewById(R.id.tv_productType);
		
		iv_Interior_1 = (ImageView) view.findViewById(R.id.iv_Interior_1);
		iv_External = (ImageView) view.findViewById(R.id.iv_External);
		iv_Interior_2 = (ImageView) view.findViewById(R.id.iv_Interior_2);

		iv_Interior_1.setOnClickListener(this);
		iv_External.setOnClickListener(this);
		iv_Interior_2.setOnClickListener(this);
		initData();
		return view;
	}
	private void initData() {
		ISqlHelper iSqlHelper = new SqliteHelper(null, mContext);
 		List<Object> list = iSqlHelper.Query("com.greeapp.Entity.LocalCollectData","SubProductID='"+SubProductID+"' and IsFinishedCollectFlag ='1'");
		if(list.size()>0){
			localCollectData = (LocalCollectData) list.get(0);
			tv_productType.setText(localCollectData.ProductModel);
			if(localCollectData.MachineType.equals("1")){
				ll_Interior_2.setVisibility(View.GONE);
				tv_Interior_1.setText(localCollectData.InteriorNum1);
				tv_External.setText(localCollectData.ExternalNum);
				LoadSDCardPicTask pic1 = new LoadSDCardPicTask(view,R.id.iv_Interior_1);
				pic1.execute(localCollectData.InteriorPicPath1);
				LoadSDCardPicTask pic2 = new LoadSDCardPicTask(view,R.id.iv_External);
				pic2.execute(localCollectData.ExternalPicPath);	
			}
			else if(localCollectData.MachineType.equals("2")){
				tv_Interior_1.setText(localCollectData.InteriorNum1);
				tv_External.setText(localCollectData.ExternalNum);
				tv_Interior_2.setText(localCollectData.InteriorNum2);
				LoadSDCardPicTask pic1 = new LoadSDCardPicTask(view,R.id.iv_Interior_1);
				pic1.execute(localCollectData.InteriorPicPath1);	
				LoadSDCardPicTask pic2 = new LoadSDCardPicTask(view,R.id.iv_External);
				pic2.execute(localCollectData.ExternalPicPath);		
				LoadSDCardPicTask pic3 = new LoadSDCardPicTask(view,R.id.iv_Interior_2);
				pic3.execute(localCollectData.InteriorPicPath2);	
							
			}else if(localCollectData.MachineType.equals("5")){
				ll_Interior_1.setVisibility(View.GONE);	
				ll_External.setVisibility(View.VISIBLE);
				ll_Interior_2.setVisibility(View.GONE);	
				tv_External.setText(localCollectData.ExternalNum);
				LoadSDCardPicTask pic1 = new LoadSDCardPicTask(view,R.id.iv_External);
				pic1.execute(localCollectData.ExternalPicPath);
				
			}else{
				ll_External.setVisibility(View.GONE);
				ll_Interior_2.setVisibility(View.GONE);	
				tv_Interior_1.setText(localCollectData.InteriorNum1);
				LoadSDCardPicTask pic1 = new LoadSDCardPicTask(view,R.id.iv_Interior_1);
				pic1.execute(localCollectData.InteriorPicPath1);	
			}
			
		}else{
			DefaultTip(getActivity(), "暂无数据");
		}
		
	}
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		if (v == nav_bar_btn_left) {
			// 加载动画
			Animation animation = AnimationUtils.loadAnimation(mContext,
					R.anim.back_anim);
			nav_bar_btn_left.setAnimation(animation);
			getActivity().finish();
		}
		if(v==iv_Interior_1){
          intent.setClass(getActivity(), ShowDataPicActivity.class);
          intent.putExtra("PicUrl", localCollectData.InteriorPicPath1);
          startActivity(intent);
		}
		if(v==iv_External){
			 intent.setClass(getActivity(), ShowDataPicActivity.class);
	          intent.putExtra("PicUrl", localCollectData.ExternalPicPath);
	          startActivity(intent);
		}
		if(v==iv_Interior_2){
			 intent.setClass(getActivity(), ShowDataPicActivity.class);
	          intent.putExtra("PicUrl", localCollectData.InteriorPicPath2);
	          startActivity(intent);
		}	
	}
	
	
}
