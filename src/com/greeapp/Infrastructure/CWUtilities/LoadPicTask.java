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
	
	// doInBackground��ɺ�Żᱻ����
	@Override
	protected void onPostExecute(Bitmap bitmap) {
	// ����setTag����ͼƬ�Ա����Զ�����ͼƬ
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
	
	// ����������ͼƬ
	@Override
	protected Bitmap doInBackground(String... params) {
	Bitmap image = null;
	try {
	// new URL���� ����ַ����
	URL url = new URL(params[0]);
	// ȡ������
	URLConnection conn = url.openConnection();
	conn.connect();
	// ȡ�÷��ص�InputStream
	InputStream is = conn.getInputStream();

	// ��InputStream��ΪBitmap

	image = BitmapFactory.decodeStream(is);
	is.close();
	} catch (Exception e) {
	e.printStackTrace();
	}
	return image;
	}
}
