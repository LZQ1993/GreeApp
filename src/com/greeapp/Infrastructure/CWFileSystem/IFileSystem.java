package com.greeapp.Infrastructure.CWFileSystem;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;



public interface IFileSystem {

	public void setContext(Context _cxt);
	
	public Context getContext();
	
	public String readTxt(String inFileName);

	public boolean write(String content,String outFileName);
	
	public List<Map<String,Object>> getFileList();
	
	public Bitmap getImage(String fileName);

	//
	//public String readImage(String inFileName);
	//
	//public String readTxt(String inFileName);

	//返回应用程序的安装路径
    public String getLocalPath();
}
