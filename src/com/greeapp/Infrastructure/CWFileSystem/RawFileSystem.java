package com.greeapp.Infrastructure.CWFileSystem;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

public class RawFileSystem extends AbsFileSystem {
    public RawFileSystem(Context _cxt){
    	setContext(_cxt);
    }
	@Override
	public String readTxt(String inFileName) {
		// 读取raw文件夹里的文件
		Resources res = getContext().getResources();
		int fileID = res.getIdentifier(inFileName, "raw", getContext().getPackageName());
		String text = null;
		try {
			InputStream in = getContext().getResources().openRawResource(fileID);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			text = EncodingUtils.getString(buffer, "UTF-8");
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
	
	public Bitmap getImage(String fileName){
		return null;
	}
}
