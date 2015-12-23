package com.greeapp.WaitWorkOrder.Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.greeapp.R;
import com.greeapp.WaitWorkOrder.Assistant.Bimp;
import com.greeapp.WaitWorkOrder.Assistant.ImageItem;
import com.greeapp.WaitWorkOrder.Assistant.PhotoView;
import com.greeapp.WaitWorkOrder.Assistant.PublicWay;
import com.greeapp.WaitWorkOrder.Assistant.ViewPagerFixed;

/**
 * 这个是用于进行图片浏览时的界面
 * 
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日 下午11:47:53
 */
public class GalleryActivity extends Activity {
	private Intent intent;
	// 返回按钮
	private Button back_bt;
	// 删除按钮
	private Button del_bt;
	// 获取前一个activity传过来的ID
	private int ID;
	// 当前的位置
	private int location = 0;

    private ImageView imageView1;
	private Context mContext;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_gallery);// 切屏到主界面
		PublicWay.activityList.add(this);
		mContext = this;
		back_bt = (Button) findViewById(R.id.gallery_back);
		del_bt = (Button) findViewById(R.id.gallery_del);
		back_bt.setOnClickListener(new BackListener());
		del_bt.setOnClickListener(new DelListener());
		ID = getIntent().getIntExtra("ID", 0);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
			ImageItem li = Bimp.tempSelectBitmap.get(i);
			if (li.imageId == ID) {
				imageView1.setImageBitmap(li.getBitmap());
			}
		}
		
	}


	// 返回按钮添加的监听器
	private class BackListener implements OnClickListener {

		public void onClick(View v) {
			finish();
		}
	}

	// 删除按钮添加的监听器
	private class DelListener implements OnClickListener {

		public void onClick(View v) {
			for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
				ImageItem li = Bimp.tempSelectBitmap.get(i);
				if (li.imageId == ID) {
					String name =Bimp.tempSelectBitmap.get(i).imagePath;
					File file = new File(name);
					file.delete();
					Bimp.tempSelectBitmap.remove(i);
				}
			}
			Bimp.max--;
			Intent intent = new Intent();
			intent.putExtra("ID",ID);
			setResult(1000, intent);
			finish();
		}
	}

}
