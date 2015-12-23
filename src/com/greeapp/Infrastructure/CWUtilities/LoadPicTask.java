package com.greeapp.Infrastructure.CWUtilities;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.greeapp.R;

public class LoadPicTask extends AsyncTask<String, Void, Bitmap>{

	private View resultView;
	private int imageContainer;
	private int defaultPic=R.drawable.ic_launcher;

	public void setDefaultPic(int defaultPic) {
		this.defaultPic = defaultPic;
	}

	public LoadPicTask(View resultView, int imageContainer) {
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
		                 .setImageResource(defaultPic);
		}
	else {
	    ((ImageView) resultView.findViewById(imageContainer))
	                     .setImageBitmap(bitmap);
	    }
	}
	
	// 从网上下载图片
	@Override
	protected Bitmap doInBackground(String... params) {
	Bitmap image = null;
	try {
	// new URL对象 把网址传入
	URL url = new URL(params[0]);
	// 取得链接
	URLConnection conn = url.openConnection();
	conn.connect();
	// 取得返回的InputStream
	InputStream is = conn.getInputStream();

	// 将InputStream变为Bitmap

	image = BitmapFactory.decodeStream(is);
	is.close();
	} catch (Exception e) {
	e.printStackTrace();
	}
	return image;
	}
}
