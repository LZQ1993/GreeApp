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
	
	// doInBackground��ɺ�Żᱻ����
	@Override
	protected void onPostExecute(Bitmap bitmap) {
	// ����setTag����ͼƬ�Ա����Զ�����ͼƬ
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
	// �ӱ�������ͼƬ
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