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
			// ����ֻ�������SD��������Ӧ�ó�����з���SD��Ȩ��
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				// ��ȡSD����Ӧ�Ĵ洢Ŀ¼
				File sdCardDir = Environment.getExternalStorageDirectory();
				// ��ȡָ���ļ���Ӧ��������
				FileInputStream fis = new FileInputStream(sdCardDir.getCanonicalPath() + inFileName);
				// ��ָ����������װ��BufferedReader
				BufferedReader br = new BufferedReader(new 
					InputStreamReader(fis));
				StringBuilder sb = new StringBuilder("");
				String line = null;
				// ѭ����ȡ�ļ�����				
				while ((line = br.readLine()) != null)
				{
					sb.append(line);
				}
				// �ر���Դ
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
			// ����ֻ�������SD��������Ӧ�ó�����з���SD��Ȩ��
			if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			{
				// ��ȡSD����Ŀ¼
				File sdCardDir = Environment.getExternalStorageDirectory();
				File targetFile = new File(sdCardDir.getCanonicalPath() + outFileName);
				// ��ָ���ļ����� RandomAccessFile����
				RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
				// ���ļ���¼ָ���ƶ������
				raf.seek(targetFile.length());
				// ����ļ�����
				raf.write(content.getBytes());
				// �ر�RandomAccessFile				
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
