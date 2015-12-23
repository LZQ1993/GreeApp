package com.greeapp.WaitWorkOrder.Assistant;

import java.io.File;
import java.io.FileInputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.greeapp.R;

public  class LoadSDCardPicTask extends AsyncTask<String, Void, Bitmap>{

	private View resultView;
	private int imageContainer;
	public LoadSDCardPicTask(View resultView, int imageContainer) {
		this.resultView = resultView;
		this.imageContainer=imageContainer;
	}
	
	// doInBackground完成后才会被调用
	@Override
	protected void onPostExecute(Bitmap bitmap) {
	// 调用setTag保存图片以便于自动更新图片
	// resultView.setTag(bitmap);
	if (bitmap == null) {
		((ImageView) resultView.findViewById(imageContainer))
		                 .setImageResource(R.drawable.plugin_camera_no_pictures);
		}
	else {
	    ((ImageView) resultView.findViewById(imageContainer))
	                     .setImageBitmap(bitmap);
	    }
	}	
	// 从本地下载图片
	@Override
	protected Bitmap doInBackground(String... params) {
	Bitmap image = null;
	try {
	File file = new File(params[0]);
	FileInputStream fs = new FileInputStream(file);
	image = BitmapFactory.decodeStream(fs);
	fs.close();
	} catch (Exception e) {
	e.printStackTrace();
	}
	return image;
	}
}