package com.greeapp.WaitWorkOrder.Assistant;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
*
*����·����ͼƬ���н���
* return bitmap
*/
public class Bimp {
	public static int max = 0;
	
	public static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();   //ѡ���ͼƬ����ʱ�б�

	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		/*�����������inJustDecodeBounds��������Ϊtrue�Ϳ����ý���������ֹΪbitmap�����ڴ棬
                                   ����ֵҲ������һ��Bitmap���󣬶���null��
                                   ��ȻBitmap��null�ˣ�����BitmapFactory.Options��outWidth��outHeight��outMimeType���Զ��ᱻ��ֵ��
         */
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
}
