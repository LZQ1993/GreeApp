package com.greeapp.Infrastructure.CWFileSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AssetFileSystem extends AbsFileSystem {
    
    public AssetFileSystem(Context _cxt){
    	setContext(_cxt);
    }
	@Override
	public String readTxt(String inFileName) {
		// 读取assets文件夹里的文件
				String text = null;
				try {
					InputStream is = getContext().getAssets().open(inFileName);
					InputStreamReader reader = new InputStreamReader(is);
					BufferedReader bufferedReader = new BufferedReader(reader);
					StringBuffer buffer = new StringBuffer("");
					String str;
					while ((str = bufferedReader.readLine()) != null) {
						buffer.append(str);
						buffer.append("\n");
					}				
					text = buffer.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return text;
	}
	
	public Bitmap getImage(String fileName)  
	  {  
	      Bitmap image = null;  
	      AssetManager am = getContext().getResources().getAssets();  
	      try  
	      {  
	          InputStream is = am.open(fileName);  
	          image = BitmapFactory.decodeStream(is);  
	          is.close();  
	      }  
	      catch (IOException e)  
	      {  
	          e.printStackTrace();  
	      }  
	  
	      return image;  
	  
	  }  
}
