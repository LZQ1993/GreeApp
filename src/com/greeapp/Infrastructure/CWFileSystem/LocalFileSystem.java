package com.greeapp.Infrastructure.CWFileSystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;

public class LocalFileSystem extends AbsFileSystem{

    public LocalFileSystem(Context _cxt){
    	setContext(_cxt);
    }
    
    @Override
	public String readTxt(String inFileName)
	{
		try
		{
			// 打开文件输入流
			FileInputStream fis = getContext().openFileInput(inFileName);
			//FileInputStream fis = new FileInputStream(inFileName);
			byte[] buff = new byte[1024];
			int hasRead = 0;
			StringBuilder sb = new StringBuilder("");
			// 读取文件内容
			while ((hasRead = fis.read(buff)) > 0)
			{
				sb.append(new String(buff, 0, hasRead));
			}
			// 关闭文件输入流
			fis.close();
			return sb.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean write(String content,String outFileName)
	{
		boolean result=false;
		try
		{
			// 以追加模式打开文件输出流
			FileOutputStream fos = getContext().openFileOutput(outFileName, getContext().MODE_APPEND);
			// 将FileOutputStream包装成PrintStream
			PrintStream ps = new PrintStream(fos);
			// 输出文件内容
			ps.println(content);
			// 关闭文件输出流
			ps.close();
			result=true;
		}
		catch (Exception e)
		{
			result=false;
			e.printStackTrace();
		}
		return result;
	}

	
	@Override
	public List<Map<String, Object>> getFileList() {
		// TODO Auto-generated method stub
		return null;
	}


	public Bitmap getImage(String fileName){
		return null;
	}


}
