package com.greeapp.Infrastructure.CWFileSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

public class SDCardFileSystem extends AbsFileSystem {
 
    public SDCardFileSystem(Context _cxt){
    	setContext(_cxt);
    }
    
    @Override
	public String readTxt(String inFileName) {
    	try
		{
			// 如果手机插入了SD卡，而且应用程序具有访问SD的权限
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				// 获取SD卡对应的存储目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				// 获取指定文件对应的输入流
				FileInputStream fis = new FileInputStream(sdCardDir.getCanonicalPath() + inFileName);
				// 将指定输入流包装成BufferedReader
				BufferedReader br = new BufferedReader(new 
					InputStreamReader(fis));
				StringBuilder sb = new StringBuilder("");
				String line = null;
				// 循环读取文件内容				
				while ((line = br.readLine()) != null)
				{
					sb.append(line);
				}
				// 关闭资源
				br.close();
				return sb.toString();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean write(String content, String outFileName) {
		boolean result=false;
		try
		{
			// 如果手机插入了SD卡，而且应用程序具有访问SD的权限
			if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			{
				// 获取SD卡的目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				File targetFile = new File(sdCardDir.getCanonicalPath() + outFileName);
				// 以指定文件创建 RandomAccessFile对象
				RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
				// 将文件记录指针移动到最后
				raf.seek(targetFile.length());
				// 输出文件内容
				raf.write(content.getBytes());
				// 关闭RandomAccessFile				
				raf.close();
				result=true;
			}
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
		File root=new File("/mnt/sdcard/");
		File[] currentFiles;
		List<Map<String,Object>> filesAndFolders=new ArrayList<Map<String,Object>>();
		
		if(root.exists()){
			currentFiles=root.listFiles();		
			super.addFile(currentFiles,filesAndFolders);
		}
		return filesAndFolders;
	}
	
	
	public Bitmap getImage(String fileName){
		return null;
	}
}
